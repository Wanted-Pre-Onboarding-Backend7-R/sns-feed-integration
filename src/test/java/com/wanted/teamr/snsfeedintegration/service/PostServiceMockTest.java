package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.domain.PostHashtag;
import com.wanted.teamr.snsfeedintegration.domain.SnsType;
import com.wanted.teamr.snsfeedintegration.dto.PostGetResponse;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceMockTest {

    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;

    @DisplayName("게시물 한 건에 대한 상세 정보를 가져온다.")
    @Test
    void getPost() {
        // given
        Long postId = 123L;
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
        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when
        PostGetResponse postGetResponse = postService.getPost(postId);

        // then
        verify(postRepository).findById(postId);
        assertThat(postGetResponse).isNotNull()
                .extracting(
                        "contentId", "type", "title", "content",
                        "hashtags", "viewCount", "likeCount", "shareCount",
                        "createdAt", "updatedAt"
                )
                .containsExactly(
                        "12345", "FACEBOOK", "맛집 탐방 1", "여기 진짜 맛집인정!",
                        List.of("맛집", "Dani"), 100L, 30L, 10L,
                        LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                        LocalDateTime.of(2023, 10, 10, 10, 10, 20)
                );
    }

    @DisplayName("게시물 id로 게시물을 찾을 수 없을 때 예외를 발생시킨다.")
    @Test
    void getPostNotFound() {
        // given
        Long postId = 123L;
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> postService.getPost(postId))
                .isInstanceOf(CustomException.class)
                .extracting("errorCodeType")
                .isEqualTo(ErrorCode.POST_NOT_FOUND);
    }

}
