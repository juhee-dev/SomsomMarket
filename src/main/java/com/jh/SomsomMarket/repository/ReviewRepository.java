package com.jh.SomsomMarket.repository;

import com.jh.SomsomMarket.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUserId(String userId); // userId로 리뷰 리스트 반환
//    void deleteByAccountId(String id);

    List<Review> findReviewByUserId(String userId); // userId로 리뷰 리스트 반환
    void deleteByUserId(String id);

}
