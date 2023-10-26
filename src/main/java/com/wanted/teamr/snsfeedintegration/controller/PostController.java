package com.wanted.teamr.snsfeedintegration.controller;

import com.wanted.teamr.snsfeedintegration.dto.GetPostResponse;
import com.wanted.teamr.snsfeedintegration.dto.PostDto;
import com.wanted.teamr.snsfeedintegration.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping("/api/posts/{postId}")
    public GetPostResponse getPost(@PathVariable("postId") Long postId) {
        PostDto postDto = postService.getPost(postId);
        return new GetPostResponse(postDto);
    }

}
