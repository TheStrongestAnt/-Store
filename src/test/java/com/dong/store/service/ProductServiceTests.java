package com.dong.store.service;

import com.dong.store.entity.Product;
import com.dong.store.service.ex.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductServiceTests {
    @Autowired
    private IProductService productService;
    @Test
    public void findHotList() {
        try {
            List<Product> list = productService.findHotList();
            System.out.println("count=" + list.size());
            for (Product item : list) {
                System.out.println(item);
            }
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void findById() {
        try {
            Integer id = 10000017;
            Product result = productService.findById(id);
            System.out.println(result);
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
}
