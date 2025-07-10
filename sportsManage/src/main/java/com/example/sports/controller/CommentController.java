package com.example.sports.controller;

import com.example.sports.service.ProductService;
import com.example.sports.vo.CommentVO;
import com.example.sports.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    ProductService productService;

    @PostMapping("/add")
    public Response addComment(@RequestBody CommentVO c){
        return Response.buildSuccess(productService.addComment(c));
    }

    @GetMapping("/{productId}")
    public Response getById(@PathVariable(value = "productId") Integer productId){
        return Response.buildSuccess(productService.findByProductId(productId));
    }

    @DeleteMapping("/delete")
    public Response delete(@RequestBody CommentVO c){
        return Response.buildSuccess(productService.deleteComment(c));
    }

    @PutMapping("/update/{id}")
    public Response update(@RequestBody CommentVO c,@PathVariable(value = "id") Integer id){
        return Response.buildSuccess(productService.updateComment(c,id));
    }
}
