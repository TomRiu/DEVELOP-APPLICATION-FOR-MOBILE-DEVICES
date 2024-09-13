package com.example.bt1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserList {
    private static List<User> userList = new ArrayList<>();

    static {
        User user1 = new User("B21DCVT442", "23042003", "Nguyễn Đức Anh Tú", "nguyenducanhtu@email.com");
        User user2 = new User("B21DCVT224", "30032003", "Hà Nhật Huy", "hanhathuy@email.com");
        userList.add(user1);
        userList.add(user2);
    }

    public static List<User> getUserList() {
        return new ArrayList<>(userList);
    }

    public static void addUser(User user) {
        userList.add(user);
    }

    public static User findUserByUsername(String username) {
        Predicate<User> predicate = u -> u.getUsername().equals(username);
        Optional<User> user = userList.stream()
                .filter(predicate)
                .findFirst();
        return user.orElse(null);
    }

    public static List<User> findAllUsersByUsername(String query) {
        Predicate<User> predicate = u -> u.getUsername().contains(query);
        return userList.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static List<User> findAllUsersByEmail(String query) {
        Predicate<User> predicate = u -> u.getEmail().toLowerCase().contains(query.toLowerCase());
        return userList.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static void updateUser(String username, String newPassword, String newFullname, String newEmail) {
        User user = findUserByUsername(username);
        user.setPassword(newPassword);
        user.setFullname(newFullname);
        user.setEmail(newEmail);
    }

    public static void delete(User user) {
        userList.remove(user);
    }
}
