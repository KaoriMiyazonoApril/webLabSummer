package com.example.sports.repository;

import com.example.sports.po.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance,Integer> {
    Attendance findByAccount_IdAndActivity_Id(Integer userId, Integer activityId);
}
