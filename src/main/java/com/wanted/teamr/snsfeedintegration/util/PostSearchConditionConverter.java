package com.wanted.teamr.snsfeedintegration.util;

import com.wanted.teamr.snsfeedintegration.domain.SearchByType;
import com.wanted.teamr.snsfeedintegration.domain.SnsType;
import com.wanted.teamr.snsfeedintegration.dto.PostSearchCondition;
import com.wanted.teamr.snsfeedintegration.dto.PostSearchRequest;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import org.springframework.stereotype.Component;

import static org.springframework.util.StringUtils.hasText;

@Component
public class PostSearchConditionConverter {

    public PostSearchCondition convert(PostSearchRequest request, String defaultHashtag) {
        String hashtag = resolveHashtag(request.getHashtag(), defaultHashtag);
        SnsType snsType = resolveSnsType(request.getType());
        SearchByType searchByType = resolveSearchBy(request.getSearchBy());
        return PostSearchCondition.of(hashtag, snsType, searchByType, request.getSearch());
    }

    private String resolveHashtag(String hashtag, String defaultHashtag) {
        if (hashtag == null) {
            return defaultHashtag;
        }
        return hashtag;
    }

    private SnsType resolveSnsType(String type) {
        if (!hasText(type)) {
            return null;
        }

        for (SnsType snsType : SnsType.values()) {
            if (type.toUpperCase().equals(snsType.name())) {
                return snsType;
            }
        }

        throw new CustomException(ErrorCode.NOT_FOUND_SNS_TYPE);
    }

    private SearchByType resolveSearchBy(String searchBy) {
        if (searchBy == null) {
            return SearchByType.TITLE_CONTENT;
        }

        for (SearchByType searchByType : SearchByType.values()) {
            if (searchBy.toLowerCase().equals(searchByType.getValue())) {
                return searchByType;
            }
        }

        throw new CustomException(ErrorCode.NOT_FOUND_SEARCH_BY_TYPE);
    }
}
