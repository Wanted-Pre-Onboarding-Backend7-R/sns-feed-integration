package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final SnsService snsService;

    @Transactional
    public void likePost(Long postId) {
        Post post = postRepository.findByIdForUpdate(postId)
                                  .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        snsService.likePost(post.getContent(), post.getType());

        post.increaseLikeCount();
        postRepository.save(post);
    }
}
