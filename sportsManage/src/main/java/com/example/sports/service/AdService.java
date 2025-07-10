package com.example.sports.service;

import com.example.sports.exception.SportsException;
import com.example.sports.po.Ad;
import com.example.sports.repository.AdRepository;
import com.example.sports.vo.AdVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdService {
    @Autowired
    AdRepository adRepository;

    public List<AdVO> getAllAds(){
        List<Ad> ls=adRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<AdVO> ans=new ArrayList<>();
        for(Ad tem:ls){
            ans.add(tem.toVO());
        }
        return ans;
    }

    public AdVO updateAd(AdVO a){
        if(a.getId()==null || a.getProductId()==null)
            throw SportsException.NoEnoughArguments();

        Ad ad=adRepository.findByIdAndProductId(Integer.parseInt(a.getId()),Integer.parseInt(a.getProductId()));
        if(ad==null)
            throw SportsException.ProductNotFound();
        if(a.getTitle()!=null)
            ad.setTitle(a.getTitle());
        if(a.getImgUrl()!=null)
            ad.setImage(a.getImgUrl());
        if(a.getContent()!=null)
            ad.setContent(a.getContent());
        adRepository.save(ad);
        return ad.toVO();
    }
    public AdVO addAd(AdVO a){
        Ad ad=new Ad();
        ad.setTitle(a.getTitle());
        ad.setImage(a.getImgUrl());
        ad.setContent(a.getContent());
        ad.setProductId(Integer.parseInt(a.getProductId()));
        adRepository.save(ad);
        return ad.toVO();
    }
    public String deleteAd(String id){
        Ad ad=adRepository.findById(Integer.valueOf(id))
                .orElseThrow(SportsException::AdNotFound);
        adRepository.delete(ad);
        return "删除成功";
    }
}
