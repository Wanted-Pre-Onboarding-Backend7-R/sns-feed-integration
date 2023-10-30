package com.wanted.teamr.snsfeedintegration.service;

import static com.wanted.teamr.snsfeedintegration.domain.StatisticsType.*;
import static com.wanted.teamr.snsfeedintegration.domain.StatisticsValue.*;
import static com.wanted.teamr.snsfeedintegration.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wanted.teamr.snsfeedintegration.dto.StatisticsGetRequest;
import com.wanted.teamr.snsfeedintegration.dto.StatisticsGetResponse;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql("/db/statistics.sql")
@Transactional
@SpringBootTest
class StatisticsServiceTest {

    private final String memberAccountName = "member1";

    @Autowired
    private StatisticsService statisticsService;

    @Test
    @DisplayName("특정 해시태그로 통계를 조회할 수 있다.")
    void getStatistics_hashtag() {
        //given
        StatisticsGetRequest request = StatisticsGetRequest.of(
                "hashtag1",
                DATE,
                LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                LocalDateTime.of(2023, 10, 30, 23, 59, 59),
                COUNT
        );

        //when
        List<StatisticsGetResponse> responses = statisticsService.getStatistics(request, memberAccountName);

        //then
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getStatisticsAt()).isEqualTo("2023-10-01");
        assertThat(responses.get(0).getCount()).isEqualTo(2);
        assertThat(responses.get(1).getStatisticsAt()).isEqualTo("2023-10-15");
        assertThat(responses.get(1).getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("일자별 count 통계를 조회할 수 있다.")
    void getStatistics_daily_count() {
        //given
        StatisticsGetRequest request = StatisticsGetRequest.of(
                null,
                DATE,
                LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                LocalDateTime.of(2023, 10, 30, 23, 59, 59),
                COUNT
        );

        //when
        List<StatisticsGetResponse> responses = statisticsService.getStatistics(request, memberAccountName);

        //then
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getStatisticsAt()).isEqualTo("2023-10-01");
        assertThat(responses.get(0).getCount()).isEqualTo(3);
        assertThat(responses.get(1).getStatisticsAt()).isEqualTo("2023-10-20");
        assertThat(responses.get(1).getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("일자별 viewCount 통계를 조회할 수 있다.")
    void getStatistics_daily_viewCount() {
        //given
        StatisticsGetRequest request = StatisticsGetRequest.of(
                null,
                DATE,
                LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                LocalDateTime.of(2023, 10, 30, 23, 59, 59),
                VIEWCOUNT
        );

        //when
        List<StatisticsGetResponse> responses = statisticsService.getStatistics(request, memberAccountName);

        //then
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getStatisticsAt()).isEqualTo("2023-10-01");
        assertThat(responses.get(0).getCount()).isEqualTo(450);
        assertThat(responses.get(1).getStatisticsAt()).isEqualTo("2023-10-20");
        assertThat(responses.get(1).getCount()).isEqualTo(650);
    }

    @Test
    @DisplayName("일자별 likeCount 통계를 조회할 수 있다.")
    void getStatistics_daily_likeCount() {
        //given
        StatisticsGetRequest request = StatisticsGetRequest.of(
                null,
                DATE,
                LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                LocalDateTime.of(2023, 10, 30, 23, 59, 59),
                LIKECOUNT
        );

        //when
        List<StatisticsGetResponse> responses = statisticsService.getStatistics(request, memberAccountName);

        //then
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getStatisticsAt()).isEqualTo("2023-10-01");
        assertThat(responses.get(0).getCount()).isEqualTo(45);
        assertThat(responses.get(1).getStatisticsAt()).isEqualTo("2023-10-20");
        assertThat(responses.get(1).getCount()).isEqualTo(65);
    }

    @Test
    @DisplayName("일자별 shareCount 통계를 조회할 수 있다.")
    void getStatistics_daily_shareCount() {
        //given
        StatisticsGetRequest request = StatisticsGetRequest.of(
                null,
                DATE,
                LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                LocalDateTime.of(2023, 10, 30, 23, 59, 59),
                SHARECOUNT
        );

        //when
        List<StatisticsGetResponse> responses = statisticsService.getStatistics(request, memberAccountName);

        //then
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getStatisticsAt()).isEqualTo("2023-10-01");
        assertThat(responses.get(0).getCount()).isEqualTo(22);
        assertThat(responses.get(1).getStatisticsAt()).isEqualTo("2023-10-20");
        assertThat(responses.get(1).getCount()).isEqualTo(32);
    }

    @Test
    @DisplayName("시간별 count 통계를 조회할 수 있다.")
    void getStatistics_hourly_count() {
        //given
        StatisticsGetRequest request = StatisticsGetRequest.of(
                null,
                HOUR,
                LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                LocalDateTime.of(2023, 10, 7, 23, 59, 59),
                COUNT
        );

        //when
        List<StatisticsGetResponse> responses = statisticsService.getStatistics(request, memberAccountName);

        //then
        assertThat(responses.size()).isEqualTo(3);
        assertThat(responses.get(0).getStatisticsAt()).isEqualTo("2023-10-01T12");
        assertThat(responses.get(0).getCount()).isEqualTo(1);
        assertThat(responses.get(1).getStatisticsAt()).isEqualTo("2023-10-01T13");
        assertThat(responses.get(1).getCount()).isEqualTo(1);
        assertThat(responses.get(2).getStatisticsAt()).isEqualTo("2023-10-01T14");
        assertThat(responses.get(2).getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("통계값 없이 통계를 조회할 수 있다.")
    void getStatistics_noValue() {
        //given
        StatisticsGetRequest request = StatisticsGetRequest.of(
                "hashtag1",
                DATE,
                LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                LocalDateTime.of(2023, 10, 30, 23, 59, 59),
                null
        );

        //when
        List<StatisticsGetResponse> responses = statisticsService.getStatistics(request, memberAccountName);
        for (StatisticsGetResponse respons : responses) {
            System.out.println("respons = " + respons);
        }

        //then
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getStatisticsAt()).isEqualTo("2023-10-01");
        assertThat(responses.get(0).getCount()).isEqualTo(2);
        assertThat(responses.get(1).getStatisticsAt()).isEqualTo("2023-10-15");
        assertThat(responses.get(1).getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("잘못된 해시태그로 통계를 조회할 수 없다.")
    void getStatistics_invalidHashtag() {
        //given
        StatisticsGetRequest request = StatisticsGetRequest.of(
                "invalid",
                DATE,
                LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                LocalDateTime.of(2023, 10, 7, 23, 59, 59),
                COUNT
        );

        //when then
        assertThatThrownBy(() -> statisticsService.getStatistics(request, memberAccountName))
                .isInstanceOf(CustomException.class)
                .hasMessage(STATISTICS_HASHTAG_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("start 미입력 시 조회 기간을 초과하면 시간별 count 통계를 조회할 수 없다.")
    void getStatistics_invalidPeriod_start() {
        //given
        StatisticsGetRequest request = StatisticsGetRequest.of(
                null,
                HOUR,
                LocalDateTime.of(2100, 1, 1, 0, 0, 0),
                null,
                COUNT
        );

        //when then
        assertThatThrownBy(() -> statisticsService.getStatistics(request, memberAccountName))
                .isInstanceOf(CustomException.class)
                .hasMessage(STATISTICS_PERIOD_INVALID.getMessage());
    }

    @Test
    @DisplayName("end 미입력 시 조회 기간을 초과하면 시간별 count 통계를 조회할 수 없다.")
    void getStatistics_invalidPeriod_end() {
        //given
        StatisticsGetRequest request = StatisticsGetRequest.of(
                null,
                HOUR,
                null,
                LocalDateTime.of(2023, 10, 7, 23, 59, 59),
                COUNT
        );

        //when then
        assertThatThrownBy(() -> statisticsService.getStatistics(request, memberAccountName))
                .isInstanceOf(CustomException.class)
                .hasMessage(STATISTICS_PERIOD_INVALID.getMessage());
    }

    @Test
    @DisplayName("조회 기간을 초과하면 일자별 count 통계를 조회할 수 없다.")
    void getStatistics_invalidPeriod_daily() {
        //given
        StatisticsGetRequest request = StatisticsGetRequest.of(
                null,
                DATE,
                LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                LocalDateTime.of(2023, 10, 31, 0, 0, 0),
                COUNT
        );

        //when then
        assertThatThrownBy(() -> statisticsService.getStatistics(request, memberAccountName))
                .isInstanceOf(CustomException.class)
                .hasMessage(STATISTICS_PERIOD_INVALID.getMessage());
    }

    @Test
    @DisplayName("조회 기간을 초과하면 시간별 count 통계를 조회할 수 없다.")
    void getStatistics_invalidPeriod_hourly() {
        //given
        StatisticsGetRequest request = StatisticsGetRequest.of(
                null,
                HOUR,
                LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                LocalDateTime.of(2023, 10, 8, 23, 59, 59),
                COUNT
        );

        //when then
        assertThatThrownBy(() -> statisticsService.getStatistics(request, memberAccountName))
                .isInstanceOf(CustomException.class)
                .hasMessage(STATISTICS_PERIOD_INVALID.getMessage());
    }
}
