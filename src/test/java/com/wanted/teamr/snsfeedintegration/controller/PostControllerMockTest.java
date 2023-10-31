package com.wanted.teamr.snsfeedintegration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamr.snsfeedintegration.domain.Member;
import com.wanted.teamr.snsfeedintegration.dto.PostGetResponse;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.fixture.MemberFixture;
import com.wanted.teamr.snsfeedintegration.security.WithAuthUser;
import com.wanted.teamr.snsfeedintegration.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
class PostControllerMockTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    private Member member;

    @BeforeEach
    void beforeEach() {
        member = MemberFixture.MEMBER1();
    }

    @DisplayName("게시물 상세 정보 응답을 보낸다.")
    @WithMockUser
    @Test
    void getPost() throws Exception {
        // given
        Long postId = 123L;
        PostGetResponse postGetResponse = PostGetResponse.of(
                postId, "12345", "FACEBOOK", "맛집 탐방 1", "여기 진짜 맛집인정!",
                List.of("맛집", "Dani"), 100L, 30L, 10L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 20)
        );
        when(postService.getPost(postId)).thenReturn(postGetResponse);

        // when, then
        mockMvc.perform(get("/api/posts/{postId}", postId))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.postId").value(postId))
               .andExpect(jsonPath("$.contentId").value("12345"))
               .andExpect(jsonPath("$.type").value("FACEBOOK"))
               .andExpect(jsonPath("$.title").value("맛집 탐방 1"))
               .andExpect(jsonPath("$.content").value("여기 진짜 맛집인정!"))
               .andExpect(jsonPath("$.hashtags").isArray())
               .andExpect(jsonPath("$.hashtags[0]").value("맛집"))
               .andExpect(jsonPath("$.hashtags[1]").value("Dani"))
               .andExpect(jsonPath("$.viewCount").value(100))
               .andExpect(jsonPath("$.likeCount").value(30))
               .andExpect(jsonPath("$.shareCount").value(10))
               .andExpect(jsonPath("$.createdAt").value("2023-10-10T10:10:10"))
               .andExpect(jsonPath("$.updatedAt").value("2023-10-10T10:10:20"));
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

    @DisplayName("게시물 좋아요에 성공하면 200 OK로 응답한다.")
    @WithAuthUser
    @Test
    void likePost() throws Exception {
        // given
        Long postId = 777L;

        // when
        ResultActions result = mockMvc.perform(post("/api/posts/{postId}/like", postId).with(csrf()))
                                      .andDo(print());
        // then
        verify(postService).likePost(postId, member);
        result.andExpect(status().isOk());
    }

    @DisplayName("게시물 좋아요 요청할 때 게시물 id에 해당하는 게시물을 찾을 수 없어 예외가 발생한다.")
    @WithAuthUser
    @Test
    void likePostFailedPostNotFound() throws Exception {
        // given
        Long postId = 5000L;
        doThrow(new CustomException(ErrorCode.POST_NOT_FOUND)).when(postService)
                                                              .likePost(postId, member);

        // when
        ResultActions result = mockMvc.perform(post("/api/posts/{postId}/like", postId).with(csrf()))
                                      .andDo(print());

        // then
        result.andExpect(status().isNotFound())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
              .andExpect(jsonPath("$.message").value(ErrorCode.POST_NOT_FOUND.getMessage()));
    }

    @DisplayName("게시물 공유에 성공하면 200 OK로 응답한다.")
    @WithAuthUser
    @Test
    void sharePost() throws Exception {
        // given
        Long postId = 12300L;

        // when
        ResultActions result = mockMvc.perform(post("/api/posts/{postId}/share", postId).with(csrf()))
                                      .andDo(print());
        // then
        verify(postService).sharePost(postId, member);
        result.andExpect(status().isOk());
    }

    @DisplayName("게시물을 공유할 때 게시물 id에 해당하는 게시물을 찾을 수 없어 예외가 발생한다.")
    @WithAuthUser
    @Test
    void sharePostFailedPostNotFound() throws Exception {
        // given
        Long postId = 1232252L;
        doThrow(new CustomException(ErrorCode.POST_NOT_FOUND)).when(postService)
                                                              .sharePost(postId, member);

        // when
        ResultActions result = mockMvc.perform(post("/api/posts/{postId}/share", postId).with(csrf()))
                                      .andDo(print());

        // then
        result.andExpect(status().isNotFound())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
              .andExpect(jsonPath("$.message").value(ErrorCode.POST_NOT_FOUND.getMessage()));
    }

}
