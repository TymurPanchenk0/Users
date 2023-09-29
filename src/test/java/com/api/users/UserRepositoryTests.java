package com.api.users;

import com.api.users.model.User;
import com.api.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTests {

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    public void testCreateUser() {
        User user = new User("test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), null, null);
        userRepository.createUser(user);

        List<User> users = userRepository.findAllUsers();
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    public void testFindUserByEmail() {
        User user = new User("test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), null, null);
        userRepository.createUser(user);

        User foundUser = userRepository.findUserByEmail("test@example.com");
        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    public void testFindUserByEmailNotFound() {
        User foundUser = userRepository.findUserByEmail("nonexistent@example.com");
        assertNull(foundUser);
    }

    @Test
    public void testUpdateUserFields() {
        User user = new User("test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), null, null);
        userRepository.createUser(user);

        User updatedUser = new User("test@example.com", "Updated", "User", LocalDate.of(1995, 2, 2), "Updated Address", "123456789");
        userRepository.updateUserFields(user.getEmail(), updatedUser);

        User foundUser = userRepository.findUserByEmail("test@example.com");
        assertNotNull(foundUser);
        assertEquals(updatedUser, foundUser);
    }

    @Test
    public void testDeleteUser() {
        User user = new User("test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), null, null);
        userRepository.createUser(user);

        assertTrue(userRepository.deleteUser(user));

        User foundUser = userRepository.findUserByEmail("test@example.com");
        assertNull(foundUser);
    }

    @Test
    public void testDeleteUserNotFound() {
        User user = new User("test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), null, null);

        assertFalse(userRepository.deleteUser(user));
    }

    @Test
    public void testFindAllUsers() {
        User user1 = new User("test1@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), null, null);
        User user2 = new User("test2@example.com", "Jane", "Smith", LocalDate.of(1985, 3, 3), null, null);

        userRepository.createUser(user1);
        userRepository.createUser(user2);

        List<User> users = userRepository.findAllUsers();
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    public void testFindUsersByBirthDateRange() {
        User user1 = new User("test1@example.com", "John", "Doe", LocalDate.of(1990, 2, 12), null, null);
        User user2 = new User("test2@example.com", "Jane", "Smith", LocalDate.of(1985, 3, 20), null, null);
        User user3 = new User("test3@example.com", "Bob", "Johnson", LocalDate.of(1995, 5, 1), null, null);

        userRepository.createUser(user1);
        userRepository.createUser(user2);
        userRepository.createUser(user3);

        LocalDate from = LocalDate.of(1989, 1, 1);
        LocalDate to = LocalDate.of(1995, 10, 30);

        List<User> usersInRange = userRepository.searchUsersByBirthDateRange(from, to);
        assertEquals(2, usersInRange.size());
        assertTrue(usersInRange.contains(user1));
        assertTrue(usersInRange.contains(user3));
    }
}
