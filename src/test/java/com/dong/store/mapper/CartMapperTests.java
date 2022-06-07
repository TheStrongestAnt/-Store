package com.dong.store.mapper;

import com.dong.store.entity.Cart;
import com.dong.store.vo.CartVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CartMapperTests {
    @Autowired
    private CartMapper cartMapper;

    @Test
    public void insert() {
        Cart cart = new Cart();
        cart.setUid(1);
        cart.setPid(2);
        cart.setNum(3);
        cart.setPrice(4L);
        Integer rows = cartMapper.insert(cart);
        System.out.println("rows=" + rows);
    }

    @Test
    public void updateNumByCid() {
        Integer cid = 1;
        Integer num = 10;
        String modifiedUser = "购物车管理员";
        Date modifiedTime = new Date();
        Integer rows = cartMapper.updateNumByCid(cid, num, modifiedUser, modifiedTime);
        System.out.println("rows=" + rows);
    }

    @Test
    public void findByUidAndPid() {
        Integer uid = 1;
        Integer pid = 2;
        Cart result = cartMapper.findByUidAndPid(uid, pid);
        System.out.println(result);
    }
    @Test
    public void findVOByUid() {
        List<CartVO> voByUid = cartMapper.findVOByUid(12);
        for (CartVO cartVO:voByUid
             ) {
            System.out.println(cartVO);
        }
    }
    @Test
    public void findByCid() {
        Cart byCid = cartMapper.findByCid(3);
        System.out.println(byCid);
    }
    @Test
    public void findBycids() {
        Integer[] cids={3,9};
        List<CartVO> voByCids = cartMapper.findVOByCids(cids);
        for (CartVO cartVO:voByCids
             ) {
            System.out.println(cartVO);
        }
    }
}
