package com.full_stack.trendsetter.service;


import com.full_stack.trendsetter.exception.UserException;
import com.full_stack.trendsetter.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public User findUserById(Long userId ) throws UserException;

    public User findUserProfileByJwt(String jwt) throws UserException;
}
