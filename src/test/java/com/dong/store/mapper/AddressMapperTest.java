package com.dong.store.mapper;

import com.dong.store.entity.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class AddressMapperTest {
    @Autowired
    private AddressMapper addressMapper;

    @Test
    public void insertTest(){
        Address address=new Address();
        address.setUid(18);
        address.setName("admin");
        address.setPhone("17858802974");
        address.setAddress("雁塔区小寨赛格");
        Integer insert = addressMapper.insert(address);
        System.out.println(insert);
    }
    @Test
    public void countByUidTest(){
        Integer integer = addressMapper.countByUid(18);
        System.out.println(integer);
    }
    @Test
    public void findByUidTest(){
        List<Address> byUid = addressMapper.findByUid(12);
        for (Address a:byUid
             ) {
            System.out.println(a);
        }
    }
    @Test
    public void updateNonDefaultByUid() {
        Integer uid = 20;
        Integer rows = addressMapper.updateNonDefaultByUid(uid);
        System.out.println("rows=" + rows);
    }
    @Test
    public void updateDefaultByAid() {
        Integer aid = 11;
        String modifiedUser = "管理员";
        Date modifiedTime = new Date();
        Integer rows = addressMapper.updateDefaultByAid(aid, modifiedUser, modifiedTime);
        System.out.println("rows=" + rows);
    }
    @Test
    public void findByAid() {
        Integer aid = 11;
        Address result = addressMapper.findByAid(aid);
        System.out.println(result);
    }
    @Test
    public void deleteByAid() {
        Integer aid = 11;
        Integer integer = addressMapper.deleteByAid(aid);
        System.out.println(integer);
    }
    @Test
    public void findLastModified() {
        Integer uid = 12;
        Address result = addressMapper.findLastModified(uid);
        System.out.println(result);
    }
}
