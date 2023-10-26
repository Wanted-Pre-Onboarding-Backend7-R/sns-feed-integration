package com.wanted.teamr.snsfeedintegration.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamr.snsfeedintegration.controller.PostController;
import com.wanted.teamr.snsfeedintegration.dto.PostDto;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostController.class)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    @DisplayName("게시물 상세 정보 응답을 보낸다.")
    @WithMockUser
    @Test
    void getPost() throws Exception {
        // given
        Long postId = 123L;
        PostDto postDto = new PostDto(
                postId, "12345", "FACEBOOK", "맛집 탐방 1", "여기 진짜 맛집인정!",
                List.of("맛집", "Dani"), 100L, 30L, 10L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 20)
        );
        when(postService.getPost(postId)).thenReturn(postDto);

        // when, then
        mockMvc.perform(get("/api/posts/{postId}", postId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.post.postId").value(postId))
                .andExpect(jsonPath("$.post.contentId").value("12345"))
                .andExpect(jsonPath("$.post.type").value("FACEBOOK"))
                .andExpect(jsonPath("$.post.title").value("맛집 탐방 1"))
                .andExpect(jsonPath("$.post.content").value("여기 진짜 맛집인정!"))
                .andExpect(jsonPath("$.post.hashtags").isArray())
                .andExpect(jsonPath("$.post.hashtags[0]").value("맛집"))
                .andExpect(jsonPath("$.post.hashtags[1]").value("Dani"))
                .andExpect(jsonPath("$.post.viewCount").value(100))
                .andExpect(jsonPath("$.post.likeCount").value(30))
                .andExpect(jsonPath("$.post.shareCount").value(10))
                .andExpect(jsonPath("$.post.createdAt").value("2023-10-10T10:10:10"))
                .andExpect(jsonPath("$.post.updatedAt").value("2023-10-10T10:10:20"));
    }

    @DisplayName("게시물을 찾을 수 없을 때 에러 응답을 보낸다.")
    @WithMockUser
    @Test
    void getPostNotFound() throws Exception {
        // given
        Long postId = 123L;
        when(postService.getPost(postId)).thenThrow(new CustomException(ErrorCode.POST_NOT_FOUND));

        // when, then
        mockMvc.perform(get("/api/posts/{postId}", postId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.POST_NOT_FOUND.getMessage()));
    }

}
