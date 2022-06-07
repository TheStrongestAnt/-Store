package com.dong.store.mapper;


import com.dong.store.entity.UserEntity;
import org.apache.ibatis.annotations.*;

import java.util.Date;

public interface UserMapper {

    /**
     * 插入用户数据
     *
     * @param userEntity 用户数据
     * @return 受影响行数
     */
//    @Insert("INSERT INTO t_user (username,password,salt,phone,email,gender,avatar,is_delete,created_user,created_time,modified_user,modified_time) VALUES(#{username},#{password},#{salt},#{phone},#{email},#{gender},#{avatar},#{isDelete},#{createdUser},#{createdTime},#{modifiedUser},#{modifiedTime})")
//    @Insert("INSERT INTO test VALUES(#{},#{},#{})")
    Integer insert(UserEntity userEntity);

//        @Select("SELECT * FROM t_user WHERE username = #{username}")
//     @Results(id = "resultMap", value = {
//     @Result(property = "isDelete", column = "is_delete"),
//     @Result(property = "createdUser", column = "created_user"),
//     @Result(property = "createdTime", column = "created_time"),
//     @Result(property = "modifiedUser", column = "modified_user"),
//     @Result(property = "modifiedTime", column = "modified_time")
//     })

    /**
     * 根据用户名查询用户数据
     *
     * @param username 用户名
     * @return 匹配的用户数据，如果没有匹配的数据，则返回null
     */
    UserEntity findByUsername(String username);

    /**
     * 通过用户id修改密码
     *
     * @param uid          用户id
     * @param password     用户密码
     * @param modifiedUser 最后修改执行人
     * @param modifiedTime 最后修改时间
     * @return 受影响行数
     */
//    @Update("UPDATE t_user set password=#{password},modified_user=#{modifiedUser},modified_time=#{modifiedTime} WHERE uid=#{uid}")
    Integer updatePasswordByUid(
            @Param("uid") Integer uid,
            @Param("password") String password,
            @Param("modifiedUser") String modifiedUser,
            @Param("modifiedTime") Date modifiedTime);

    /**
     * 通过用户id查询用户数据
     *
     * @param uid 用户id
     * @return 匹配的用户数据，如果没有匹配的用户数据，则返回null
     */
//    @Select("SELECT * FROM t_user WHERE uid=#{uid}")
//    @ResultMap("resultMap")
    UserEntity findByUid(Integer uid);

    /**
     * 通过用户id修改用户个人资料
     *
     * @param userEntity 用户数据
     * @return 受影响行数
     */
//    @UpdateProvider(method = "updateByPrimaryKeySelective", type = UserSqlProvider.class)
    Integer updateInfoByUid(UserEntity userEntity);

    /**
     * 通过uid更改用户头像
     * @param uid 用户id
     * @param avatar 用户上传头像路径
     * @param modifiedUser 最后修改人
     * @param modifiedTime 最后修改时间
     * @return 受影响行数
     */
//    @Update("UPDATE t_user SET avatar=#{avatar},modified_user=#{modifiedUser},modified_time=#{modifiedTime} WHERE uid=#{uid}")
    Integer updateAvatarByUid(
            @Param("uid") Integer uid,
            @Param("avatar") String avatar,
            @Param("modifiedUser") String modifiedUser,
            @Param("modifiedTime") Date modifiedTime
    );
}
