package com.full_stack.trendsetter.controller;

import com.full_stack.trendsetter.exception.CartItemException;
import com.full_stack.trendsetter.exception.UserException;
import com.full_stack.trendsetter.model.CartItem;
import com.full_stack.trendsetter.model.User;
import com.full_stack.trendsetter.response.ApiResponse;
import com.full_stack.trendsetter.service.CartItemService;
import com.full_stack.trendsetter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {
    @Autowired
  private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long cartItemId ,
                                                      @RequestHeader("Authorization") String jwt) throws UserException , CartItemException{

        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.removeCartItem(user.getId() , cartItemId);

        ApiResponse res = new ApiResponse();
        res.setMessage("Item deleted Successfully");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long cartItemId, @RequestBody CartItem cartItem,
                                                   @RequestHeader("Authorization") String jwt) throws UserException , CartItemException {

        User user = userService.findUserProfileByJwt(jwt);
        CartItem updatedCartItem =  cartItemService.updateCartItem(user.getId() , cartItemId , cartItem);
        return new ResponseEntity<>(updatedCartItem , HttpStatus.OK);
    }

}
