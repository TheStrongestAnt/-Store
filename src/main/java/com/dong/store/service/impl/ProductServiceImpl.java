package com.dong.store.service.impl;

import com.dong.store.entity.Product;
import com.dong.store.mapper.ProductMapper;
import com.dong.store.service.IProductService;
import com.dong.store.service.ex.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Override
    public List<Product> findHotList() {
        List<Product> hotList = productMapper.findHotList();
        for (Product product: hotList
             ) {
            product.setPriority(null);
            product.setCreatedUser(null);
            product.setCreatedTime(null);
            product.setModifiedUser(null);
            product.setModifiedTime(null);
        }
        return hotList;
    }

    @Override
    public Product findById(Integer id) {
        // 根据参数id调用私有方法执行查询，获取商品数据
        Product byId = productMapper.findById(id);
        // 判断查询结果是否为null
        if (byId==null){
            // 是：抛出ProductNotFoundException
            throw new ProductNotFoundException("尝试访问的商品数据不存在");
        }
        // 将查询结果中的部分属性设置为null
        byId.setPriority(null);
        byId.setCreatedUser(null);
        byId.setCreatedTime(null);
        byId.setModifiedUser(null);
        byId.setModifiedTime(null);
        // 返回查询结果
        return byId;
    }
}
