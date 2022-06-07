package com.dong.store.service;

import com.dong.store.service.ex.ServiceException;
import com.dong.store.vo.CartVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CartServiceTests {
    @Autowired
    private ICartService cartService;

    @Test
    public void addToCart() {
        try {
            Integer uid = 2;
            Integer pid = 10000007;
            Integer amount = 1;
            String username = "Tom";
            cartService.addToCart(uid, pid, amount, username);
            System.out.println("OK.");
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void findVOByUid() {
        List<CartVO> voByUid = cartService.findVOByUid(12);
        System.out.println(voByUid);
    }
    @Test
    public void addNum() {
        try {
            Integer cid = 4;
            Integer uid = 12;
            String username = "管理员";
            Integer num = cartService.addNum(cid, uid, username);
            System.out.println("OK. New num=" + num);
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void reduceNum() {
        try {
            Integer cid = 4;
            Integer uid = 12;
            String username = "管理员";
            Integer num = cartService.reduceNum(cid, uid, username);
            System.out.println("OK. New num=" + num);
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void getVOByCids() {
        Integer[] cids={3,4};
        List<CartVO> voByCids = cartService.getVOByCids(12, cids);
        for (CartVO cartVO:voByCids
             ) {
            System.out.println(cartVO);
        }
    }
}
