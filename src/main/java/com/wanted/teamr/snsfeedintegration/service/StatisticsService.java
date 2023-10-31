package com.wanted.teamr.snsfeedintegration.service;

import static com.wanted.teamr.snsfeedintegration.exception.ErrorCode.*;

import com.wanted.teamr.snsfeedintegration.domain.PostHashtag;
import com.wanted.teamr.snsfeedintegration.domain.StatisticsType;
import com.wanted.teamr.snsfeedintegration.domain.StatisticsValue;
import com.wanted.teamr.snsfeedintegration.dto.StatisticsGetRequest;
import com.wanted.teamr.snsfeedintegration.dto.StatisticsGetResponse;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.repository.PostHashtagRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private static final long STATISTICS_MAX_DAILY_PERIOD = 30;
    private static final long STATISTICS_MAX_HOURLY_PERIOD = 7;

    private final PostHashtagRepository postHashtagRepository;

    @Transactional(readOnly = true)
    public List<StatisticsGetResponse> getStatistics(StatisticsGetRequest statisticsGetRequest, String memberAccountName) {
        validateStatisticsOptions(statisticsGetRequest, memberAccountName);

        return postHashtagRepository.getCountByCreatedAt(statisticsGetRequest);
    }

    private void validateStatisticsOptions(StatisticsGetRequest statisticsGetRequest, String memberAccountName) {
        validateHashtag(statisticsGetRequest, memberAccountName);
        validateStartAndEnd(statisticsGetRequest);
        validateValue(statisticsGetRequest);
    }

    private void validateHashtag(StatisticsGetRequest statisticsGetRequest, String memberAccountName) {
        setDefaultHashtag(statisticsGetRequest, memberAccountName);
        findPostHashtagByHashtag(statisticsGetRequest);
    }

    private void setDefaultHashtag(StatisticsGetRequest statisticsGetRequest, String memberAccountName) {
        if (!StringUtils.hasText(statisticsGetRequest.getHashtag())) {
            statisticsGetRequest.setHashtag(memberAccountName);
        }
    }

    private void findPostHashtagByHashtag(StatisticsGetRequest statisticsGetRequest) {
        List<PostHashtag> postHashtags = postHashtagRepository.findByHashtag(statisticsGetRequest.getHashtag());
        if (postHashtags.isEmpty()) {
            throw new CustomException(STATISTICS_HASHTAG_NOT_FOUND);
        }
    }

    private void validateStartAndEnd(StatisticsGetRequest statisticsGetRequest) {
        setDefaultStartAndEnd(statisticsGetRequest);
        if (StatisticsType.DATE == statisticsGetRequest.getType()) {
            validatePeriod(statisticsGetRequest, STATISTICS_MAX_DAILY_PERIOD);
        }
        if (StatisticsType.HOUR == statisticsGetRequest.getType()) {
            validatePeriod(statisticsGetRequest, STATISTICS_MAX_HOURLY_PERIOD);
        }
    }

    private void setDefaultStartAndEnd(StatisticsGetRequest statisticsGetRequest) {
        LocalDateTime start = statisticsGetRequest.getStart();
        LocalDateTime end = statisticsGetRequest.getEnd();

        LocalDateTime now = LocalDateTime.now();
        if (start == null) {
            start = now.minusDays(STATISTICS_MAX_HOURLY_PERIOD);
        }
        if (end == null) {
            end = now;
        }

        if (StatisticsType.DATE == statisticsGetRequest.getType()) {
            start = start.with(LocalTime.MIN);
            end = end.with(LocalTime.MIN);
        }

        statisticsGetRequest.setStart(start);
        statisticsGetRequest.setEnd(end);
    }

    private void validatePeriod(StatisticsGetRequest statisticsGetRequest, Long maxPeriod) {
        LocalDateTime start = statisticsGetRequest.getStart();
        LocalDateTime end = statisticsGetRequest.getEnd();

        if (start.isAfter(end)) {
            throw new CustomException(STATISTICS_PERIOD_INVALID);
        }

        LocalDateTime limitDateTime = end.minusDays(maxPeriod);
        if (start.isBefore(limitDateTime) || start.isEqual(limitDateTime)) {
            throw new CustomException(STATISTICS_PERIOD_INVALID);
        }
    }

    private void validateValue(StatisticsGetRequest statisticsGetRequest) {
        if (statisticsGetRequest.getValue() == null) {
            statisticsGetRequest.setValue(StatisticsValue.COUNT);
        }
    }
}
