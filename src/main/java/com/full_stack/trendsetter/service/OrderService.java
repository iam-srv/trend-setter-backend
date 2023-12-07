package com.full_stack.trendsetter.service;

import com.full_stack.trendsetter.exception.OrderException;
import com.full_stack.trendsetter.model.Address;
import com.full_stack.trendsetter.model.Order;
import com.full_stack.trendsetter.model.User;
import org.hibernate.metamodel.mapping.ordering.ast.OrderingExpression;

import java.util.List;

public interface OrderService {

    public Order createOrder(User user , Address shippingAddress);

    public Order findOrderById(Long orderId) throws OrderException;

    public List<Order> usersOrderHistory(Long userId);

    public Order placedOrder(Long orderId) throws OrderException;

    public Order confirmedOrder(Long orderId) throws OrderException;

    public Order shippedOrder(Long orderId) throws OrderException;

    public Order deliveredOrder (Long orderId) throws OrderException;

    public Order cancelledOrder(Long orderId) throws OrderException;

    public List<Order> getAllOrders();

    public void deleteOrder(Long orderId) throws OrderException;

}
