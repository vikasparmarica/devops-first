package com.azure.devops.first.service;

import com.azure.devops.first.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private List<User> users = new ArrayList<>();

    public List<User> findAllUsers() {
        return users;
    }

    public User findById(String id) {
        User defaultUser = new User("NULL", "NULL", 0);
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(defaultUser);
    }

    public boolean isUserExist(User user) {
        return users.stream()
                .anyMatch(user1 -> user1.equals(user));
    }

    public void saveUser(User user) {
        users.add(user);
    }

    public void deleteUserById(User user) {
        users.remove(user);
    }

    public void deleteAllUsers() {
        users.clear();
    }
}
