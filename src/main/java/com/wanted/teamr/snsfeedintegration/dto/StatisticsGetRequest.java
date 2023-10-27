package com.wanted.teamr.snsfeedintegration.dto;

import com.wanted.teamr.snsfeedintegration.domain.StatisticsType;
import com.wanted.teamr.snsfeedintegration.domain.StatisticsValue;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StatisticsGetRequest {

    private String hashtag;
    private final StatisticsType type;
    private LocalDateTime start;
    private LocalDateTime end;
    private StatisticsValue value;

    @Builder(access = AccessLevel.PRIVATE)
    private StatisticsGetRequest(String hashtag, StatisticsType type, LocalDateTime start, LocalDateTime end, StatisticsValue value) {
        this.hashtag = hashtag;
        this.type = type;
        this.start = start;
        this.end = end;
        this.value = value;
    }

    public static StatisticsGetRequest of(String hashtag, StatisticsType type, LocalDateTime start, LocalDateTime end, StatisticsValue value) {
        return StatisticsGetRequest.builder()
                .hashtag(hashtag)
                .type(type)
                .start(start)
                .end(end)
                .value(value)
                .build();
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setValue(StatisticsValue statisticsValue) {
        this.value = statisticsValue;
    }
}
