package com.example.sports.repository;

import com.example.sports.po.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findByActivity_Id(Integer activityId);

    Comment findByAccount_IdAndActivity_Id(Integer userId, Integer activityId);
}
