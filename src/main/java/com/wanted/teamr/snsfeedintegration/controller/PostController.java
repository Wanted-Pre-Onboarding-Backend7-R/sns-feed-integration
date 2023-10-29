package com.wanted.teamr.snsfeedintegration.controller;

import com.wanted.teamr.snsfeedintegration.dto.PostGetResponse;
import com.wanted.teamr.snsfeedintegration.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping("/api/posts/{postId}")
    public PostGetResponse getPost(@PathVariable("postId") Long postId) {
        return postService.getPost(postId);
    }

    @PostMapping("/api/posts/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable("postId") Long postId) {
        postService.likePost(postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/posts/{postId}/share")
    public ResponseEntity<?> sharePost(@PathVariable("postId") Long postId) {
        postService.sharePost(postId);
        return ResponseEntity.ok().build();
    }

}
