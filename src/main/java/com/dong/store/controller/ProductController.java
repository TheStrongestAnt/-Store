package com.dong.store.controller;

import com.dong.store.entity.Product;
import com.dong.store.service.IProductService;
import com.dong.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController extends BaseController{
    @Autowired
    private IProductService productService;

    @RequestMapping("hot_list")
    JsonResult<List<Product>> getHotList(){
        List<Product> hotList = productService.findHotList();
        return new JsonResult<>(OK,hotList);
    }
    @RequestMapping("{id}/details")
    JsonResult<Product> getById(@PathVariable("id") Integer id){
        // 调用业务对象执行获取数据
        Product byId = productService.findById(id);
        // 返回成功和数据
        return new JsonResult<>(OK,byId);
    }
}
