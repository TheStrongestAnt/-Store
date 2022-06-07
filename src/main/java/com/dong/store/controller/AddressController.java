package com.dong.store.controller;

import com.dong.store.entity.Address;
import com.dong.store.service.IAddressService;
import com.dong.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("addresses")
public class AddressController extends BaseController {
    @Autowired
    private IAddressService addressService;

    @RequestMapping("add_new_address")
    JsonResult<Void> addNewAddress(HttpSession session, Address address) {
        // 从Session中获取uid和username
        Integer uid = getUidFromSession(session);
        String username = getUserNameFromSession(session);
        // 调用业务对象的方法执行业务
        addressService.addNewAddress(uid, username, address);
        // 响应成功
        return new JsonResult<>(OK);
    }

    @RequestMapping({"", "/"})
    JsonResult<List<Address>> getByUid(HttpSession session) {
        Integer uid = getUidFromSession(session);
        List<Address> byUid = addressService.getByUid(uid);
        return new JsonResult<>(OK, byUid);
    }
    @RequestMapping("{aid}/set_default")
    JsonResult<Void> setDefault(
            HttpSession session,
            @PathVariable("aid") Integer aid
    ) {
        Integer uidFromSession = getUidFromSession(session);
        String userNameFromSession = getUserNameFromSession(session);
        addressService.setDefault(aid,uidFromSession,userNameFromSession);
        return new JsonResult<>(OK);
    }
    @RequestMapping("{aid}/delete")
    JsonResult<Void>delete(@PathVariable("aid") Integer aid,HttpSession session){
        Integer uid = getUidFromSession(session);
        String username = getUserNameFromSession(session);
        addressService.delete(aid,uid,username);
        return new JsonResult<>(OK);
    }
}
