package com.sns.repository;

import com.sns.domain.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {
}
