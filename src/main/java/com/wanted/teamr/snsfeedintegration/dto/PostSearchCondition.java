package com.wanted.teamr.snsfeedintegration.dto;

import com.wanted.teamr.snsfeedintegration.domain.SearchByType;
import com.wanted.teamr.snsfeedintegration.domain.SnsType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearchCondition {

    private final String hashtag;
    private final SnsType type;
    private final SearchByType searchBy;
    private final String search;

    @Builder
    private PostSearchCondition(String hashtag, SnsType type, SearchByType searchBy, String search) {
        this.hashtag = hashtag;
        this.type = type;
        this.searchBy = searchBy;
        this.search = search;
    }

}
