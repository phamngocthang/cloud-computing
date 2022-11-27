package com.cloudcomputing.service;

import com.cloudcomputing.entity.User;
import com.cloudcomputing.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public void saveOrUpdate(User user) {
        userRepository.save(user);
    }
}
