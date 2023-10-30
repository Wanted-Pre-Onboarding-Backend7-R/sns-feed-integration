package com.wanted.teamr.snsfeedintegration.controller;

import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.domain.PostHashtag;
import com.wanted.teamr.snsfeedintegration.domain.SnsType;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.PostRepository;
import com.wanted.teamr.snsfeedintegration.security.WithAuthUser;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @DisplayName("게시물 좋아요에 성공하면 200 OK로 응답한다.")
    @WithAuthUser
    @Test
    void likePost() throws Exception {
        // given
        Post post = Post.builder()
                        .contentId("5668")
                        .type(SnsType.INSTAGRAM)
                        .title("우리집 고양이")
                        .content("우리집 고양이 보고가세요")
                        .viewCount(21600L)
                        .likeCount(7775L)
                        .shareCount(555L)
                        .createdAt(LocalDateTime.of(2021, 8, 10, 8, 5, 22))
                        .updatedAt(LocalDateTime.of(2021, 8, 17, 17, 35, 42))
                        .build();
        PostHashtag.builder()
                   .post(post)
                   .hashtag("고양이")
                   .build();
        PostHashtag.builder()
                   .post(post)
                   .hashtag("냥스타그램")
                   .build();
        postRepository.save(post);
        Long postId = post.getId();

        // when, then
        mockMvc.perform(post("/api/posts/{postId}/like", postId))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @DisplayName("게시물 좋아요 요청할 때 게시물 id에 해당하는 게시물을 찾을 수 없어 예외가 발생한다.")
    @WithAuthUser
    @Test
    void likePostFailedPostNotFound() throws Exception {
        // given
        Long postId = 808080L;

        // when, then
        mockMvc.perform(post("/api/posts/{postId}/like", postId))
               .andDo(print())
               .andExpect(status().isNotFound())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
               .andExpect(jsonPath("$.message").value(ErrorCode.POST_NOT_FOUND.getMessage()));
    }

    @DisplayName("게시물 공유에 성공하면 200 OK로 응답한다.")
    @WithAuthUser
    @Test
    void sharePost() throws Exception {
        // given
        Post post = Post.builder()
                        .contentId("1234567")
                        .type(SnsType.INSTAGRAM)
                        .title("강아지 졸귀")
                        .content("우리집 강아지 보고가세요")
                        .viewCount(225672L)
                        .likeCount(45333L)
                        .shareCount(10235L)
                        .createdAt(LocalDateTime.of(2023, 8, 10, 8, 5, 22))
                        .updatedAt(LocalDateTime.of(2023, 8, 13, 17, 35, 42))
                        .build();
        PostHashtag.builder()
                   .post(post)
                   .hashtag("강아지")
                   .build();
        PostHashtag.builder()
                   .post(post)
                   .hashtag("댕스타그램")
                   .build();
        postRepository.save(post);
        Long postId = post.getId();

        // when, then
        mockMvc.perform(post("/api/posts/{postId}/share", postId))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @DisplayName("게시물을 공유할 때 게시물 id에 해당하는 게시물을 찾을 수 없어 예외가 발생한다.")
    @WithAuthUser
    @Test
    void sharePostFailedPostNotFound() throws Exception {
        // given
        Long postId = 2500423423410L;

        // when, then
        mockMvc.perform(post("/api/posts/{postId}/share", postId))
               .andDo(print())
               .andExpect(status().isNotFound())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
               .andExpect(jsonPath("$.message").value(ErrorCode.POST_NOT_FOUND.getMessage()));
    }

}
