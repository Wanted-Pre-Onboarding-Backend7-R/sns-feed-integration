package com.wanted.teamr.snsfeedintegration.integration;

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
public class PostApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @DisplayName("게시물 상세 정보 응답을 보낸다.")
    @WithMockUser
    @Test
    void getPost() throws Exception {
        // given
        Post post = new Post(
                "12345", SnsType.FACEBOOK, "맛집 탐방 1", "여기 진짜 맛집인정!",
                100L, 30L, 10L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 20)
        );
        new PostHashtag(post, "맛집");
        new PostHashtag(post, "Dani");
        postRepository.save(post);
        Long postId = post.getId();

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
