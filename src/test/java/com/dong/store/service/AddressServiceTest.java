package com.dong.store.service;

import com.dong.store.entity.Address;
import com.dong.store.service.ex.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AddressServiceTest {
    @Autowired
    private IAddressService iAddressService;

    @Test
    public void insertTest() {
        try {
            Integer uid = 20;
            String username = "管理员";
            Address address = new Address();
            address.setName("张三");
            address.setPhone("17858805555");
            address.setAddress("雁塔区小寨华旗");
            iAddressService.addNewAddress(uid, username, address);
            System.out.println("ok");
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void getByUid() {
        List<Address> byUid = iAddressService.getByUid(12);
        for (Address address : byUid
        ) {
            System.out.println(address);
        }
    }

    @Test
    public void setDefaultTest() {
        iAddressService.setDefault(11, 12, "user014");
    }

    @Test
    public void delete() {
        iAddressService.delete(3, 20, "xie");
    }
    @Test
    public void getByAid(){
        Address byAid = iAddressService.getByAid(10, 12);
        System.out.println(byAid);
    }
}
