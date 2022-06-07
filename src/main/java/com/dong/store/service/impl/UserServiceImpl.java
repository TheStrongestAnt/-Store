package com.dong.store.service.impl;

import com.dong.store.entity.UserEntity;
import com.dong.store.mapper.UserMapper;
import com.dong.store.service.IUserService;
import com.dong.store.service.ex.*;
import com.dong.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public void reg(UserEntity userEntity) {


        // 根据参数user对象获取注册的用户名
        String username = userEntity.getUsername();
        // 调用持久层的User findByUsername(String username)方法，根据用户名查询用户数据
        UserEntity result = userMapper.findByUsername(username);
        // 判断查询结果是否不为null
        if (result != null) {
            // 是：表示用户名已被占用，则抛出UsernameDuplicateException异常
            throw new UsernameDuplicateException("用户名已被使用");
        }
        // 创建当前时间对象
        Date date = new Date();
        // 补全数据：加密后的密码
        //获取盐值
        String salt = UUID.randomUUID().toString().toUpperCase();
        String md5Password = getMd5Password(userEntity.getPassword(), salt);
        userEntity.setPassword(md5Password);
        // 补全数据：盐值
        userEntity.setSalt(salt);
        // 补全数据：isDelete(0)
        userEntity.setIsDelete(0);
        // 补全数据：4项日志属性
        userEntity.setCreatedUser(userEntity.getUsername());
        userEntity.setCreatedTime(date);
        userEntity.setModifiedUser(userEntity.getUsername());
        userEntity.setModifiedTime(date);
        // 表示用户名没有被占用，则允许注册
        // 调用持久层Integer insert(User user)方法，执行注册并获取返回值(受影响的行数)
        Integer rows = userMapper.insert(userEntity);
        // 判断受影响的行数是否不为1
        // 是：插入数据时出现某种错误，则抛出InsertException异常
        if (rows != 1) {
            throw new InsertException("在用户注册过程中产生了未知异常");
        }
    }

    @Override
    public UserEntity login(String username, String password) {
        // 调用userMapper的findByUsername()方法，根据参数username查询用户数据
        UserEntity result = userMapper.findByUsername(username);
        // 判断查询结果是否为null
        if (result == null) {
            // 是：抛出UserNotFoundException异常
            throw new UserNotFoundException("用户不存在");
        }
        // 判断查询结果中的isDelete是否为1
        else if (result.getIsDelete() == 1) {
            // 是：抛出UserNotFoundException异常
            throw new UserNotFoundException("用户已被删除");
        }
        // 从查询结果中获取盐值
        String salt = result.getSalt();
        // 调用getMd5Password()方法，将参数password和salt结合起来进行加密
        String md5Password = getMd5Password(password, salt);
        // 判断查询结果中的密码，与以上加密得到的密码是否不一致
        if (!result.getPassword().equals(md5Password)) {
            // 是：抛出PasswordNotMatchException异常
            throw new PasswordNotMatchException("密码不正确");
        }
        // 创建新的User对象
        UserEntity user = new UserEntity();
        // 将查询结果中的uid、username、avatar封装到新的user对象中
        user.setUid(result.getUid());
        user.setUsername(result.getUsername());
        user.setAvatar(result.getAvatar());
        // 返回新的user对象
        return user;
    }

    @Override
    public void changePassword(Integer uid, String username, String oldPassword, String newPassword) {
        // 调用userMapper的findByUid()方法，根据参数uid查询用户数据
        UserEntity byUid = userMapper.findByUid(uid);
        // 检查查询结果是否为null
        if (byUid == null){
            // 是：抛出UserNotFoundException异常
            throw new UserNotFoundException("该id用户不存在");
        }
        // 检查查询结果中的isDelete是否为1
        else if (byUid.getIsDelete() == 1){
            // 是：抛出UserNotFoundException异常
            throw new UserNotFoundException("该id用户已删除");
        }
        // 从查询结果中取出盐值
        String salt = byUid.getSalt();
        // 将参数oldPassword结合盐值加密，得到oldMd5Password
        String oldMd5Password = getMd5Password(oldPassword, salt);
        // 判断查询结果中的password与oldMd5Password是否不一致
        if (!oldMd5Password.equals(byUid.getPassword())){
            // 是：抛出PasswordNotMatchException异常
            throw new PasswordNotMatchException("原密码错误");
        }
        // 将参数newPassword结合盐值加密，得到newMd5Password
        String newMd5Password = getMd5Password(newPassword, salt);
        // 创建当前时间对象
        Date now = new Date();
        // 调用userMapper的updatePasswordByUid()更新密码，并获取返回值
        Integer integer = userMapper.updatePasswordByUid(uid, newMd5Password, byUid.getUsername(), now);
        // 判断以上返回的受影响行数是否不为1
        if (integer != 1){
            // 是：抛出UpdateException异常
            throw new UpdateException("修改密码时发生未知错误");
        }
    }

    @Override
    public void changeInfo(Integer uid, String username, UserEntity userEntity) {
        // 调用userMapper的findByUid()方法，根据参数uid查询用户数据
        UserEntity byUid = userMapper.findByUid(uid);
        // 判断查询结果是否为null
        if (byUid==null){
            // 是：抛出UserNotFoundException异常
            throw new UserNotFoundException("用户不存在");
        }
        // 判断查询结果中的isDelete是否为1
        else if (byUid.getIsDelete()==1){
            // 是：抛出UserNotFoundException异常
            throw new UserNotFoundException("用户已删除");
        }
        // 向参数user中补全数据：uid
        userEntity.setUid(uid);
        // 向参数user中补全数据：modifiedUser(username)
        userEntity.setModifiedUser(username);
        // 向参数user中补全数据：modifiedTime(new Date())
        userEntity.setModifiedTime(new Date());
        // 调用userMapper的updateInfoByUid(User user)方法执行修改，并获取返回值
        Integer integer = userMapper.updateInfoByUid(userEntity);
        // 判断以上返回的受影响行数是否不为1
        if (integer!=1){
            // 是：抛出UpdateException异常
            throw new UpdateException("修改信息时发生异常");
        }
    }

    @Override
    public UserEntity getByUid(Integer uid) {
        // 调用userMapper的findByUid()方法，根据参数uid查询用户数据
        UserEntity byUid = userMapper.findByUid(uid);
        // 判断查询结果是否为null
        if (byUid==null){
            // 是：抛出UserNotFoundException异常
            throw new UserNotFoundException("用户不存在");
        }
        // 判断查询结果中的isDelete是否为1
        else if (byUid.getIsDelete()==1){
            // 是：抛出UserNotFoundException异常
            throw new UserNotFoundException("用户已删除");
        }
        // 创建新的User对象
        UserEntity userEntity=new UserEntity();
        // 将以上查询结果中的username/phone/email/gender封装到新User对象中
        userEntity.setUsername(byUid.getUsername());
        userEntity.setPhone(byUid.getPhone());
        userEntity.setEmail(byUid.getEmail());
        userEntity.setGender(byUid.getGender());
        // 返回新的User对象
        return userEntity;
    }

    @Override
    public void changeAvatar(Integer uid, String username, String avatar) {
        // 调用userMapper的findByUid()方法，根据参数uid查询用户数据
        UserEntity byUid = userMapper.findByUid(uid);
        // 检查查询结果是否为null
        if (byUid==null){
            // 是：抛出UserNotFoundException
            throw new UserNotFoundException("用户不存在");
        }
        // 检查查询结果中的isDelete是否为1
        if (byUid.getIsDelete()==1){
            // 是：抛出UserNotFoundException
            throw new UserNotFoundException("用户以删除");
        }
        // 创建当前时间对象
        Date date = new Date();
        // 调用userMapper的updateAvatarByUid()方法执行更新，并获取返回值
        Integer integer = userMapper.updateAvatarByUid(uid, avatar, username, date);
        // 判断以上返回的受影响行数是否不为1
        if (integer!=1){
            // 是：抛了UpdateException
            throw new UpdateException("修改时发生未知异常");
        }


    }

    private String getMd5Password(String password, String salt) {
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        return password;
    }
}
