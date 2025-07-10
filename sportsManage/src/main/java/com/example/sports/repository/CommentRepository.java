package com.example.sports.repository;

import com.example.sports.po.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findByProductId(Integer productId);

    Comment findByUserIdAndProductId(Integer userId,Integer productId);
}
