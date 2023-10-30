package com.wanted.teamr.snsfeedintegration.dto;

import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.domain.PostHashtag;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostGetResponse {

    private final Long postId;
    private final String contentId;
    private final String type;
    private final String title;
    private final String content;
    private final List<String> hashtags;
    private final Long viewCount;
    private final Long likeCount;
    private final Long shareCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    private PostGetResponse(Long postId, String contentId, String type, String title, String content,
                            List<String> hashtags, Long viewCount, Long likeCount, Long shareCount,
                            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.postId = postId;
        this.contentId = contentId;
        this.type = type;
        this.title = title;
        this.content = content;
        this.hashtags = hashtags;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.shareCount = shareCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PostGetResponse of(Post post) {
        List<String> hashtags = post.getPostHashtags().stream().map(PostHashtag::getHashtag).toList();
        return PostGetResponse.builder()
                .postId(post.getId())
                .contentId(post.getContentId())
                .type(post.getType().name().toLowerCase())
                .title(post.getTitle())
                .content(post.getContent())
                .hashtags(hashtags)
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .shareCount(post.getShareCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static PostGetResponse of(Long postId, String contentId, String type, String title, String content,
                                     List<String> hashtags, Long viewCount, Long likeCount, Long shareCount,
                                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        return PostGetResponse.builder()
                .postId(postId)
                .contentId(contentId)
                .type(type)
                .title(title)
                .content(content)
                .hashtags(hashtags)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .shareCount(shareCount)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }


}
