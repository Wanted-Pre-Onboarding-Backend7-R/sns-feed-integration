package com.wanted.teamr.snsfeedintegration.dto;

import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.domain.PostHashtag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PostDto {

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

    public static PostDto of(Post post) {
        List<String> hashtags = post.getPostHashtags().stream().map(PostHashtag::getHashtag).toList();
        return new PostDto(
                post.getId(), post.getContentId(), post.getType().name(), post.getTitle(), post.getContent(),
                hashtags, post.getViewCount(), post.getLikeCount(), post.getShareCount(),
                post.getCreatedAt(), post.getUpdatedAt()
        );
    }

}
