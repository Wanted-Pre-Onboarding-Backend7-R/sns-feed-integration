package com.wanted.teamr.snsfeedintegration.repository;

import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.domain.PostHashtag;
import com.wanted.teamr.snsfeedintegration.domain.SearchByType;
import com.wanted.teamr.snsfeedintegration.domain.SnsType;
import com.wanted.teamr.snsfeedintegration.dto.PostSearchCondition;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;


@DataJpaTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    EntityManager em;


    // TODO: hashtag이 없는 경우는 없다. 컨트롤러가 hashtag 없는 요청을 받았을 때 서비스에서 본인 계정 hashtag 넣어주도록 해야 한다.
    @DisplayName("정확히 일치하는 hashtag의 게시물만 검색해야 합니다.")
    @Test
    void givenPostWithExactHashtag_thenReturnPosts() {
        // post, hashtag 준비
        createPostAndHashtags(
                "12345", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "qwerty", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "asdfg", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );

        // 검색 조건 설정
        PostSearchCondition searchCondition = PostSearchCondition.builder()
                .hashtag("초콜릿")
                .type(SnsType.FACEBOOK)
                .searchBy(SearchByType.content)
                .search("산책로")
                .build();
        PageRequest pageRequest = PageRequest.of(0, 10,
                Sort.by(Sort.Direction.ASC, "createdAt"));

        // when
        List<Post> posts = postRepository.search(searchCondition, pageRequest);

        // then
        Assertions.assertThat(posts).hasSize(3)
                .extracting("contentId").containsExactly("12345", "qwerty", "asdfg");
    }


    @DisplayName("정확히 일치하는 hashtag의 게시물이 없을 경우 빈 리스트를 반환해야 합니다.")
    @Test
    void givenNoPostWithExactHashtag_thenReturnEmptyList() {
        // post, hashtag 준비
        createPostAndHashtags(
                "12345", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "qwerty", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "asdfg", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );

        // 어떤 게시물과도 일치하지 않는 해시태그 검색 조건 설정
        PostSearchCondition searchCondition = PostSearchCondition.builder()
                .hashtag("카메라")
                .type(SnsType.FACEBOOK)
                .searchBy(SearchByType.content)
                .search("산책로")
                .build();
        PageRequest pageRequest = PageRequest.of(0, 10,
                Sort.by(Sort.Direction.ASC, "createdAt"));

        // when
        List<Post> posts = postRepository.search(searchCondition, pageRequest);

        // then
        Assertions.assertThat(posts).hasSize(0);
    }

    @DisplayName("일치하는 소셜 미디어 타입 게시물을 검색합니다.")
    @Test
    void givenSocialMediaType() {
        // post, hashtag 준비
        createPostAndHashtags(
                "12345", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "qwerty", SnsType.INSTAGRAM,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "asdfg", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );

        // 검색 조건 설정
        PostSearchCondition searchCondition = PostSearchCondition.builder()
                .hashtag("초콜릿")
                .type(SnsType.FACEBOOK)
                .searchBy(SearchByType.content)
                .search("산책로")
                .build();
        PageRequest pageRequest = PageRequest.of(0, 10,
                Sort.by(Sort.Direction.ASC, "createdAt"));

        // when
        List<Post> posts = postRepository.search(searchCondition, pageRequest);

        // then
        Assertions.assertThat(posts).hasSize(2)
                .extracting("contentId").containsExactly("12345", "asdfg");
    }

    @DisplayName("소셜 미디어 타입을 정하지 않으면 모든 타입이 조회됩니다.")
    @Test
    void givenNoSocialMediaType_thenSearchEveryTypes() {
        // post, hashtag 준비
        createPostAndHashtags(
                "12345", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "qwerty", SnsType.INSTAGRAM,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "asdfg", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );

        // 검색 조건 설정 - 소셜 미디어 타입 null
        PostSearchCondition searchCondition = PostSearchCondition.builder()
                .hashtag("초콜릿")
                .type(null)
                .searchBy(SearchByType.content)
                .search("산책로")
                .build();
        PageRequest pageRequest = PageRequest.of(0, 10,
                Sort.by(Sort.Direction.ASC, "createdAt"));

        // when
        List<Post> posts = postRepository.search(searchCondition, pageRequest);

        // then
        Assertions.assertThat(posts).hasSize(3)
                .extracting("contentId").containsExactly("12345", "qwerty", "asdfg");
    }

    @DisplayName("다양한 정렬 기준(createdAt, updatedAt, likeCount, shareCount, viewCount)에 맞춰 게시물이 정렬됩니다.")
    @MethodSource("getOrderByArgumentsProvider")
    @ParameterizedTest
    void givenOrderBy_ThenOrdered(String orderProperty, Sort.Direction orderDirection, List<String> expectedContentIds) {
        // post, hashtag 준비 - post의 viewCount, likeCount, shareCount, createdAt, updatedAt 만 서로 다르게 준비
        createPostAndHashtags(
                "12345", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                100L, 30L, 10L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "qwerty", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "asdfg", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                102L, 32L, 12L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                List.of("도서관", "초콜릿")
        );

        // 검색 조건 설정
        PostSearchCondition searchCondition = PostSearchCondition.builder()
                .hashtag("초콜릿")
                .type(SnsType.FACEBOOK)
                .searchBy(SearchByType.content)
                .search("산책로")
                .build();
        PageRequest pageRequest = PageRequest.of(0, 10,
                Sort.by(orderDirection, orderProperty));

        // when
        List<Post> posts = postRepository.search(searchCondition, pageRequest);

        // then
        Assertions.assertThat(posts).hasSize(3)
                .extracting("contentId").containsExactly(expectedContentIds.toArray());
    }

    static Stream<Arguments> getOrderByArgumentsProvider() {
        return Stream.of(
                Arguments.of("createdAt", Sort.Direction.ASC, List.of("12345", "qwerty", "asdfg")),
                Arguments.of("createdAt", Sort.Direction.DESC, List.of("asdfg", "qwerty", "12345")),
                Arguments.of("updatedAt", Sort.Direction.ASC, List.of("12345", "qwerty", "asdfg")),
                Arguments.of("updatedAt", Sort.Direction.DESC, List.of("asdfg", "qwerty", "12345")),
                Arguments.of("likeCount", Sort.Direction.ASC, List.of("12345", "qwerty", "asdfg")),
                Arguments.of("likeCount", Sort.Direction.DESC, List.of("asdfg", "qwerty", "12345")),
                Arguments.of("shareCount", Sort.Direction.ASC, List.of("12345", "qwerty", "asdfg")),
                Arguments.of("shareCount", Sort.Direction.DESC, List.of("asdfg", "qwerty", "12345")),
                Arguments.of("viewCount", Sort.Direction.ASC, List.of("12345", "qwerty", "asdfg")),
                Arguments.of("viewCount", Sort.Direction.DESC, List.of("asdfg", "qwerty", "12345"))
        );
    }

    @DisplayName("다양한 검색 기준(title, content, title,content)에 맞게 게시물을 검색합니다.")
    @MethodSource("givenSearchByArgumentsProvider")
    @ParameterizedTest
    void givenSearchBy(SearchByType searchByType, String search, int expectedSize, List<String> expectedContentIds) {
        // post, hashtag 준비 - title과 content만 다르게 준비
        createPostAndHashtags(
                "12345", SnsType.FACEBOOK,
                "강아지 해변", "책상 노트북 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "qwerty", SnsType.FACEBOOK,
                "공원 노트북", "휴가 선물 모자",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "asdfg", SnsType.FACEBOOK,
                "해변 신발", "고양이 자전거 도서관",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );

        // 검색 조건 설정
        PostSearchCondition searchCondition = PostSearchCondition.builder()
                .hashtag("초콜릿")
                .type(SnsType.FACEBOOK)
                .searchBy(searchByType)
                .search(search)
                .build();
        PageRequest pageRequest = PageRequest.of(0, 10,
                Sort.by(Sort.Direction.ASC, "createdAt"));

        // when
        List<Post> posts = postRepository.search(searchCondition, pageRequest);

        // then
        Assertions.assertThat(posts).hasSize(expectedSize)
                .extracting("contentId").containsExactly(expectedContentIds.toArray());
    }

    static Stream<Arguments> givenSearchByArgumentsProvider() {
        return Stream.of(
                // SearchByType searchByType, String search, int expectedSize, List<String> expectedContentIds
                Arguments.of(SearchByType.title, "해변", 2, List.of("12345", "asdfg")),
                Arguments.of(SearchByType.content, "선물", 1, List.of("qwerty")),
                Arguments.of(SearchByType.titleContent, "노트북", 2, List.of("12345", "qwerty"))
        );
    }

    @DisplayName("페이지 당 게시물 갯수와 조회하려는 페이지를 지정할 수 있습니다.")
    @MethodSource("givenPageCountAndPageArgumentsProvider")
    @ParameterizedTest
    void givenPageCountAndPage(int pageNumber, int pageSize, int expectedSize, List<String> expectedContentIds) {
        // post, hashtag 준비 - createdAt 제외 모든 값 도일
        createPostAndHashtags(
                "12345", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "qwerty", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "asdfg", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );
        createPostAndHashtags(
                "jkl", SnsType.FACEBOOK,
                "강아지 해변", "책상 비행기 산책로",
                101L, 31L, 11L,
                LocalDateTime.of(2023, 10, 10, 10, 10, 13),
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                List.of("도서관", "초콜릿")
        );

        // 검색 조건 설정 - 모든 게시물 선택
        PostSearchCondition searchCondition = PostSearchCondition.builder()
                .hashtag("초콜릿")
                .type(SnsType.FACEBOOK)
                .searchBy(SearchByType.content)
                .search("산책로")
                .build();

        // 페이지 설정 - argument 대입
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize,
                Sort.by(Sort.Direction.ASC, "createdAt"));

        // when
        List<Post> posts = postRepository.search(searchCondition, pageRequest);

        // then
        Assertions.assertThat(posts).hasSize(expectedSize)
                .extracting("contentId").containsExactly(expectedContentIds.toArray());
    }

    static Stream<Arguments> givenPageCountAndPageArgumentsProvider() {
        return Stream.of(
                // int pageNumber, int pageSize, int expectedSize, List<String> expectedContentIds
                Arguments.of(0, 4, 4, List.of("12345", "qwerty", "asdfg", "jkl")),
                Arguments.of(0, 5, 4, List.of("12345", "qwerty", "asdfg", "jkl")),
                Arguments.of(0, 2, 2, List.of("12345", "qwerty")),
                Arguments.of(1, 2, 2, List.of("asdfg", "jkl")),
                Arguments.of(1, 1, 1, List.of("qwerty")),
                Arguments.of(5, 1, 0, List.of())
        );
    }

    private Post createPostAndHashtags(
            String contentId, SnsType snsType,
            String title, String content,
            long viewCount, long likeCount, long shareCount,
            LocalDateTime createdAt, LocalDateTime updatedAt,
            List<String> hashtags
    ) {
        Post post = Post.builder()
                .contentId(contentId)
                .type(snsType)
                .title(title)
                .content(content)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .shareCount(shareCount)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        for (String hashtag : hashtags) {
            PostHashtag.builder()
                    .post(post)
                    .hashtag(hashtag)
                    .build();
        }
        return postRepository.save(post);
    }

}
