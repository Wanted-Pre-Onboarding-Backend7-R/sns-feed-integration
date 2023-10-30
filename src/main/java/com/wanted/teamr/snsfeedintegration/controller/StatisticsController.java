package com.wanted.teamr.snsfeedintegration.controller;

import com.wanted.teamr.snsfeedintegration.dto.StatisticsGetRequest;
import com.wanted.teamr.snsfeedintegration.dto.StatisticsGetResponse;
import com.wanted.teamr.snsfeedintegration.service.StatisticsService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/api/statistics")
    public ResponseEntity<List<StatisticsGetResponse>> getStatistics(@ModelAttribute @Valid StatisticsGetRequest statisticsGetRequest) {
        //TODO: 시큐리티 Merge 후 세션정보(accountName) 받기 위해 재작업 필요.
        String tempAccountName = "member1";
        List<StatisticsGetResponse> statisticsGetResponses = statisticsService.getStatistics(statisticsGetRequest, tempAccountName);
        return ResponseEntity.ok(statisticsGetResponses);
    }
}