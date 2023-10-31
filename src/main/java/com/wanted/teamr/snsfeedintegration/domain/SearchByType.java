package com.wanted.teamr.snsfeedintegration.domain;

import lombok.Getter;

@Getter
public enum SearchByType {

    TITLE("title"),
    CONTENT("content"),
    TITLE_CONTENT("title,content");

    private final String value;

    SearchByType(String value) {
        this.value = value;
    }

}
