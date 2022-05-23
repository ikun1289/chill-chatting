package com.ttnm.chillchatting.services.message;


import com.ttnm.chillchatting.configs.jwt.JwtAuthenticationFilter;
import com.ttnm.chillchatting.configs.jwt.JwtTokenProvider;
import com.ttnm.chillchatting.dtos.MyEnum;
import com.ttnm.chillchatting.dtos.RegistNameRequest;
import com.ttnm.chillchatting.dtos.RegistNameResponse;
import com.ttnm.chillchatting.dtos.message.MessageDto;
import com.ttnm.chillchatting.dtos.statistic.MapStatistic;
import com.ttnm.chillchatting.dtos.statistic.Statistic;
import com.ttnm.chillchatting.dtos.statistic.embed.ChartStatistic;
import com.ttnm.chillchatting.entities.Message;
import com.ttnm.chillchatting.exceptions.InvalidException;
import com.ttnm.chillchatting.repositories.MessageRepository;
import com.ttnm.chillchatting.services.badword.BadWordService;
import com.ttnm.chillchatting.utils.EnumChannel;
import com.ttnm.chillchatting.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService{

    private final MessageRepository messageRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final PasswordEncoder passwordEncoder;

    private final MongoTemplate mongoTemplate;

    private final BadWordService badWordService;

    private static final String SECRET = "xhuuanng";

    public MessageServiceImpl(MessageRepository messageRepository, JwtTokenProvider jwtTokenProvider, JwtAuthenticationFilter jwtAuthenticationFilter, PasswordEncoder passwordEncoder, MongoTemplate mongoTemplate, BadWordService badWordService) {
        this.messageRepository = messageRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.passwordEncoder = passwordEncoder;
        this.mongoTemplate = mongoTemplate;
        this.badWordService = badWordService;
    }

    @Override
    public List<MyEnum> getKenhs() {
        return Arrays.stream(EnumChannel.values())
                .map(vaiTroBaiBao -> new MyEnum(vaiTroBaiBao.getKey(), vaiTroBaiBao.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public RegistNameResponse dangKyTenChat(RegistNameRequest dto) {
        String guestName = dto.getName();

        if(ObjectUtils.isEmpty(guestName))
            throw new InvalidException("Tên không được để trống");
        if(guestName.length()<6)
            throw new InvalidException("Tên không được ngắn hơn 6 ký tự");
        if(guestName.length()>16)
            throw new InvalidException("Tên không được dài hơn 16 ký tự");

        int i = new Random().nextInt(999);
        guestName = guestName.replaceAll("[-+.^:,<>()]","");
        String checkName = guestName +"#"+ i;
        while (messageRepository.checkIfGuestNameExists(checkName))
        {
            i = new Random().nextInt(999);
            checkName = guestName + "#"+i;
        }
        String token = passwordEncoder.encode(SECRET+checkName);
        System.out.println(checkName);
        return new RegistNameResponse(checkName, token);
    }

    @Override
    public Message guiMessage(MessageDto dto){
        if(!passwordEncoder.matches(SECRET+dto.getGuestName(),dto.getToken()))
            throw new InvalidException("Tên không hợp với token đã gửi");
        if(ObjectUtils.isEmpty(dto.getMessage()))
            throw new InvalidException("Message không được để trống");
        if(dto.getMessage().length()>100)
            throw new InvalidException("Message quá dài");
        if(getKenhs().stream().noneMatch(myEnum -> myEnum.getKey().equals(dto.getChannel())))
            throw new InvalidException("Channel không hợp lệ");

        Message message = new Message();
        message.setMessage(dto.getMessage());
        message.setChannel(dto.getChannel());
        message.setGuestName(dto.getGuestName());
        message.setCreatedDate(new Date());

        message = messageRepository.save(message);
        message.setMessage(badWordService.filter(message.getMessage()));
        return message;

    }

    @Override
    public List<Message> getTheLatestMessages(String kenh){
        Pageable pageable = PageUtils.createPageable(0, 20, "desc", "createdDate");
        List<Message> result = messageRepository.getListMessageWithLimit(kenh, pageable);
        Collections.reverse(result);

        for (Message message: result) {
            message.setMessage(badWordService.filter(message.getMessage()));
        }

        return result;
    }

    public Date removeTime(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public Statistic getStatistic(){
        Date today = removeTime(new Date());
        Calendar cal = Calendar.getInstance();

        //today count
        cal.setTime(today);
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();
        int totalMessagesToday = messageRepository.countMessageByDateRange(today, tomorrow);

        //this month count
        cal.setTime(today);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDate = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        Date lastDate = cal.getTime();
        int totalMessageThisMonth = messageRepository.countMessageByDateRange(firstDate, lastDate);

        //statistic
        List<MapStatistic> listStatistic = testStatistic(firstDate, lastDate);
        cal.setTime(today);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int myMonth=cal.get(Calendar.MONTH);
        List<Date> dateInMonth = new ArrayList<>();
        List<ChartStatistic> chartStatisticList = new ArrayList<>();
        while (myMonth==cal.get(Calendar.MONTH)) {
            dateInMonth.add(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        for (Date date:dateInMonth) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date);
            MapStatistic mapStatistic = listStatistic.stream().filter(mapStatistic1 -> mapStatistic1.getDate().getDay() == cal1.get(Calendar.DAY_OF_MONTH)).findFirst().orElse(null);
            ChartStatistic chartStatistic = new ChartStatistic();
            if(ObjectUtils.isEmpty(mapStatistic)) {
                chartStatistic.setDate(cal1.get(Calendar.DAY_OF_MONTH));
                chartStatistic.setCount(0);
            }
            else {
                chartStatistic.setDate(mapStatistic.getDate().getDay());
                chartStatistic.setCount(mapStatistic.getCount());
            }
            chartStatisticList.add(chartStatistic);
        }

        //statistic
        Statistic statistic = new Statistic();
        statistic.setTotalMessagesToday(totalMessagesToday);
        statistic.setTotalMessageThisMonth(totalMessageThisMonth);
        statistic.setStatisticMessageForThePastMonth(chartStatisticList);
        return statistic;

    }

    public List<MapStatistic> testStatistic(Date firstDate, Date lastDate) {
        Criteria criteria = new Criteria();

        criteria.and("createdDate").gte(firstDate).lte(lastDate);

        MatchOperation searchOperation = match(criteria);

        ProjectionOperation projectionOperation = project("createdDate")
                .and(DateOperators.Year.yearOf("createdDate"))
                .as("year")
                .and(DateOperators.Month.monthOf("createdDate"))
                .as("month")
                .and(DateOperators.DayOfMonth.dayOfMonth("createdDate"))
                .as("day");

        GroupOperation groupOperation = group("year","month","day").count().as("count");

        SortOperation sortOperation = sort(Sort.Direction.ASC, "id.day");

        Aggregation aggregation = newAggregation(
                searchOperation,
                projectionOperation,
                groupOperation
        );


        AggregationResults<MapStatistic> aggregate = mongoTemplate.aggregate(aggregation,
                Message.class, MapStatistic.class);
        List<MapStatistic> documents = aggregate.getMappedResults();

        return documents;
    }

}
