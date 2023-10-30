package com.wanted.teamr.snsfeedintegration.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.teamr.snsfeedintegration.domain.Post;
import com.wanted.teamr.snsfeedintegration.domain.SearchByType;
import com.wanted.teamr.snsfeedintegration.domain.SnsType;
import com.wanted.teamr.snsfeedintegration.dto.PostSearchCondition;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.wanted.teamr.snsfeedintegration.domain.QPost.post;
import static com.wanted.teamr.snsfeedintegration.domain.QPostHashtag.postHashtag;

public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Post> search(PostSearchCondition condition, Pageable pageable) {
        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .join(post.postHashtags, postHashtag)
                .on(
                        postHashtag.hashtag.eq(condition.getHashtag())
                )
                .where(
                        typeEqual(condition.getType()),
                        searchContain(condition.getSearchBy(), condition.getSearch())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder<Post> pathBuilder = new PathBuilder<>(post.getType(), post.getMetadata());
            query.orderBy(
                    new OrderSpecifier(
                            o.isAscending() ? Order.ASC : Order.DESC,
                            pathBuilder.get(o.getProperty())
                    )
            );
        }
        return query.fetch();
    }

    private BooleanExpression typeEqual(SnsType type) {
        return type == null ? null : post.type.eq(type);
    }

    private BooleanExpression searchContain(SearchByType searchBy, String search) {
        if (search == null) {
            return null;
        }

        if (searchBy == SearchByType.TITLE) {
            return post.title.contains(search);
        }

        if (searchBy == SearchByType.CONTENT) {
            return post.content.contains(search);
        }

        return post.title.contains(search).or(post.content.contains(search));
    }

}
