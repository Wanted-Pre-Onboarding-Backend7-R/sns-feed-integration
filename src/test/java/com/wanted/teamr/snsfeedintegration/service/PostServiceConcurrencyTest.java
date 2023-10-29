package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.domain.SnsType;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class PostServiceConcurrencyTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("게시물 좋아요 기능 멀티 스레드로 동시에 총 1000번 요청")
    public void postLikeMultiThreadRequest1000() throws InterruptedException {
        // given: 좋아요 수가 0인 게시글 설정
        Post post = Post.builder()
                        .contentId("123456789")
                        .type(SnsType.FACEBOOK)
                        .title("게시물 제목")
                        .content("게시물 내용")
                        .viewCount(0L)
                        .likeCount(0L)
                        .shareCount(0L)
                        .createdAt(LocalDateTime.of(2023, 7, 7, 5, 23, 33))
                        .updatedAt(LocalDateTime.of(2023, 7, 7, 5, 23, 33))
                        .build();
        postRepository.save(post);
        assertThat(post.getLikeCount()).isEqualTo(0L);

        int totalExecutedCnt = 1000;
        int threadCnt = 16;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCnt);
        CountDownLatch latch = new CountDownLatch(totalExecutedCnt);

        // when: threadCnt개 만큼 스레드가 멀티스레드 방식으로 총 totalExecutedCnt번 게시글 좋아요 요청
        for (int idx = 0; idx < totalExecutedCnt; idx++) {
            executorService.execute(() -> {
                try {
                    postService.likePost(post.getId());
                } catch (CustomException ex) {
                    System.out.println(ex.getErrorCodeType());
                } catch (Exception ex) {
                    System.out.println(ex);
                } finally {
                    // CountDownLatch를 줄여 스레드가 완료됨을 알림
                    latch.countDown();
                }
            });
        }
        // 모든 스레드가 완료될 때까지 대기
        latch.await();

        // then: 게시글의 좋아요 수가 총 실행 횟수인 totalExecutedCnt와 같아야 한다
        Post samePost = postRepository.findById(post.getId()).get();
        assertThat(samePost.getLikeCount()).isEqualTo(totalExecutedCnt);
        postRepository.delete(samePost);
    }

}
