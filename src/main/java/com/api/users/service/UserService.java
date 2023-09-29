package com.api.users.service;

import com.api.users.model.User;
import com.api.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(User user) {
        userRepository.createUser(user);
    }

    public boolean updateUserFields(String email, User user) {
        return userRepository.updateUserFields(email, user);
    }

    public boolean deleteUser(User user) {
        return userRepository.deleteUser(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public List<User> findUsersByBirthDateRange(LocalDate from, LocalDate to) {
        return userRepository.searchUsersByBirthDateRange(from, to);
    }
}
