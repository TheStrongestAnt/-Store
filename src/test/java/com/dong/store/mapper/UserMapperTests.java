package com.dong.store.mapper;

import com.dong.store.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class UserMapperTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void updatePasswordByUid(){
        Integer uid = 14;
        String password = "123456";
        String modifiedUser = "超级管理员";
        Date modifiedTime = new Date();
        Integer integer = userMapper.updatePasswordByUid(uid,password,modifiedUser,modifiedTime);
        System.out.println(integer);
    }
    @Test
    public void findById(){
        Integer uid = 14;
        UserEntity byUid = userMapper.findByUid(uid);
        System.out.println(byUid);
    }

    @Test
    public void updateInfoByUid(){
        UserEntity userEntity=new UserEntity();
        userEntity.setUid(30);
        userEntity.setPhone("19195524342");
//        userEntity.setEmail(null);
        userEntity.setGender(1);
        userEntity.setAvatar("xxxxxxx");
        userEntity.setModifiedUser("系统管理员");
        userEntity.setModifiedTime(new Date());
        Integer integer = userMapper.updateInfoByUid(userEntity);
        System.out.println(integer);
    }

    @Test
    public void updateAvatarByUid() {
        Integer uid = 30;
        String avatar = "/upload/avatar.jpg";
        String modifiedUser = "超级管理员";
        Date modifiedTime = new Date();
        Integer rows = userMapper.updateAvatarByUid(uid, avatar, modifiedUser, modifiedTime);
        System.out.println("rows=" + rows);
    }
}
