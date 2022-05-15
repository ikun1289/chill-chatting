package com.ttnm.chillchatting.dtos.statistic;

import com.ttnm.chillchatting.dtos.statistic.embed.MapStatisticId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapStatistic {
    @Id
    private MapStatisticId date;

    private int count;
}
