package com.api.users.repository;

import com.api.users.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private final List<User> users = new ArrayList<>();

    public void createUser(User user) {
        users.add(user);
    }

    public User findUserByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public boolean updateUserFields(String email, User user) {
        User existingUser = findUserByEmail(email);
        if (existingUser == null) {
            return false;
        }

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setBirthDate(user.getBirthDate());
        existingUser.setAddress(user.getAddress());
        existingUser.setPhoneNumber(user.getPhoneNumber());

        return true;
    }

    public boolean deleteUser(User user) {
        return users.remove(user);
    }

    public List<User> findAllUsers() {
        return new ArrayList<>(users);
    }

    public List<User> searchUsersByBirthDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Invalid date range: 'From' date must be before 'To' date");
        }

        List<User> usersInRange = new ArrayList<>();

        for (User user : users) {
            LocalDate userBirthDate = user.getBirthDate();
            if (userBirthDate != null && !userBirthDate.isBefore(from) && !userBirthDate.isAfter(to)) {
                usersInRange.add(user);
            }
        }

        return usersInRange;
    }
}
