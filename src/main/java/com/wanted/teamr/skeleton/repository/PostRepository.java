package com.wanted.teamr.skeleton.repository;

import com.wanted.teamr.skeleton.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
