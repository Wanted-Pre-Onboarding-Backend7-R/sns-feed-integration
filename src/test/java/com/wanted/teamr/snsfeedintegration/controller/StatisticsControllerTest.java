package com.wanted.teamr.snsfeedintegration.controller;

import static com.wanted.teamr.snsfeedintegration.domain.StatisticsType.*;
import static com.wanted.teamr.snsfeedintegration.domain.StatisticsValue.*;
import static com.wanted.teamr.snsfeedintegration.exception.ErrorCode.*;
import static com.wanted.teamr.snsfeedintegration.exception.RequestBodyErrorCode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Sql("/db/statistics.sql")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("특정 해시태그로 통계를 조회할 수 있다.")
    void getStatistics_hashtag() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("hashtag", "hashtag1")
                        .param("type", DATE.name())
                        .param("start", "2023-10-01T00:00:00")
                        .param("end", "2023-10-30T00:00:00")
                        .param("value", COUNT.name())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].statisticsAt").value("2023-10-01"))
                .andExpect(jsonPath("$[0].count").value(2))
                .andExpect(jsonPath("$[1].statisticsAt").value("2023-10-15"))
                .andExpect(jsonPath("$[1].count").value(1));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("일자별 count 통계를 조회할 수 있다.")
    void getStatistics_daily_count() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("type", DATE.name())
                        .param("start", "2023-10-01T00:00:00")
                        .param("end", "2023-10-30T00:00:00")
                        .param("value", COUNT.name())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].statisticsAt").value("2023-10-01"))
                .andExpect(jsonPath("$[0].count").value(3))
                .andExpect(jsonPath("$[1].statisticsAt").value("2023-10-20"))
                .andExpect(jsonPath("$[1].count").value(1));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("일자별 viewCount 통계를 조회할 수 있다.")
    void getStatistics_daily_viewCount() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("type", DATE.name())
                        .param("start", "2023-10-01T00:00:00")
                        .param("end", "2023-10-30T00:00:00")
                        .param("value", VIEWCOUNT.name())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].statisticsAt").value("2023-10-01"))
                .andExpect(jsonPath("$[0].count").value(450))
                .andExpect(jsonPath("$[1].statisticsAt").value("2023-10-20"))
                .andExpect(jsonPath("$[1].count").value(650));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("일자별 likeCount 통계를 조회할 수 있다.")
    void getStatistics_daily_likeCount() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("type", DATE.name())
                        .param("start", "2023-10-01T00:00:00")
                        .param("end", "2023-10-30T00:00:00")
                        .param("value", LIKECOUNT.name())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].statisticsAt").value("2023-10-01"))
                .andExpect(jsonPath("$[0].count").value(45))
                .andExpect(jsonPath("$[1].statisticsAt").value("2023-10-20"))
                .andExpect(jsonPath("$[1].count").value(65));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("일자별 shareCount 통계를 조회할 수 있다.")
    void getStatistics_daily_shareCount() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("type", DATE.name())
                        .param("start", "2023-10-01T00:00:00")
                        .param("end", "2023-10-30T00:00:00")
                        .param("value", SHARECOUNT.name())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].statisticsAt").value("2023-10-01"))
                .andExpect(jsonPath("$[0].count").value(22))
                .andExpect(jsonPath("$[1].statisticsAt").value("2023-10-20"))
                .andExpect(jsonPath("$[1].count").value(32));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("시간별 count 통계를 조회할 수 있다.")
    void getStatistics_hourly_count() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("type", HOUR.name())
                        .param("start", "2023-10-01T00:00:00")
                        .param("end", "2023-10-07T23:59:59")
                        .param("value", COUNT.name())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].statisticsAt").value("2023-10-01T12"))
                .andExpect(jsonPath("$[0].count").value(1))
                .andExpect(jsonPath("$[1].statisticsAt").value("2023-10-01T13"))
                .andExpect(jsonPath("$[1].count").value(1))
                .andExpect(jsonPath("$[2].statisticsAt").value("2023-10-01T14"))
                .andExpect(jsonPath("$[2].count").value(1));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("통계값 없이 통계를 조회할 수 있다.")
    void getStatistics_noValue() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("hashtag", "hashtag1")
                        .param("type", DATE.name())
                        .param("start", "2023-10-01T00:00:00")
                        .param("end", "2023-10-30T23:59:59")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].statisticsAt").value("2023-10-01"))
                .andExpect(jsonPath("$[0].count").value(2))
                .andExpect(jsonPath("$[1].statisticsAt").value("2023-10-15"))
                .andExpect(jsonPath("$[1].count").value(1));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("잘못된 해시태그로 통계를 조회할 수 없다.")
    void getStatistics_invalidHashtag() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("hashtag", "invalid")
                        .param("type", DATE.name())
                        .param("start", "2023-10-01T00:00:00")
                        .param("end", "2023-10-30T23:59:59")
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(STATISTICS_HASHTAG_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(STATISTICS_HASHTAG_NOT_FOUND.getMessage()));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("start 미입력 시 조회 기간을 초과하면 시간별 count 통계를 조회할 수 없다.")
    void getStatistics_invalidPeriod_start() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("type", DATE.name())
                        .param("end", "2100-01-01T00:00:00")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(STATISTICS_PERIOD_INVALID.name()))
                .andExpect(jsonPath("$.message").value(STATISTICS_PERIOD_INVALID.getMessage()));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("end 미입력 시 조회 기간을 초과하면 시간별 count 통계를 조회할 수 없다.")
    void getStatistics_invalidPeriod_end() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("type", DATE.name())
                        .param("start", "2020-01-01T00:00:00")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(STATISTICS_PERIOD_INVALID.name()))
                .andExpect(jsonPath("$.message").value(STATISTICS_PERIOD_INVALID.getMessage()));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("조회 기간을 초과하면 일자별 count 통계를 조회할 수 없다.")
    void getStatistics_invalidPeriod_daily() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("type", DATE.name())
                        .param("start", "2023-01-01T00:00:00")
                        .param("end", "2023-01-31T23:59:59")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(STATISTICS_PERIOD_INVALID.name()))
                .andExpect(jsonPath("$.message").value(STATISTICS_PERIOD_INVALID.getMessage()));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("조회 기간을 초과하면 시간별 count 통계를 조회할 수 없다.")
    void getStatistics_invalidPeriod_hourly() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("type", HOUR.name())
                        .param("start", "2023-01-01T00:00:00")
                        .param("end", "2023-01-08T23:59:59")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(STATISTICS_PERIOD_INVALID.name()))
                .andExpect(jsonPath("$.message").value(STATISTICS_PERIOD_INVALID.getMessage()));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("유효하지 않은 통계유형으로 통계를 조회할 수 없다.")
    void getStatistics_invalidType() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("type", "TIME")
                        .param("start", "2023-01-01T00:00:00")
                        .param("end", "2023-01-08T23:59:59")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(STATISTICS_STATISTICSTYPE_INVALID.name()))
                .andExpect(jsonPath("$.message").value(STATISTICS_STATISTICSTYPE_INVALID.getMessage()));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("유효하지 않은 통계값으로 통계를 조회할 수 없다.")
    void getStatistics_invalidValue() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("type", DATE.name())
                        .param("start", "2023-01-01T00:00:00")
                        .param("end", "2023-01-08T23:59:59")
                        .param("value", "TEST")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(STATISTICS_STATISTICSVALUE_INVALID.name()))
                .andExpect(jsonPath("$.message").value(STATISTICS_STATISTICSVALUE_INVALID.getMessage()));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("유효하지 않은 통계 시작일시로 통계를 조회할 수 없다.")
    void getStatistics_invalidStart() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("type", DATE.name())
                        .param("start", "2023.01.01T00:00:00")
                        .param("end", "2023-01-08T23:59:59")
                        .param("value", "TEST")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(STATISTICS_START_INVALID.name()))
                .andExpect(jsonPath("$.message").value(STATISTICS_START_INVALID.getMessage()));
    }

    @Test
    @WithUserDetails(value = "member1")
    @DisplayName("유효하지 않은 통계 종료일시로 통계를 조회할 수 없다.")
    void getStatistics_invalidEnd() throws Exception {
        //given, when, then
        mockMvc.perform(get("/api/statistics")
                        .param("type", DATE.name())
                        .param("start", "2023-01-01T00:00:00")
                        .param("end", "2023.01.08T23:59:59")
                        .param("value", "TEST")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(STATISTICS_END_INVALID.name()))
                .andExpect(jsonPath("$.message").value(STATISTICS_END_INVALID.getMessage()));
    }
}
