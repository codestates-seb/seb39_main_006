package com.codestates.seb006main.Image.repository;

import com.codestates.seb006main.Image.entity.Image;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.codestates.seb006main.Image.entity.QImage.image;

public class ImageRepositoryImpl implements ImageRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public ImageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Image> findUnusedImages(LocalDateTime aHourAgo) {
        return queryFactory
                .selectFrom(image)
                .where(isNullAndLoe(aHourAgo))
                .fetch();
    }

    private BooleanExpression isNullAndLoe(LocalDateTime aHourAgo) {
        return image.feed.isNull().and(image.member.isNull().and(image.posts.isNull())).and(image.uploadedAt.loe(aHourAgo));
    }
}
