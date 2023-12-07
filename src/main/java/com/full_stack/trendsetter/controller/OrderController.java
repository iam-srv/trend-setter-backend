package com.full_stack.trendsetter.controller;

import com.full_stack.trendsetter.exception.OrderException;
import com.full_stack.trendsetter.exception.UserException;
import com.full_stack.trendsetter.model.Address;
import com.full_stack.trendsetter.model.Order;
import com.full_stack.trendsetter.model.User;
import com.full_stack.trendsetter.service.OrderService;
import com.full_stack.trendsetter.service.UserService;
import com.sun.net.httpserver.HttpsServer;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddress ,
                                             @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);

        Order order = orderService.createOrder(user , shippingAddress);
        System.out.println("Order" + order);

        return new ResponseEntity<Order>(order , HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>>  usersOrderHistory(@RequestHeader("Authorization") String jwt ) throws UserException{
        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orders = orderService.usersOrderHistory(user.getId());

        return new ResponseEntity<>(orders , HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable("id") Long orderId ,
                                               @RequestHeader("Authorization") String jwt) throws UserException , OrderException{
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(order , HttpStatus.ACCEPTED);
    }

}
