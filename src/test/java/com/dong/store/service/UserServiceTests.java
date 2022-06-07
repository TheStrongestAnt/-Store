package com.dong.store.service;

import com.dong.store.entity.UserEntity;
import com.dong.store.service.ex.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class UserServiceTests {

    @Autowired
    private IUserService iUserService;


    @Test
    public void regTest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user0145");
        userEntity.setPassword("123456");
        userEntity.setGender(1);
        userEntity.setPhone("17858802974");
        userEntity.setEmail("lower@tedu.cn");
        userEntity.setAvatar("xxxx");
        iUserService.reg(userEntity);
        System.out.println("注册成功");
    }
    @Test
    public void loginTest(){
        try {
            String username = "user0145";
            String password = "123456";
            UserEntity login = iUserService.login(username, password);
            System.out.println("登录成功！" + login);
        } catch (ServiceException e) {
            System.out.println("登录失败！" + e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void getByUid(){
        try {
            Integer uid = 31;
            UserEntity byUid = iUserService.getByUid(uid);
            System.out.println(byUid);
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void changeInfo(){
        Integer uid = 31;
        String username = "谢曾";
        UserEntity userEntity=new UserEntity();
        userEntity.setPhone("15512328888");
        userEntity.setEmail("admin03@cy.cn");
        userEntity.setGender(2);
        iUserService.changeInfo(uid,username,userEntity);
    }

    @Test
    public void changeAvatar() {
        try {
            Integer uid = 13;
            String username = "头像管理员";
            String avatar = "/upload/avatar.png";
            iUserService.changeAvatar(uid, username, avatar);
            System.out.println("OK.");
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }

}
