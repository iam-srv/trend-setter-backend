package com.full_stack.trendsetter.repository;

import com.full_stack.trendsetter.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository  extends JpaRepository<Rating , Long> {

    @Query("SELECT r from Rating r Where r.product.id = :productId")
    public List<Rating> getAllProductRating(@Param("productId") Long productId);
}
