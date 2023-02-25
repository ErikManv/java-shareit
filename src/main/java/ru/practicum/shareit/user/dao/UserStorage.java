package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserStorage {

    private Integer userId = 0;
    private void countId() {
        userId++;
    }

    private final Map<Integer, User> users = new HashMap<>();

    public List<User> findAll(){
        return new ArrayList<>(users.values());
    }

    public User createUser(User user){
        countId();
        user.setId(userId);
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User userUpdate, Integer userId){
        User userOld = users.get(userId);
        if(userUpdate.getName() != null) {
            userOld.setName(userUpdate.getName());
        }
        if(userUpdate.getEmail() != null) {
            userOld.setEmail(userUpdate.getEmail());
        }
        return userOld;
    }

    public User getUser(Integer userId){
        return users.get(userId);
    }

    public void deleteUser(Integer userId) {
        users.remove(userId);
    }

}
