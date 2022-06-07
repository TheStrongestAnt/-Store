package com.dong.store.service;

import com.dong.store.entity.UserEntity;

public interface IUserService {
    /**
     * 注册用户
     * @param userEntity 用户数据
     */
    void reg(UserEntity userEntity);

    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @param password 用户密码
     * @return 登录成功的用户数据
     */
    UserEntity login(String username,String password);

    /**
     * 修改密码
     * @param uid 用户id
     * @param username 用户名
     * @param oldPassword 用户原密码
     * @param newPassword 用户更改的新密码
     * @return
     */
    void changePassword(Integer uid,String username,String oldPassword,String newPassword);

    /**
     * 修改用户信息
     * @param uid 用户id
     * @param username 用户名
     * @param user 用户信息
     */
    void changeInfo(Integer uid, String username, UserEntity user);

    /**
     * 获取当前登录的用户的信息
     * @param uid 用户id
     * @return 当前登录的用户的信息
     */
    UserEntity getByUid(Integer uid);

    /**
     * 通过用户uid改变用户头像
     * @param uid 当前登录的用户id
     * @param username 当前登录的用户名
     * @param avatar 用户新头像的路径
     */
    void changeAvatar(Integer uid,String username,String avatar);
}
