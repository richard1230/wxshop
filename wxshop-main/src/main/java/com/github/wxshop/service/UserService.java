package com.github.wxshop.service;

import com.github.wxshop.dao.UserDao;
import com.github.wxshop.generate.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUserIfNotExist(String tel) {
        // 这里不能先写存不存在再添加用户(并发环境下会出问题)
        User user = new User();
        user.setTel(tel);
        user.setCreateAt(new Date());
        user.setUpdatedAt(new Date());
        try {
            userDao.insertUser(user);
        } catch (Exception e) {
            return userDao.getUserByTel(tel);
        }
        return user;
    }

    public Optional<User> getUserByTel(String tel) {
        return Optional.ofNullable(userDao.getUserByTel(tel));
    }
}
