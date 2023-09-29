package com.api.users.controller;

import com.api.users.model.User;
import com.api.users.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${user.minAge}")
    private int minAge;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        try {
            validateUserAge(user.getBirthDate());

            userService.createUser(user);

            return ResponseEntity.ok("User created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PatchMapping("/{email}")
    public ResponseEntity<String> updateUserFields(
            @PathVariable @Email(message = "Invalid email") String email,
            @RequestBody User changeUser) {
        try {
            User user = userService.findUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }

            userService.updateUserFields(email, changeUser);
            return ResponseEntity.ok("User fields updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable @Email(message = "Invalid email") String email) {
        try {
            User user = userService.findUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }

            userService.deleteUser(user);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsersByBirthDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            validateDateRange(from, to);

            List<User> users = userService.findUsersByBirthDateRange(from, to);
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }

    private ResponseEntity<String> handleValidationErrors(BindingResult bindingResult) {
        String errorMessage = "Validation failed for one or more fields";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    private void validateUserAge(LocalDate birthDate) {
        int userAge = LocalDate.now().getYear() - birthDate.getYear();
        if (userAge < minAge) {
            throw new IllegalArgumentException("User must be at least " + minAge + " years old");
        }
    }

    private void validateDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'From' date must be before 'To' date");
        }
    }
}
