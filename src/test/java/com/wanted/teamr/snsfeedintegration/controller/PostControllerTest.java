package com.wanted.teamr.snsfeedintegration.controller;

import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.domain.PostHashtag;
import com.wanted.teamr.snsfeedintegration.domain.SnsType;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @DisplayName("게시물 상세 정보 응답을 보낸다.")
    @WithMockUser
    @Test
    void getPost() throws Exception {
        // given
        Post post = Post.builder()
                .contentId("12345")
                .type(SnsType.FACEBOOK)
                .title("맛집 탐방 1")
                .content("여기 진짜 맛집인정!")
                .viewCount(100L)
                .likeCount(30L)
                .shareCount(10L)
                .createdAt(LocalDateTime.of(2023, 10, 10, 10, 10, 10))
                .updatedAt(LocalDateTime.of(2023, 10, 10, 10, 10, 20))
                .build();
        PostHashtag.builder()
                .post(post)
                .hashtag("맛집")
                .build();
        PostHashtag.builder()
                .post(post)
                .hashtag("Dani")
                .build();
        postRepository.save(post);
        Long postId = post.getId();

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
        List<Post> all = postRepository.findAll();
        Assertions.assertThat(all).hasSize(0);

        // when, then
        mockMvc.perform(get("/api/posts/{postId}", 1L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.POST_NOT_FOUND.getMessage()));
    }

}
