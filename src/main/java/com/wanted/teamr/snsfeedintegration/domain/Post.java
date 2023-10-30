package com.wanted.teamr.snsfeedintegration.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseEntity {

    @Column(nullable = false)
    private String contentId;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SnsType type;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false)
    private Long likeCount;

    @Column(nullable = false)
    private Long shareCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    private Post(String contentId, SnsType type,
                 String title, String content, Long viewCount, Long likeCount,
                 Long shareCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.contentId = contentId;
        this.type = type;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.shareCount = shareCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void increaseViewCount() {
        viewCount += 1;
    }

    public void increaseLikeCount() {
        likeCount += 1;
    }

    public void increaseShareCount() {
        shareCount += 1;
    }

}
