package com.full_stack.trendsetter.repository;

import com.full_stack.trendsetter.model.Cart;
import com.full_stack.trendsetter.model.CartItem;
import com.full_stack.trendsetter.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem , Long> {

    @Query("SELECT ci from CartItem ci Where ci.cart= :cart AND ci.product= :product AND ci.size = :size AND ci.userId= :userId")
    public CartItem isCartItemExist(@Param("cart") Cart cart ,
                                    @Param("product")Product product ,
                                    @Param("size") String size,
                                    @Param("userId") Long userId);
}
