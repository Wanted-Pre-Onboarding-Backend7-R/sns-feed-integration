package com.wanted.teamr.snsfeedintegration.repository;

import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.dto.PostSearchCondition;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> search(PostSearchCondition condition, Pageable pageable);

}
