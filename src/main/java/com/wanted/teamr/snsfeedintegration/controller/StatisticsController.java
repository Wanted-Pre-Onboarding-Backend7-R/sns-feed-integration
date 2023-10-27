package com.wanted.teamr.snsfeedintegration.controller;

import com.wanted.teamr.snsfeedintegration.dto.StatisticsGetRequest;
import com.wanted.teamr.snsfeedintegration.dto.StatisticsGetResponse;
import com.wanted.teamr.snsfeedintegration.service.StatisticsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/api/statistics")
    public ResponseEntity<List<StatisticsGetResponse>> getStatistics(@RequestParam StatisticsGetRequest statisticsGetRequest) {
        //TODO: 시큐리티 Merge 후 세션정보(accountName) 받기 위해 재작업 필요.
        String tempAccountName = "member1";
        List<StatisticsGetResponse> statisticsGetResponses = statisticsService.getStatistics(statisticsGetRequest, tempAccountName);
        return ResponseEntity.ok(statisticsGetResponses);
    }
}
