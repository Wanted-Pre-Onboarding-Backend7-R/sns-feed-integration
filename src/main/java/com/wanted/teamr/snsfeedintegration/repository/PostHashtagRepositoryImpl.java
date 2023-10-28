package com.wanted.teamr.snsfeedintegration.repository;

import static com.wanted.teamr.snsfeedintegration.domain.QPost.post;
import static com.wanted.teamr.snsfeedintegration.domain.QPostHashtag.postHashtag;
import static com.wanted.teamr.snsfeedintegration.domain.StatisticsValue.*;
import static com.wanted.teamr.snsfeedintegration.exception.ErrorCode.*;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.teamr.snsfeedintegration.domain.StatisticsType;
import com.wanted.teamr.snsfeedintegration.domain.StatisticsValue;
import com.wanted.teamr.snsfeedintegration.dto.StatisticsGetRequest;
import com.wanted.teamr.snsfeedintegration.dto.StatisticsGetResponse;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostHashtagRepositoryImpl implements PostHashtagRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<StatisticsGetResponse> getCountByCreatedAt(StatisticsGetRequest request) {
        String datetimeFormat = "%Y-%m-%d";
        if (StatisticsType.HOUR == request.getType()) {
            datetimeFormat += "T%H";
        }

        StringTemplate formattedDate = Expressions.stringTemplate(
                "date_format({0}, {1})",
                post.createdAt,
                datetimeFormat
        );

        return jpaQueryFactory.
                select(Projections.constructor(StatisticsGetResponse.class,
                        formattedDate.as("statisticsAt"),
                        countOrSumByStatisticsValue(request.getValue()).as("count"))
                )
                .from(postHashtag)
                .join(postHashtag.post, post)
                .where(
                        postHashtag.hashtag.eq(request.getHashtag()),
                        post.createdAt.goe(request.getStart()),
                        post.createdAt.loe(request.getEnd())
                )
                .groupBy(formattedDate)
                .orderBy(formattedDate.asc())
                .fetch();
    }

    private NumberExpression<Long> countOrSumByStatisticsValue(StatisticsValue statisticsValue) {
        if (COUNT == statisticsValue) {
            return post.count();
        } else if (LIKECOUNT == statisticsValue) {
            return post.likeCount.sum();
        } else if (SHARECOUNT == statisticsValue) {
            return post.shareCount.sum();
        } else if (VIEWCOUNT == statisticsValue) {
            return post.viewCount.sum();
        } else {
            throw new CustomException(STATISTICS_STATISTICSVALUE_NOT_FOUND);
        }
    }
}
