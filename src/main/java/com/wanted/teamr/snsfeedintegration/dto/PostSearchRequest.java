package com.wanted.teamr.snsfeedintegration.dto;

import lombok.Getter;

@Getter
public class PostSearchRequest {

    private String hashtag;
    private String type;
    private String searchBy;
    private String search;

    private PostSearchRequest(String hashtag, String type, String searchBy, String search) {
        this.hashtag = hashtag;
        this.type = type;
        this.searchBy = searchBy;
        this.search = search;
    }

}
