package com.ttnm.chillchatting.dtos.statistic.embed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChartStatistic {

    private Date time;

    private int count;
}
