package com.dong.store;

import com.dong.store.entity.UserEntity;
import com.dong.store.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@SpringBootTest
@MapperScan("com.cy.store.mapper")
class SpringbootProjectApplicationTests {
    @Autowired //自动装配
    private DataSource dataSource;

    private UserMapper userMapper;


    @BeforeEach
    public void before() throws IOException {
        //加载核心配置文件
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        //获得sqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        //获得sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        userMapper = sqlSession.getMapper(UserMapper.class);

    }


    @Test
    void contextLoads() {
    }

    @Test
    void getConnection() throws SQLException {
        System.out.println(dataSource.getConnection());
    }

    @Test
    void testSave() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("谢曾");
        userEntity.setPassword("123456");

        Integer insert = userMapper.insert(userEntity);
        System.out.println(insert);
    }

    @Test
    public void insert() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user02");
        userEntity.setPassword("123456");
        Integer rows = userMapper.insert(userEntity);
        System.out.println("rows=" + rows);
    }

    @Test
    public void findByUsername() {
        String username = "user01";
        UserEntity result = userMapper.findByUsername(username);
        System.out.println(result);
    }

}
