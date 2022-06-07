package com.dong.store.controller;

import com.dong.store.entity.Order;
import com.dong.store.service.IOrderService;
import com.dong.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("orders")
public class OrderController extends BaseController{
    @Autowired
    private IOrderService orderService;

    @RequestMapping("create")
    JsonResult<Order> create (HttpSession session,Integer aid, Integer[] cids){
        Integer uid = getUidFromSession(session);
        String username = getUserNameFromSession(session);
        Order order = orderService.create(aid, cids, uid, username);
        return new JsonResult<>(OK,order);
    }
}
