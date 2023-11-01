package com.wanted.teamr.snsfeedintegration.controller;

import com.wanted.teamr.snsfeedintegration.domain.Member;
import com.wanted.teamr.snsfeedintegration.dto.PostGetResponse;
import com.wanted.teamr.snsfeedintegration.security.CurrentMember;
import com.wanted.teamr.snsfeedintegration.dto.PostSearchRequest;
import com.wanted.teamr.snsfeedintegration.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping("/api/posts/{postId}")
    public PostGetResponse getPost(@PathVariable("postId") Long postId) {
        return postService.getPost(postId);
    }

    @GetMapping("/api/posts")
    public List<PostGetResponse> getPostList(PostSearchRequest request, Pageable pageable,
                                             @CurrentMember Member member) {
        return postService.getPostList(request, pageable, member);
    }

    @PostMapping("/api/posts/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable("postId") Long postId, @CurrentMember Member member) {
        postService.likePost(postId, member);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/posts/{postId}/share")
    public ResponseEntity<?> sharePost(@PathVariable("postId") Long postId, @CurrentMember Member member) {
        postService.sharePost(postId, member);
        return ResponseEntity.ok().build();
    }

}
