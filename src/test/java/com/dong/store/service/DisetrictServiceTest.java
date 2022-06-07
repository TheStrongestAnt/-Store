package com.dong.store.service;

import com.dong.store.entity.District;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DisetrictServiceTest {
    @Autowired
    private IDistrictService districtService;
    @Test
    public void insertTest(){
        List<District> byParent = districtService.getByParent("86");
        for (District d:byParent
             ) {
            System.out.println(d.getCode());
        }
    }
    @Test
    public void findNameByCodeTest(){
        String nameByCode = districtService.getNameByCode("440100");
        System.out.println(nameByCode);
    }
}
