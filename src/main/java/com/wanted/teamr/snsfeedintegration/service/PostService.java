package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.dto.PostGetResponse;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final SnsService snsService;

    @Transactional
    public PostGetResponse getPost(Long postId) {
        Post post = postRepository.findByIdForUpdate(postId)
                                  .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.increaseViewCount();
        return PostGetResponse.of(post);
    }

    @Transactional
    public void likePost(Long postId) {
        Post post = postRepository.findByIdForUpdate(postId)
                                  .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        snsService.likePost(post.getContentId(), post.getType());

        post.increaseLikeCount();
        postRepository.save(post);
    }

    @Transactional
    public void sharePost(Long postId) {
        Post post = postRepository.findByIdForUpdate(postId)
                                  .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        snsService.sharePost(post.getContentId(), post.getType());

        post.increaseShareCount();
    }
}
