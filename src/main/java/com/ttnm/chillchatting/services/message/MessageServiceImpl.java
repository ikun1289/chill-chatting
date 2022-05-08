package com.ttnm.chillchatting.services.message;


import com.ttnm.chillchatting.configs.jwt.JwtAuthenticationFilter;
import com.ttnm.chillchatting.configs.jwt.JwtTokenProvider;
import com.ttnm.chillchatting.dtos.MyEnum;
import com.ttnm.chillchatting.dtos.RegistNameRequest;
import com.ttnm.chillchatting.dtos.RegistNameResponse;
import com.ttnm.chillchatting.dtos.message.MessageDto;
import com.ttnm.chillchatting.entities.Message;
import com.ttnm.chillchatting.exceptions.InvalidException;
import com.ttnm.chillchatting.repositories.MessageRepository;
import com.ttnm.chillchatting.utils.EnumChannel;
import com.ttnm.chillchatting.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService{

    private final MessageRepository messageRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final PasswordEncoder passwordEncoder;

    private static final String SECRET = "xhuuanng";

    public MessageServiceImpl(MessageRepository messageRepository, JwtTokenProvider jwtTokenProvider, JwtAuthenticationFilter jwtAuthenticationFilter, PasswordEncoder passwordEncoder) {
        this.messageRepository = messageRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.passwordEncoder = passwordEncoder;
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
        if(getKenhs().stream().noneMatch(myEnum -> myEnum.getKey().equals(dto.getChannel())))
            throw new InvalidException("Channel không hợp lệ");

        Message message = new Message();
        message.setMessage(dto.getMessage());
        message.setChannel(dto.getChannel());
        message.setGuestName(dto.getGuestName());
        message.setCreatedDate(new Date());

        return messageRepository.save(message);

    }

    @Override
    public List<Message> getTheLatestMessages(String kenh){
        Pageable pageable = PageUtils.createPageable(0, 20, "asc", "createdDate");
        return messageRepository.getListMessageWithLimit(kenh, pageable);
    }

}
