package com.dong.store.controller;

import com.dong.store.service.ICartService;
import com.dong.store.util.JsonResult;
import com.dong.store.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("carts")
public class CartController extends BaseController{
    @Autowired
    private ICartService cartService;

    @RequestMapping("add_to_cart")
    JsonResult<Void> addToCart(Integer pid, Integer amount, HttpSession session){
        // 从Session中获取uid和username
        Integer uid = getUidFromSession(session);
        String username = getUserNameFromSession(session);
        // 调用业务对象执行添加到购物车
        cartService.addToCart(uid,pid,amount,username);
        // 返回成功
        return new JsonResult<>(OK);
    }

    @RequestMapping({"","/"})
    JsonResult<List<CartVO>> getVOByUid(HttpSession session){
        Integer uid = getUidFromSession(session);
        List<CartVO> voByUid = cartService.findVOByUid(uid);
        return new JsonResult<>(OK,voByUid);
    }

    @RequestMapping("{cid}/num/add")
    JsonResult<Integer> addNum(@PathVariable("cid") Integer cid, HttpSession session){
        Integer uid = getUidFromSession(session);
        String username = getUserNameFromSession(session);
        Integer num = cartService.addNum(cid, uid, username);
        return new JsonResult<>(OK,num);
    }
    @RequestMapping("{cid}/num/reduce")
    JsonResult<Integer> reduceNum(@PathVariable("cid") Integer cid, HttpSession session){
        Integer uid = getUidFromSession(session);
        String username = getUserNameFromSession(session);
        Integer num = cartService.reduceNum(cid, uid, username);
        return new JsonResult<>(OK,num);
    }

    @GetMapping("list")
    JsonResult<List<CartVO>> getVOByCids(Integer[] cids, HttpSession session){
        Integer uid = getUidFromSession(session);
        List<CartVO> voByCids = cartService.getVOByCids(uid, cids);
        return new JsonResult<>(OK,voByCids);
    }
}
