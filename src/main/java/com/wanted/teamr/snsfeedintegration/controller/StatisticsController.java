package com.wanted.teamr.snsfeedintegration.controller;

import com.wanted.teamr.snsfeedintegration.domain.UserDetailsImpl;
import com.wanted.teamr.snsfeedintegration.dto.StatisticsGetRequest;
import com.wanted.teamr.snsfeedintegration.dto.StatisticsGetResponse;
import com.wanted.teamr.snsfeedintegration.service.StatisticsService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/api/statistics")
    public ResponseEntity<List<StatisticsGetResponse>> getStatistics(@ModelAttribute @Valid StatisticsGetRequest statisticsGetRequest,
                                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String memberAccountName = userDetails.getMember().getAccountName();
        List<StatisticsGetResponse> statisticsGetResponses = statisticsService.getStatistics(statisticsGetRequest, memberAccountName);
        return ResponseEntity.ok(statisticsGetResponses);
    }
}
