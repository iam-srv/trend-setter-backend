package com.full_stack.trendsetter.service;

import com.full_stack.trendsetter.exception.ProductException;
import com.full_stack.trendsetter.model.Cart;
import com.full_stack.trendsetter.model.User;
import com.full_stack.trendsetter.request.AddItemRequest;

public interface CartService {

    public Cart createCart (User user);

    public Cart addCartItem (Long userId , AddItemRequest req)throws ProductException;

    public Cart findUserCart(Long userId);

}
