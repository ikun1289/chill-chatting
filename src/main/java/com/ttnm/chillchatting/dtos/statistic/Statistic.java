package com.ttnm.chillchatting.dtos.statistic;

import com.ttnm.chillchatting.dtos.statistic.embed.ChartStatistic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Statistic {
    private int totalMessagesToday;

    private int totalMessageThisMonth;

    private List<ChartStatistic> statisticMessageForThePastMonth;

}
