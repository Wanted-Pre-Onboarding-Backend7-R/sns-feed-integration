package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.domain.Member;
import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.dto.PostGetResponse;
import com.wanted.teamr.snsfeedintegration.dto.PostSearchCondition;
import com.wanted.teamr.snsfeedintegration.dto.PostSearchRequest;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.PostRepository;
import com.wanted.teamr.snsfeedintegration.util.PostSearchConditionConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final SnsService snsService;
    private final PostSearchConditionConverter converter;

    @Transactional
    public PostGetResponse getPost(Long postId) {
        Post post = postRepository.findByIdForUpdate(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.increaseViewCount();
        return PostGetResponse.of(post);
    }

    public List<PostGetResponse> getPostList(PostSearchRequest request, Pageable pageable) {
        PostSearchCondition condition = converter.convert(request);
        List<Post> posts = postRepository.search(condition, pageable);
        return posts.stream().map(PostGetResponse::of).toList();
    }

    @Transactional
    public void likePost(Long postId, Member member) {
        Post post = postRepository.findByIdForUpdate(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        snsService.likePost(post.getContentId(), post.getType(), member);

        post.increaseLikeCount();
    }

    @Transactional
    public void sharePost(Long postId, Member member) {
        Post post = postRepository.findByIdForUpdate(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        snsService.sharePost(post.getContentId(), post.getType(), member);

        post.increaseShareCount();
    }

}
