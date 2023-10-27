package com.wanted.teamr.snsfeedintegration.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PostHashtag extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Post post;

    @Column(nullable = false)
    private String hashtag;

    @Builder
    private PostHashtag(Post post, String hashtag) {
        setPost(post);
        this.hashtag = hashtag;
    }

    public void setPost(Post post) {
        if (this.post != null) {
            this.post.getPostHashtags().remove(this);
        }
        this.post = post;
        post.getPostHashtags().add(this);
    }

}

