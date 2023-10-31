package com.wanted.teamr.snsfeedintegration.repository;

import com.wanted.teamr.snsfeedintegration.dto.StatisticsGetRequest;
import com.wanted.teamr.snsfeedintegration.dto.StatisticsGetResponse;
import java.util.List;

public interface PostHashtagRepositoryCustom {

    List<StatisticsGetResponse> getCountByCreatedAt(StatisticsGetRequest request);
}
