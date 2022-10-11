package com.codestates.seb006main.matching.repository;

import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.posts.dto.PostsCond;
import com.codestates.seb006main.posts.entity.Posts;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.codestates.seb006main.matching.entity.QMatching.matching;
import static com.codestates.seb006main.posts.entity.QPosts.posts;

public class MatchingRepositoryImpl implements MatchingRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public MatchingRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    // TODO: OR이냐 AND냐 아님 BooleanExpression 써야 하냐
    @Override
    public List<Matching> findClosedMatching() {
        return queryFactory
                .selectFrom(matching)
                .where(matching.matchingStatus.eq(Matching.MatchingStatus.REFUSED).or(matching.matchingStatus.eq(Matching.MatchingStatus.ACCEPTED)))
                .fetch();
    }

    @Override
    public Page<Matching> findAllNotMatched(PageRequest pageRequest) {
        List<Matching> content = searchMatching(pageRequest);
        JPAQuery<Long> countQuery = getCountQuery();
        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

    private JPAQuery<Long> getCountQuery() {
        return queryFactory
                .select(matching.count())
                .from(matching)
                .where(matching.matchingStatus.eq(Matching.MatchingStatus.READ).or(matching.matchingStatus.eq(Matching.MatchingStatus.NOT_READ)));
    }

    private List<Matching> searchMatching(PageRequest pageRequest) {
        return queryFactory
                .selectFrom(matching)
                .where(matching.matchingStatus.eq(Matching.MatchingStatus.READ).or(matching.matchingStatus.eq(Matching.MatchingStatus.NOT_READ)))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(new OrderSpecifier<>(Order.DESC, matching.matchingId))
                .fetch();
    }
}
