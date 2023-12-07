package com.full_stack.trendsetter.service;


import com.full_stack.trendsetter.config.JwtProvider;
import com.full_stack.trendsetter.exception.UserException;
import com.full_stack.trendsetter.model.User;
import com.full_stack.trendsetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserById(Long userId) throws UserException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return user.get();
        }
        throw new UserException("No User Found with id" + userId);
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);

        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new UserException("User not found with email" + email);
        }
        return user;
    }
}
