package com.dong.store.service.impl;

import com.dong.store.entity.Cart;
import com.dong.store.entity.Product;
import com.dong.store.mapper.CartMapper;
import com.dong.store.mapper.ProductMapper;
import com.dong.store.service.ICartService;
import com.dong.store.service.ex.AccessDeniedException;
import com.dong.store.service.ex.CartNotFoundException;
import com.dong.store.service.ex.InsertException;
import com.dong.store.service.ex.UpdateException;
import com.dong.store.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public
class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public void addToCart(Integer uid, Integer pid, Integer amount, String username) {
        // 根据参数pid和uid查询购物车中的数据
        Cart byUidAndPid = cartMapper.findByUidAndPid(uid, pid);
        Date date = new Date();
        // 判断查询结果是否为null
        if (byUidAndPid==null){
            // 是：表示该用户并未将该商品添加到购物车
            // -- 创建Cart对象
            Cart cart = new Cart();
            // -- 封装数据：uid,pid,amount
            cart.setUid(uid);
            cart.setPid(pid);
            cart.setNum(amount);
            // -- 调用productService.findById(pid)查询商品数据，得到商品价格
            Product product = productMapper.findById(pid);
            Long price = product.getPrice();
            // -- 封装数据：price
            cart.setPrice(price);
            // -- 封装数据：4个日志
            cart.setCreatedUser(username);
            cart.setCreatedTime(date);
            cart.setModifiedUser(username);
            cart.setModifiedTime(date);
            // -- 调用insert(cart)执行将数据插入到数据表中
            Integer rows = cartMapper.insert(cart);
            if (rows!=1){
                throw new UpdateException("插入商品数据时出现未知错误，请联系系统管理员");
            }
        }
        // 否：表示该用户的购物车中已有该商品
        else {
            // -- 从查询结果中获取购物车数据的id
            Integer cid = byUidAndPid.getCid();
            // -- 从查询结果中取出原数量，与参数amount相加，得到新的数量
            Integer num = byUidAndPid.getNum();
            int newNum = num + amount;
            // -- 执行更新数量
            Integer rows = cartMapper.updateNumByCid(cid, newNum, username, date);
            if (rows!=1){
                throw new InsertException("修改商品数量时出现未知错误，请联系系统管理员");
            }
        }
    }

    @Override
    public List<CartVO> findVOByUid(Integer uid) {
        List<CartVO> voByUid = cartMapper.findVOByUid(uid);
        return voByUid;
    }

    @Override
    public Integer addNum(Integer cid, Integer uid, String username) {
        // 调用findByCid(cid)根据参数cid查询购物车数据
        Cart byCid = cartMapper.findByCid(cid);
        // 判断查询结果是否为null
        if (byCid==null){
            // 是：抛出CartNotFoundException
            throw new CartNotFoundException("尝试访问的购物车数据不存在");
        }
        // 判断查询结果中的uid与参数uid是否不一致
        if (byCid.getUid()!=uid){
            // 是：抛出AccessDeniedException
            throw new AccessDeniedException("非法访问");
        }
        // 可选：检查商品的数量是否大于多少(适用于增加数量)或小于多少(适用于减少数量)
        // 根据查询结果中的原数量增加1得到新的数量num
        int newNum = byCid.getNum() + 1;
        // 创建当前时间对象，作为modifiedTime
        Date date = new Date();
        // 调用updateNumByCid(cid, num, modifiedUser, modifiedTime)执行修改数量
        Integer integer = cartMapper.updateNumByCid(cid, newNum, username, date);
        if (integer!=1){
            throw new UpdateException("增加商品数量时发生异常");
        }
        return newNum;
    }

    @Override
    public Integer reduceNum(Integer cid, Integer uid, String username) {
        // 调用findByCid(cid)根据参数cid查询购物车数据
        Cart byCid = cartMapper.findByCid(cid);
        // 判断查询结果是否为null
        if (byCid==null){
            // 是：抛出CartNotFoundException
            throw new CartNotFoundException("尝试访问的购物车数据不存在");
        }
        // 判断查询结果中的uid与参数uid是否不一致
        if (byCid.getUid()!=uid){
            // 是：抛出AccessDeniedException
            throw new AccessDeniedException("非法访问");
        }
        // 可选：检查商品的数量是否大于多少(适用于增加数量)或小于多少(适用于减少数量)
        // 根据查询结果中的原数量增加1得到新的数量num
        int newNum = byCid.getNum() - 1;
        // 创建当前时间对象，作为modifiedTime
        Date date = new Date();
        // 调用updateNumByCid(cid, num, modifiedUser, modifiedTime)执行修改数量
        Integer integer = cartMapper.updateNumByCid(cid, newNum, username, date);
        if (integer!=1){
            throw new UpdateException("减少商品数量时发生异常");
        }
        return newNum;
    }

    @Override
    public List<CartVO> getVOByCids(Integer uid, Integer[] cids) {
        List<CartVO> voByCids = cartMapper.findVOByCids(cids);
        /*for (CartVO cartVO:voByCids
             ) {
            if (cartVO.getUid().equals(uid)){
                voByCids.remove(cartVO);
            }
        }*/
        voByCids.removeIf(cart -> !cart.getUid().equals(uid));
        return voByCids;
    }
}
