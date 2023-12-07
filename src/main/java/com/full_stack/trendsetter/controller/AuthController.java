package com.full_stack.trendsetter.controller;

//User controller

import com.full_stack.trendsetter.config.JwtProvider;
import com.full_stack.trendsetter.exception.UserException;
import com.full_stack.trendsetter.model.Cart;
import com.full_stack.trendsetter.model.User;
import com.full_stack.trendsetter.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.full_stack.trendsetter.repository.UserRepository;
import com.full_stack.trendsetter.request.LoginRequest;
import com.full_stack.trendsetter.response.AuthResponse;
import com.full_stack.trendsetter.service.CustomUserServiceImplementation;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepository userRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;

    private CartService cartService;
    private CustomUserServiceImplementation customUserServiceImplementation;

    public AuthController(UserRepository userRepository , CustomUserServiceImplementation customUserServiceImplementation , PasswordEncoder passwordEncoder,
    JwtProvider jwtProvider ,  CartService cartService) {
        this.userRepository = userRepository;
        this.customUserServiceImplementation = customUserServiceImplementation;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this. cartService = cartService;
    }


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException{

        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        User isEmailExist = userRepository.findByEmail(email);

        if(isEmailExist != null){
            throw new UserException("Email already used with another account");
        }

        User createdUSer = new User();
        createdUSer.setEmail(email);
        createdUSer.setPassword(passwordEncoder.encode(password));
        createdUSer.setFirstName(firstName);
        createdUSer.setLastName(lastName);

        User savedUser = userRepository.save(createdUSer);
         Cart cart = cartService.createCart(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail() , savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Signup Success");

        return new ResponseEntity<AuthResponse>(authResponse , HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler (@RequestBody LoginRequest loginRequest ){

       String userName = loginRequest.getEmail();
       String password = loginRequest.getPassword();

       Authentication authentication = authenticate(userName , password);
       SecurityContextHolder.getContext().setAuthentication(authentication);


       String token = jwtProvider.generateToken(authentication);

       AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("SignIn Success");
       return new ResponseEntity<>(authResponse , HttpStatus.CREATED);

    }
    private Authentication authenticate (String username , String password){

        UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(username);

        if(userDetails == null){
            throw new BadCredentialsException("Invalid UserName");
        }

        if(!passwordEncoder.matches(password , userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails , null , userDetails.getAuthorities());

    }
}
