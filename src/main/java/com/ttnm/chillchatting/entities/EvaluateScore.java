package com.ttnm.chillchatting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="evaluate-score")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateScore {
    @Id
    @JsonIgnore
    private String id;
    private double overallScore;
    private double chatScore;
    private double interfaceScore;
}
