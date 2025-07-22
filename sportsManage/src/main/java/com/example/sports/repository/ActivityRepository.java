package com.example.sports.repository;

import com.example.sports.po.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ActivityRepository extends JpaRepository<Activity,Integer> {

    public List<Activity> findByNameContainingOrDetailContaining(String name, String detail);

    @Modifying
    @Query("UPDATE Activity ac SET ac.limitCount=ac.limitCount - 1" +
            "WHERE ac.id = :activityId AND ac.limitCount>=1")
    @Transactional
    int lockStock(@Param("activityId") Integer activityId);

    @Modifying
    @Query("UPDATE Activity ac SET ac.limitCount = ac.limitCount + 1" +
            "WHERE ac.id=:activityId")
    @Transactional
    void releaseStock(@Param("activityId") Integer activityId);


}