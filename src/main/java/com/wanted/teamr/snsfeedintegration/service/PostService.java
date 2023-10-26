package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final SnsService snsService;

    public PostService(final PostRepository postRepository, final SnsService snsService) {
        this.postRepository = postRepository;
        this.snsService = snsService;
    }

    @Transactional
    public void likePost(Long postId) {
        Post post = postRepository.findByIdForUpdate(postId)
                                  .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        snsService.likePost(post.getContent(), post.getType());

        post.increaseLikeCount();
        postRepository.save(post);
    }
}
