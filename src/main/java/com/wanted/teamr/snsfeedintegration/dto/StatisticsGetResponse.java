package com.wanted.teamr.snsfeedintegration.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StatisticsGetResponse {

    private final String statisticsAt;
    private final Long count;

    @Builder
    public StatisticsGetResponse(String statisticsAt, Long count) {
        this.statisticsAt = statisticsAt;
        this.count = count;
    }
}
