package com.codestates.seb006main.posts.repository;

import com.codestates.seb006main.posts.dto.PostsCond;
import com.codestates.seb006main.posts.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PostsRepositoryCustom {
    Page<Posts> findAllWithCondition(PostsCond postsCond, PageRequest pageRequest);
}
