package com.example.sports.service;

import com.example.sports.exception.SportsException;
import com.example.sports.po.Account;
import com.example.sports.po.Comment;
import com.example.sports.repository.AccountRepository;
import com.example.sports.repository.CommentRepository;
import com.example.sports.util.SecurityUtil;
import com.example.sports.vo.AccountVO;
import com.example.sports.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
//()

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private AccountRepository accountRepository;

    public CommentVO addComment(CommentVO comment) {
        Account currentUser = securityUtil.getCurrentUser();
        if(currentUser == null){
            throw SportsException.notLogin();
        }
        if (comment.getActivity().getId() == null|| (comment.getScore() == null && comment.getDetail() == null)) {
            throw SportsException.NoEnoughArguments();
        }

        comment.setAccount(currentUser.toVO());
        Comment ret=comment.toPO();
        Comment a = commentRepository.findByAccount_IdAndActivity_Id(comment.getAccount().getId(), comment.getActivity().getId());
        if (a == null)
            commentRepository.save(ret);
        else{
            commentRepository.delete(a);
            commentRepository.save(ret);
        }
        return ret.toVO();
    }

    public boolean deleteComment(Integer userId,Integer activityId) {
        Account currentUser = securityUtil.getCurrentUser();
        if(currentUser == null){
            throw SportsException.notLogin();
        }
        if(Objects.equals(currentUser.getRole(), "Admin") || Objects.equals(currentUser.getId(), userId)){
            if(userId == null || activityId == null){
                throw SportsException.NoEnoughArguments();
            }
            Comment a=commentRepository.findByAccount_IdAndActivity_Id(userId,activityId);
            if(a == null){
                throw SportsException.CommentNotFound();
            }
            commentRepository.delete(a);
            return true;
        }
        else{
            throw SportsException.NoAccession();
        }

    }

    public List<CommentVO> getCommentsByActivityId(Integer activityId){

        Account currentUser=securityUtil.getCurrentUser();
        if(currentUser==null){
            throw SportsException.notLogin();
        }

        List<Comment> comments=commentRepository.findByActivity_Id(activityId);
        List<CommentVO> ret=new ArrayList<>();

        for (Comment comment : comments) {
            CommentVO a = comment.toVO();
            Account account = accountRepository.findById(comment.getAccount().getId()).get();
            AccountVO accountVO = account.toVO();
            accountVO.setTelephone(null);
            accountVO.setPassword(null);
            accountVO.setRole(null);
            a.setAccount(accountVO);
            ret.add(a);
        }

        for(int i=0;i<ret.size();i++){
            if(ret.get(i).getAccount().getId().equals(securityUtil.getCurrentUser().getId())){
                CommentVO target = ret.remove(i);
                ret.add(0, target);
                break;
            }
        }

        return ret;
    }

    public Double getAvgScore(Integer activityId){
        Account currentUser=securityUtil.getCurrentUser();
        if(currentUser==null){
            throw SportsException.notLogin();
        }

        List<Comment> comments=commentRepository.findByActivity_Id(activityId);
        double sum=0;
        double num=0;
        for(int i=0;i<comments.size();i++){
            if(comments.get(i).getScore()!=0){
                num++;
                sum+=comments.get(i).getScore();
            }
        }
        if(num==0){
            return 0.0;
        }
        return Math.round((sum / num) * 10.0) / 10.0;
    }
}
