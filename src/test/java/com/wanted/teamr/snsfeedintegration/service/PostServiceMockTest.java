package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.config.ExternalApiConfig;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceMockTest {

    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;

    @Mock
    SnsService snsService;

    @Mock
    ExternalApiConfig externalApiConfig;

    @DisplayName("게시물 한 건에 대한 상세 정보를 가져오며 조회수를 1 늘린다.")
    @Test
    void getPost() {
        // given
        long viewCount = 100L;
        Long postId = 123L;
        Post post = Post.builder()
                .contentId("12345")
                .type(SnsType.FACEBOOK)
                .title("맛집 탐방 1")
                .content("여기 진짜 맛집인정!")
                .viewCount(viewCount)
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
        given(postRepository.findByIdForUpdate(postId)).willReturn(Optional.of(post));

        // when
        PostGetResponse postGetResponse = postService.getPost(postId);

        // then
        verify(postRepository).findByIdForUpdate(postId);
        assertThat(postGetResponse).isNotNull()
                .extracting(
                        "contentId", "type", "title", "content",
                        "hashtags", "viewCount", "likeCount", "shareCount",
                        "createdAt", "updatedAt"
                )
                .containsExactly(
                        "12345", "FACEBOOK", "맛집 탐방 1", "여기 진짜 맛집인정!",
                        List.of("맛집", "Dani"), viewCount + 1, 30L, 10L,
                        LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                        LocalDateTime.of(2023, 10, 10, 10, 10, 20)
                );
    }

    @DisplayName("게시물 id로 게시물을 찾을 수 없을 때 예외를 발생시킨다.")
    @Test
    void getPostNotFound() {
        // given
        Long postId = 123L;
        given(postRepository.findByIdForUpdate(postId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> postService.getPost(postId))
                .isInstanceOf(CustomException.class)
                .extracting("errorCodeType")
                .isEqualTo(ErrorCode.POST_NOT_FOUND);
    }

    @DisplayName("게시물 공유 성공")
    @Test
    void sharePost() {
        // given: 게시물이 존재하도록 설정
        Long postId = 12321L;
        Long shareCount = 10235L;
        Post post = Post.builder()
                        .contentId("1234567")
                        .type(SnsType.INSTAGRAM)
                        .title("강아지 졸귀")
                        .content("우리집 강아지 보고가세요")
                        .viewCount(225672L)
                        .likeCount(45333L)
                        .shareCount(shareCount)
                        .createdAt(LocalDateTime.of(2023, 8, 10, 8, 5, 22))
                        .updatedAt(LocalDateTime.of(2023, 8, 13, 17, 35, 42))
                        .build();
        given(postRepository.findByIdForUpdate(postId)).willReturn(Optional.of(post));
        given(snsService.sharePost(post.getContentId(), post.getType())).willReturn(true);

        // when
        postService.sharePost(postId);

        // then: 공유 수가 +1 됐는지 확인
        assertAll(
                () -> verify(postRepository).findByIdForUpdate(postId),
                () -> verify(snsService).sharePost(post.getContentId(), post.getType()),
                () -> assertThat(post.getShareCount()).isEqualTo(shareCount + 1)
        );
    }

    @DisplayName("게시물을 공유할 때 게시물 id에 해당하는 게시물을 찾을 수 없어 예외가 발생한다.")
    @Test
    void sharePostFailedPostNotFound() {
        // given: postId에 해당하는 게시물이 존재하지 않도록 설정
        Long postId = 222500L;
        given(postRepository.findByIdForUpdate(postId)).willReturn(Optional.empty());

        // when
        CustomException ex = assertThrows(CustomException.class, () -> postService.sharePost(postId));

        // then: POST_NOT_FOUND 예외 발생 확인
        assertAll(
                () -> assertThat(ex.getErrorCodeType()).isEqualTo(ErrorCode.POST_NOT_FOUND),
                () -> verify(postRepository).findByIdForUpdate(postId),
                () -> verify(snsService, never()).sharePost(any(String.class), any(SnsType.class))
        );
    }

}
