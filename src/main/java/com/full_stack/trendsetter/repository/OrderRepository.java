package com.full_stack.trendsetter.repository;

import com.full_stack.trendsetter.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order , Long> {

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND (o.OrderStatus = 'PLACED' OR o.OrderStatus = 'CONFIRMED' OR o.OrderStatus = 'SHIPPED' OR o.OrderStatus = 'DELIVERED')")
    public List<Order> getUsersOrders(@Param("userId") Long userId);
}
