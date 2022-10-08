package com.codestates.seb006main.posts.repository;

import com.codestates.seb006main.posts.dto.PostsCond;
import com.codestates.seb006main.posts.entity.Posts;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.codestates.seb006main.posts.entity.QPosts.posts;

public class PostsRepositoryImpl implements PostsRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public PostsRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Posts> findActiveById(Long postId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(posts)
                .where(posts.postId.eq(postId).and(posts.postsStatus.ne(Posts.PostsStatus.INACTIVE)))
                .fetchOne());
    }

    @Override
    public Page<Posts> findAllWithCondition(PostsCond postsCond, PageRequest pageRequest) {
        List<Posts> content = searchPosts(postsCond, pageRequest);
        JPAQuery<Long> countQuery = getCountQuery(postsCond);
        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

    private JPAQuery<Long> getCountQuery(PostsCond postsCond) {
        return queryFactory
                .select(posts.count())
                .from(posts)
                .where(
                        likeContent(postsCond.getTitle(), postsCond.getBody(), postsCond.getLocation()),
//                        likeTitle(postsCond.getTitle()),
//                        likeBody(postsCond.getBody()),
//                        likeLocation(postsCond.getLocation()),
                        isRecruiting(postsCond.getFilters()),
                        afterStartDate(postsCond.getStartDate()),
                        beforeEndDate(postsCond.getEndDate()),
                        posts.postsStatus.ne(Posts.PostsStatus.INACTIVE));
    }

    private List<Posts> searchPosts(PostsCond postsCond, PageRequest pageRequest) {
        return queryFactory
                .selectFrom(posts)
                .where(
                        likeContent(postsCond.getTitle(), postsCond.getBody(), postsCond.getLocation()),
//                        likeTitle(postsCond.getTitle()),
//                        likeBody(postsCond.getBody()),
//                        likeLocation(postsCond.getLocation()),
                        afterStartDate(postsCond.getStartDate()),
                        beforeEndDate(postsCond.getEndDate()),
                        isRecruiting(postsCond.getFilters()),
                        posts.postsStatus.ne(Posts.PostsStatus.INACTIVE))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(getSort(pageRequest))
                .fetch();
    }

    private BooleanBuilder likeContent(String title, String body, String location) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (StringUtils.hasText(title)) {
            booleanBuilder.or(posts.title.likeIgnoreCase("%" + title + "%"));
        }
        if (StringUtils.hasText(body)) {
            booleanBuilder.or(posts.body.likeIgnoreCase("%" + body + "%"));
        }
        if (StringUtils.hasText(location)) {
            booleanBuilder.or(posts.location.likeIgnoreCase("%" + location + "%"));
        }
        return booleanBuilder;
    }

    private BooleanExpression afterStartDate(String startDate) {
        if (StringUtils.hasText(startDate)) {
            return posts.travelPeriod.startDate.goe(LocalDate.parse(startDate));
        }
        return null;
    }

    private BooleanExpression beforeEndDate(String endDate) {
        if (StringUtils.hasText(endDate)) {
            return posts.travelPeriod.endDate.loe(LocalDate.parse(endDate));
        }
        return null;
    }

    private BooleanExpression isRecruiting(List<String> filters) {
        if (filters.contains("Recruiting")) {
            return posts.postsStatus.eq(Posts.PostsStatus.RECRUITING);
        }
        return null;
    }

    private BooleanExpression likeTitle(String title) {
        if (StringUtils.hasText(title)) {
            return posts.title.likeIgnoreCase("%" + title + "%");
        }
        return null;
    }

    private BooleanExpression likeBody(String body) {
        if (StringUtils.hasText(body)) {
            return posts.body.likeIgnoreCase("%" + body + "%");
        }
        return null;
    }

    private BooleanExpression likeLocation(String location) {
        if (StringUtils.hasText(location)) {
            return posts.location.likeIgnoreCase("%" + location + "%");
        }
        return null;
    }

    /**
     * OrderSpecifier 를 쿼리로 반환하여 정렬조건을 맞춰준다.
     * 리스트 정렬
     * @param page
     * @return
     */
    private OrderSpecifier<?> getSort(Pageable page) {
        //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
        if (!page.getSort().isEmpty()) {
            //정렬값이 들어 있으면 for 사용하여 값을 가져온다
            for (Sort.Order order : page.getSort()) {
                // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                switch (order.getProperty()) {
                    case "postId": case "newest":
                        return new OrderSpecifier<>(Order.DESC, posts.postId);
                    case "title":
                        return new OrderSpecifier<>(direction, posts.title);
                    case "startDate":
                        return new OrderSpecifier<>(Order.ASC, posts.travelPeriod.startDate);
                    case "endDate":
                        return new OrderSpecifier<>(Order.ASC, posts.travelPeriod.endDate);
                    case "closeDate":
                        return new OrderSpecifier<>(Order.ASC, posts.closeDate);
                    case "totalCount":
                        return new OrderSpecifier<>(direction, posts.totalCount);
                    case "limited":
                        return new OrderSpecifier<>(Order.ASC, posts.totalCount.subtract(posts.participants.size()));
                }
            }
        }
        return null;
    }
}
