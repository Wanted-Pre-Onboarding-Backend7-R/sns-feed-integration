package com.wanted.teamr.snsfeedintegration.dto;

import com.wanted.teamr.snsfeedintegration.domain.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetPostResponse {

    private final PostDto postDto;

    public static GetPostResponse of(Post post) {
        return new GetPostResponse(PostDto.of(post));
    }

}
