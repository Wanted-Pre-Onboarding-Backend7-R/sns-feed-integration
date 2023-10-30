package com.wanted.teamr.snsfeedintegration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.domain.PostHashtag;
import com.wanted.teamr.snsfeedintegration.domain.SnsType;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Posts API 통합 테스트")
@AutoConfigureMockMvc
@SpringBootTest
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        postRepository.deleteAll();
    }

    @DisplayName("GET, /api/posts/{postId} 게시물 상세 정보 API")
    @Nested
    class getPost {

        @DisplayName("게시물 상세 정보 응답을 보낸다.")
        @WithMockUser
        @Test
        void getPostFound() throws Exception {
            // given
            Post post = createPostAndHashtags(
                    "12345", SnsType.FACEBOOK,
                    "강아지 해변", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 20),
                    List.of("초콜릿", "하늘")
            );
            Long postId = post.getId();

            // when, then
            mockMvc.perform(get("/api/posts/{postId}", postId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.postId").value(postId))
                    .andExpect(jsonPath("$.contentId").value("12345"))
                    .andExpect(jsonPath("$.type").value("facebook"))
                    .andExpect(jsonPath("$.title").value("강아지 해변"))
                    .andExpect(jsonPath("$.content").value("책상 비행기 산책로"))
                    .andExpect(jsonPath("$.hashtags").isArray())
                    .andExpect(jsonPath("$.hashtags[0]").value("초콜릿"))
                    .andExpect(jsonPath("$.hashtags[1]").value("하늘"))
                    .andExpect(jsonPath("$.viewCount").value(101))
                    .andExpect(jsonPath("$.likeCount").value(31))
                    .andExpect(jsonPath("$.shareCount").value(11))
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

    @DisplayName("GET, /api/posts 게시물 목록 API")
    @Nested
    class getPostList {

        /**
         * 테스트의 간결성, 효율성을 위해 이 외 테스트에서는 모든 필드 대신 contentId만 검증하기로 한다.
         */
        @DisplayName("게시물 리스트가 원한는 모양과 필드로 반환된다.")
        @WithMockUser
        @Test
        void postList() throws Exception {
            // given
            // post, hashtag 준비 - hashtag, contentId, createdAt 제외 모든 것이 동일
            createPostAndHashtags(
                    "12345", SnsType.FACEBOOK,
                    "강아지 해변", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                    List.of("초콜릿")
            );
            createPostAndHashtags(
                    "qwerty", SnsType.FACEBOOK,
                    "강아지 해변", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                    List.of("도서관", "초콜릿")
            );

            // when, then
            mockMvc.perform(get("/api/posts")
                            .queryParam("hashtag", "도서관")
                            .queryParam("type", "facebook")
                            .queryParam("orderBy", "createdAt,asc")
                            .queryParam("searchBy", "content")
                            .queryParam("search", "책상")
                            .queryParam("pageCount", "10")
                            .queryParam("page", "0")
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.[0].contentId").value("qwerty"))
                    .andExpect(jsonPath("$.[0].type").value("facebook"))
                    .andExpect(jsonPath("$.[0].title").value("강아지 해변"))
                    .andExpect(jsonPath("$.[0].content").value("책상 비행기 산책로"))
                    .andExpect(jsonPath("$.[0].hashtags[0]").value("도서관"))
                    .andExpect(jsonPath("$.[0].hashtags[1]").value("초콜릿"))
                    .andExpect(jsonPath("$.[0].viewCount").value(101))
                    .andExpect(jsonPath("$.[0].likeCount").value(31))
                    .andExpect(jsonPath("$.[0].shareCount").value(11))
                    .andExpect(jsonPath("$.[0].createdAt").value("2023-10-10T10:10:11"))
                    .andExpect(jsonPath("$.[0].updatedAt").value("2023-10-10T10:10:10"));
        }

        @DisplayName("해시태그와 정확히 일치하는 게시물 목록을 응답한다.")
        @WithMockUser
        @Test
        void givenHashtag_thenReturnPosts() throws Exception {
            // given
            // post, hashtag 준비 - hashtag, contentId, creatdAt 제외 모든 것이 동일
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
                    List.of("호수", "카메라")
            );
            createPostAndHashtags(
                    "zxcvb", SnsType.FACEBOOK,
                    "강아지 해변", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                    List.of("노래", "호수")
            );

            // when, then
            mockMvc.perform(get("/api/posts")
                            .queryParam("hashtag", "호수")
                            .queryParam("type", "facebook")
                            .queryParam("orderBy", "createdAt,asc")
                            .queryParam("searchBy", "content")
                            .queryParam("search", "책상")
                            .queryParam("pageCount", "10")
                            .queryParam("page", "0")
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.[0].contentId").value("qwerty"))
                    .andExpect(jsonPath("$.[1].contentId").value("zxcvb"));
        }

        @DisplayName("소셜 미디어 타입과 일치하는 게시물 리스트를 응답한다.")
        @WithMockUser
        @Test
        void givenSnsType_thenReturnPosts() throws Exception {
            // given
            // post, hashtag 준비 - type, contentId, creatdAt 제외 모든 것이 동일
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
                    "zxcvb", SnsType.INSTAGRAM,
                    "강아지 해변", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                    List.of("도서관", "초콜릿")
            );

            // when, then
            mockMvc.perform(get("/api/posts")
                            .queryParam("hashtag", "초콜릿")
                            .queryParam("type", "facebook")
                            .queryParam("orderBy", "createdAt,asc")
                            .queryParam("searchBy", "content")
                            .queryParam("search", "책상")
                            .queryParam("pageCount", "10")
                            .queryParam("page", "0")
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.[0].contentId").value("12345"))
                    .andExpect(jsonPath("$.[1].contentId").value("qwerty"));
        }

        @DisplayName("소셜 미디어 타입 값을 미입력 시 모든 type의 게시물이 조회된다.")
        @WithMockUser
        @Test
        void givenNoSnsType_thenReturnAllTypesPosts() throws Exception {
            // given
            // post, hashtag 준비 - type, contentId, creatdAt 제외 모든 것이 동일
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
                    "zxcvb", SnsType.INSTAGRAM,
                    "강아지 해변", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                    List.of("도서관", "초콜릿")
            );

            // when, then
            mockMvc.perform(get("/api/posts")
                            .queryParam("hashtag", "초콜릿")
                            //.queryParam("type", "facebook") // type 파라미터 삭제
                            .queryParam("orderBy", "createdAt,asc")
                            .queryParam("searchBy", "content")
                            .queryParam("search", "책상")
                            .queryParam("pageCount", "10")
                            .queryParam("page", "0")
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.[0].contentId").value("12345"))
                    .andExpect(jsonPath("$.[1].contentId").value("qwerty"))
                    .andExpect(jsonPath("$.[2].contentId").value("zxcvb"));
        }

        @DisplayName("소셜 미디어 타입 값이 blank 라면 모든 type의 게시물이 조회된다.")
        @WithMockUser
        @Test
        void givenBlankSnsType_thenReturnAllTypesPosts() throws Exception {
            // given
            // post, hashtag 준비 - type, contentId, creatdAt 제외 모든 것이 동일
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
                    "zxcvb", SnsType.INSTAGRAM,
                    "강아지 해변", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                    List.of("도서관", "초콜릿")
            );

            // when, then
            String type = "   ";
            mockMvc.perform(get("/api/posts")
                            .queryParam("hashtag", "초콜릿")
                            .queryParam("type", type)
                            .queryParam("orderBy", "createdAt,asc")
                            .queryParam("searchBy", "content")
                            .queryParam("search", "책상")
                            .queryParam("pageCount", "10")
                            .queryParam("page", "0")
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.[0].contentId").value("12345"))
                    .andExpect(jsonPath("$.[1].contentId").value("qwerty"))
                    .andExpect(jsonPath("$.[2].contentId").value("zxcvb"));
        }

        @DisplayName("정렬 기준과 정렬 방향에 맞춰 게시물 리스트가 조회된다. 정렬 방향을 생략한 경우 asc로 취급된다.")
        @WithMockUser
        @MethodSource("givenOrderByArgumentsProvider")
        @ParameterizedTest
        void givenOrderBy_thenReturnPosts(String orderBy, List<String> expectedContentIds) throws Exception {
            // given
            // post, hashtag 준비 - contentId, createdAt, updatedAt, likeCount, shareCount, viewCount 제외 모두 동일
            createPostAndHashtags(
                    "12345", SnsType.FACEBOOK,
                    "강아지 해변", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "qwerty", SnsType.FACEBOOK,
                    "강아지 해변", "책상 비행기 산책로",
                    102L, 32L, 12L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "zxcvb", SnsType.FACEBOOK,
                    "강아지 해변", "책상 비행기 산책로",
                    103L, 33L, 13L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 13),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 13),
                    List.of("도서관", "초콜릿")
            );

            // when, then
            mockMvc.perform(get("/api/posts")
                            .queryParam("hashtag", "초콜릿")
                            .queryParam("type", "facebook")
                            .queryParam("orderBy", orderBy)
                            .queryParam("searchBy", "content")
                            .queryParam("search", "책상")
                            .queryParam("pageCount", "10")
                            .queryParam("page", "0")
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.[0].contentId").value(expectedContentIds.get(0)))
                    .andExpect(jsonPath("$.[1].contentId").value(expectedContentIds.get(1)))
                    .andExpect(jsonPath("$.[2].contentId").value(expectedContentIds.get(2)));
        }

        static Stream<Arguments> givenOrderByArgumentsProvider() {
            return Stream.of(
                    Arguments.of("createdAt,asc", List.of("12345", "qwerty", "zxcvb")),
                    Arguments.of("createdAt,desc", List.of("zxcvb", "qwerty", "12345")),
                    Arguments.of("createdAt,", List.of("12345", "qwerty", "zxcvb")),
                    Arguments.of("createdAt", List.of("12345", "qwerty", "zxcvb")),
                    Arguments.of("updatedAt,asc", List.of("12345", "qwerty", "zxcvb")),
                    Arguments.of("updatedAt,desc", List.of("zxcvb", "qwerty", "12345")),
                    Arguments.of("updatedAt,", List.of("12345", "qwerty", "zxcvb")),
                    Arguments.of("updatedAt", List.of("12345", "qwerty", "zxcvb")),
                    Arguments.of("likeCount,asc", List.of("12345", "qwerty", "zxcvb")),
                    Arguments.of("likeCount,desc", List.of("zxcvb", "qwerty", "12345")),
                    Arguments.of("likeCount,", List.of("12345", "qwerty", "zxcvb")),
                    Arguments.of("likeCount", List.of("12345", "qwerty", "zxcvb")),
                    Arguments.of("shareCount,asc", List.of("12345", "qwerty", "zxcvb")),
                    Arguments.of("shareCount,desc", List.of("zxcvb", "qwerty", "12345")),
                    Arguments.of("shareCount,", List.of("12345", "qwerty", "zxcvb")),
                    Arguments.of("shareCount", List.of("12345", "qwerty", "zxcvb")),
                    Arguments.of("viewCount,asc", List.of("12345", "qwerty", "zxcvb")),
                    Arguments.of("viewCount,desc", List.of("zxcvb", "qwerty", "12345")),
                    Arguments.of("viewCount,", List.of("12345", "qwerty", "zxcvb")),
                    Arguments.of("viewCount", List.of("12345", "qwerty", "zxcvb"))
            );
        }

        @DisplayName("정렬 기준과 정렬 방향에이 제공되지 않으면 createdAt,asc 로 취급된다.")
        @WithMockUser
        @Test
        void givenNullOrderBy_thenCreatedAtAsc() throws Exception {
            // given
            // post, hashtag 준비 - contentId, createdAt, updatedAt, likeCount, shareCount, viewCount 제외 모두 동일
            createPostAndHashtags(
                    "12345", SnsType.FACEBOOK,
                    "강아지 해변", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "qwerty", SnsType.FACEBOOK,
                    "강아지 해변", "책상 비행기 산책로",
                    102L, 32L, 12L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "zxcvb", SnsType.FACEBOOK,
                    "강아지 해변", "책상 비행기 산책로",
                    103L, 33L, 13L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 13),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 13),
                    List.of("도서관", "초콜릿")
            );

            // when, then
            mockMvc.perform(get("/api/posts")
                            .queryParam("hashtag", "초콜릿")
                            .queryParam("type", "facebook")
                            //.queryParam("orderBy", orderBy)
                            .queryParam("searchBy", "content")
                            .queryParam("search", "책상")
                            .queryParam("pageCount", "10")
                            .queryParam("page", "0")
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.[0].contentId").value("12345"))
                    .andExpect(jsonPath("$.[1].contentId").value("qwerty"))
                    .andExpect(jsonPath("$.[2].contentId").value("zxcvb"));
        }

        @DisplayName("검색 영역을 제목(title)로 지정할 수 있다.")
        @WithMockUser
        @Test
        void givenSearchByAsTitle_thenReturnPosts() throws Exception {
            // given
            // post, hashtag 준비 - contentId, createdAt, title 제외 모두 동일
            createPostAndHashtags(
                    "12345", SnsType.FACEBOOK,
                    "고양이 산 도서관", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "qwerty", SnsType.FACEBOOK,
                    "레스토랑 산호초", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "zxcvb", SnsType.FACEBOOK,
                    "호수 수영장 산호초", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 13),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );

            String searchBy = "title";
            String search = "산호초";

            // when, then
            mockMvc.perform(get("/api/posts")
                            .queryParam("hashtag", "초콜릿")
                            .queryParam("type", "facebook")
                            .queryParam("orderBy", "createdAt,asc")
                            .queryParam("searchBy", searchBy)
                            .queryParam("search", search)
                            .queryParam("pageCount", "10")
                            .queryParam("page", "0")
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.[0].contentId").value("qwerty"))
                    .andExpect(jsonPath("$.[1].contentId").value("zxcvb"));
        }

        @DisplayName("검색 영역을 내용(content)로 지정할 수 있다.")
        @WithMockUser
        @Test
        void givenSearchByAsContent_thenReturnPosts() throws Exception {
            // given
            // post, hashtag 준비 - contentId, createdAt, content 제외 모두 동일
            createPostAndHashtags(
                    "12345", SnsType.FACEBOOK,
                    "고양이 산 도서관", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "qwerty", SnsType.FACEBOOK,
                    "고양이 산 도서관", "노래 수영장",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "zxcvb", SnsType.FACEBOOK,
                    "고양이 산 도서관", "스마트폰 사진 비행기",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 13),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );

            String searchBy = "content";
            String search = "비행기";

            // when, then
            mockMvc.perform(get("/api/posts")
                            .queryParam("hashtag", "초콜릿")
                            .queryParam("type", "facebook")
                            .queryParam("orderBy", "createdAt,asc")
                            .queryParam("searchBy", searchBy)
                            .queryParam("search", search)
                            .queryParam("pageCount", "10")
                            .queryParam("page", "0")
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.[0].contentId").value("12345"))
                    .andExpect(jsonPath("$.[1].contentId").value("zxcvb"));
        }

        @DisplayName("검색 영역을 제목,내용(title,content)으로 지정할 수 있다.")
        @WithMockUser
        @Test
        void givenSearchByAsTitleAndContent_thenReturnPosts() throws Exception {
            // given
            // post, hashtag 준비 - contentId, createdAt, content 제외 모두 동일
            createPostAndHashtags(
                    "12345", SnsType.FACEBOOK,
                    "고양이 산 도서관", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "qwerty", SnsType.FACEBOOK,
                    "꿈 하늘", "고양이 냇가 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "zxcvb", SnsType.FACEBOOK,
                    "달 구름 고양이", "헤드폰 스피커 고양이",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 13),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );

            String searchBy = "title,content";
            String search = "고양이";

            // when, then
            mockMvc.perform(get("/api/posts")
                            .queryParam("hashtag", "초콜릿")
                            .queryParam("type", "facebook")
                            .queryParam("orderBy", "createdAt,asc")
                            .queryParam("searchBy", searchBy)
                            .queryParam("search", search)
                            .queryParam("pageCount", "10")
                            .queryParam("page", "0")
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.[0].contentId").value("12345"))
                    .andExpect(jsonPath("$.[1].contentId").value("qwerty"))
                    .andExpect(jsonPath("$.[2].contentId").value("zxcvb"));
        }

        @DisplayName("검색 영역을 지정하지 않는다면 제목, 내용으로 검색한다.")
        @WithMockUser
        @Test
        void givenNullSearchBy_thenAsTitleAndContent() throws Exception {
            // given
            // post, hashtag 준비 - contentId, createdAt, content, title 제외 모두 동일
            createPostAndHashtags(
                    "12345", SnsType.FACEBOOK,
                    "고양이 산 도서관", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "qwerty", SnsType.FACEBOOK,
                    "꿈 하늘", "고양이 냇가 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "zxcvb", SnsType.FACEBOOK,
                    "달 구름 고양이", "헤드폰 스피커 고양이",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 13),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );

            String search = "고양이";

            // when, then
            mockMvc.perform(get("/api/posts")
                            .queryParam("hashtag", "초콜릿")
                            .queryParam("type", "facebook")
                            .queryParam("orderBy", "createdAt,asc")
                            //.queryParam("searchBy", searchBy)
                            .queryParam("search", search)
                            .queryParam("pageCount", "10")
                            .queryParam("page", "0")
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.[0].contentId").value("12345"))
                    .andExpect(jsonPath("$.[1].contentId").value("qwerty"))
                    .andExpect(jsonPath("$.[2].contentId").value("zxcvb"));
        }

        @DisplayName("검색어를 지정하지 않는다면 검색어는 검색 조건에 들어가지 않는다.")
        @WithMockUser
        @Test
        void givenNullSearch_thenNoSearchCriteria() throws Exception {
            // given
            // post, hashtag 준비 - contentId, createdAt 제외 모두 동일
            createPostAndHashtags(
                    "12345", SnsType.FACEBOOK,
                    "고양이 산 도서관", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "qwerty", SnsType.FACEBOOK,
                    "고양이 산 도서관", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "zxcvb", SnsType.FACEBOOK,
                    "고양이 산 도서관", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 13),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );

            // when, then
            mockMvc.perform(get("/api/posts")
                            .queryParam("hashtag", "초콜릿")
                            .queryParam("type", "facebook")
                            .queryParam("orderBy", "createdAt,asc")
                            .queryParam("searchBy", "title")
                            //.queryParam("search", search)
                            .queryParam("pageCount", "10")
                            .queryParam("page", "0")
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.[0].contentId").value("12345"))
                    .andExpect(jsonPath("$.[1].contentId").value("qwerty"))
                    .andExpect(jsonPath("$.[2].contentId").value("zxcvb"));
        }

        @DisplayName("페이지당 게시물 수와 페이지 번호를 지정할 수 있다.")
        @WithMockUser
        @Test
        void givenPageCountAndPage() throws Exception {
            // given
            // post, hashtag 준비 - contentId, createdAt 제외 모두 동일
            createPostAndHashtags(
                    "12345", SnsType.FACEBOOK,
                    "고양이 산 도서관", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "qwerty", SnsType.FACEBOOK,
                    "고양이 산 도서관", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "zxcvb", SnsType.FACEBOOK,
                    "고양이 산 도서관", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 13),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );

            String pageCount = "1";
            String page = "2";

            // when, then
            mockMvc.perform(get("/api/posts")
                            .queryParam("hashtag", "초콜릿")
                            .queryParam("type", "facebook")
                            .queryParam("orderBy", "createdAt,asc")
                            .queryParam("searchBy", "title")
                            .queryParam("search", "도서관")
                            .queryParam("pageCount", pageCount)
                            .queryParam("page", page)
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$.[0].contentId").value("zxcvb"));
        }

        @DisplayName("페이지당 게시물 수와 페이지 번호가 제공되지 않을 경우 각 10, 0의 기본값을 갖는다.")
        @WithMockUser
        @Test
        void givenNullPageCountAndPage() throws Exception {
            // given
            // post, hashtag 준비 - contentId, createdAt 제외 모두 동일
            createPostAndHashtags(
                    "12345", SnsType.FACEBOOK,
                    "고양이 산 도서관", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "qwerty", SnsType.FACEBOOK,
                    "고양이 산 도서관", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 12),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );
            createPostAndHashtags(
                    "zxcvb", SnsType.FACEBOOK,
                    "고양이 산 도서관", "책상 비행기 산책로",
                    101L, 31L, 11L,
                    LocalDateTime.of(2023, 10, 10, 10, 10, 13),
                    LocalDateTime.of(2023, 10, 10, 10, 10, 11),
                    List.of("도서관", "초콜릿")
            );

            // when, then
            mockMvc.perform(get("/api/posts")
                            .queryParam("hashtag", "초콜릿")
                            .queryParam("type", "facebook")
                            .queryParam("orderBy", "createdAt,asc")
                            .queryParam("searchBy", "title")
                            //.queryParam("pageCount", pageCount)
                            //.queryParam("page", page)
                            .queryParam("search", "도서관")
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.[0].contentId").value("12345"))
                    .andExpect(jsonPath("$.[1].contentId").value("qwerty"))
                    .andExpect(jsonPath("$.[2].contentId").value("zxcvb"));
        }

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
