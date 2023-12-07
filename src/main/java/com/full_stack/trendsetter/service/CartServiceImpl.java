package com.full_stack.trendsetter.service;

import com.full_stack.trendsetter.exception.ProductException;
import com.full_stack.trendsetter.model.Cart;
import com.full_stack.trendsetter.model.CartItem;
import com.full_stack.trendsetter.model.Product;
import com.full_stack.trendsetter.model.User;
import com.full_stack.trendsetter.repository.CartRepository;
import com.full_stack.trendsetter.request.AddItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ProductService productService;

    @Override
    public Cart createCart(User user) {
       Cart cart = new Cart();
       cart.setUser(user);
       return cartRepository.save(cart);
    }

    @Override
    public Cart addCartItem(Long userId, AddItemRequest req) throws ProductException {
       Cart cart = cartRepository.findByUserId(userId); // we fetch the cart
       Product product = productService.findProductById(req.getProductId()); //

       CartItem isPresent = cartItemService.isCartItemExist(cart , product , req.getSize() , userId);

        if(isPresent == null){
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);

            int price = req.getQuantity() * product.getDiscountedPrice();
            cartItem.setPrice(price);
            cartItem.setSize(req.getSize());

            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
        }
//        System.out.println(cart);
        return cart;
    }


    @Override
    public Cart findUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);

        double totalPrice = 0;
        int totalDiscountedPrice =  0;
        int totalItem = 0;

        for(CartItem cartItem : cart.getCartItems()){
            totalPrice += cartItem.getPrice();
            totalDiscountedPrice += cartItem.getDiscountedPrice();
            totalItem += cartItem.getQuantity();
        }
        cart.setTotalPrice(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalDiscountedPrice((int) (totalPrice - totalDiscountedPrice));

        return cartRepository.save(cart);
    }
}
