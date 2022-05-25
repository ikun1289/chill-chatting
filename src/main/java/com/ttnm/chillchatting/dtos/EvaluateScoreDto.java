package com.ttnm.chillchatting.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateScoreDto {
    private int overallScore;
    private int chatScore;
    private int interfaceScore;
}
