package com.example.sports.controller;

import com.example.sports.service.ActivityService;
import com.example.sports.service.CommentService;
import com.example.sports.vo.CommentVO;
import com.example.sports.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("/add")
    public Response addComment(@RequestBody CommentVO c){
        return Response.buildSuccess(commentService.addComment(c));
    }

    @GetMapping("/{activityId}")
    public Response getById(@PathVariable(value = "activityId") Integer activityId){
        return Response.buildSuccess(commentService.getCommentsByActivityId(activityId));
    }

    @DeleteMapping("/delete")
    public Response delete(@RequestBody CommentVO c){
        return Response.buildSuccess(commentService.deleteComment(c));
    }

    @GetMapping("/avg/{id}")
    public Response update(@PathVariable(value = "id") Integer id){
        return Response.buildSuccess(commentService.getAvgScore(id));
    }
}
