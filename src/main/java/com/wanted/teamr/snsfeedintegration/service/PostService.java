package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.dto.GetPostResponse;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public GetPostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return GetPostResponse.of(post);
    }

}
