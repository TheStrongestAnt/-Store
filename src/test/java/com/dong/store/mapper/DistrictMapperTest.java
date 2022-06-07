package com.dong.store.mapper;

import com.dong.store.entity.District;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DistrictMapperTest {
    @Autowired
    private DistrictMapper districtMapper;
    @Test
    public void findByParent(){
        List<District> byParent = districtMapper.findByParent("86");
        for (District d:byParent
             ) {
            System.out.println(d);
        }
    }
    @Test
    public void findNameByCode(){
        String nameByCode = districtMapper.findNameByCode("440100");
        System.out.println(nameByCode);
    }
}
