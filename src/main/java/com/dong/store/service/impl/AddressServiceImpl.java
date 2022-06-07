package com.dong.store.service.impl;

import com.dong.store.entity.Address;
import com.dong.store.mapper.AddressMapper;
import com.dong.store.mapper.DistrictMapper;
import com.dong.store.service.IAddressService;
import com.dong.store.service.ex.*;
import com.dong.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private DistrictMapper districtMapper;

    @Value("${user.address.max-count}")
    private Integer maxCount;

    @Override
    public void addNewAddress(Integer uid, String username, Address address) {
        // 根据参数uid调用addressMapper的countByUid()方法，统计当前用户的收货地址数据的数量
        Integer integer = addressMapper.countByUid(uid);
        // 判断数量是否达到上限值
        if (integer >= maxCount) {
            // 是：抛出AddressCountLimitException
            throw new AddressCountLimitException("收货地址达到上限");
        }
        // 补全数据：将参数uid封装到参数address中
        address.setUid(uid);
        // 补全数据：省、市、区的名称
        String provinceName = districtMapper.findNameByCode(address.getProvinceCode());
        String cityName = districtMapper.findNameByCode(address.getCityCode());
        String areaName = districtMapper.findNameByCode(address.getAreaCode());
        address.setProvinceName(provinceName);
        address.setCityName(cityName);
        address.setAreaName(areaName);
        // 补全数据：根据以上统计的数量，得到正确的isDefault值(是否默认：0-不默认，1-默认)，并封装
        int isDefault = integer == 0 ? 1 : 0;
        address.setIsDefault(isDefault);
        // 补全数据：4项日志
        address.setCreatedUser(username);
        address.setCreatedTime(new Date());
        address.setModifiedUser(username);
        address.setModifiedTime(new Date());
        // 调用addressMapper的insert()方法插入收货地址数据，并获取返回的受影响行数
        Integer insert = addressMapper.insert(address);
        // 判断受影响行数是否不为1
        if (insert != 1) {
            // 是：抛出InsertException
            throw new InsertException("插入收货地址数据时出现未知错误，请联系系统管理员！");
        }

    }

    @Override
    public List<Address> getByUid(Integer uid) {
        List<Address> byUid = addressMapper.findByUid(uid);
        for (Address address : byUid
        ) {
            address.setUid(null);
            address.setProvinceCode(null);
            address.setCityCode(null);
            address.setAreaCode(null);
            address.setCreatedUser(null);
            address.setCreatedTime(null);
            address.setModifiedUser(null);
            address.setModifiedTime(null);
        }
        return byUid;
    }

    @Override
    public void setDefault(Integer aid, Integer uid, String username) {
        // 根据参数aid，调用addressMapper中的findByAid()查询收货地址数据
        Address byAid = addressMapper.findByAid(aid);
        // 判断查询结果是否为null
        if (byAid == null) {
            // 是：抛出AddressNotFoundException
            throw new AddressNotFoundException("尝试访问的收货地址数据不存在");
        }
        // 判断查询结果中的uid与参数uid是否不一致(使用equals()判断)
        if (!uid.equals(byAid.getUid())) {
            // 是：抛出AccessDeniedException：非法访问
            throw new AccessDeniedException("非法访问的异常");
        }
        // 调用addressMapper的updateNonDefaultByUid()将该用户的所有收货地址全部设置为非默认，并获取返回的受影响的行数
        Integer integer = addressMapper.updateNonDefaultByUid(uid);
        // 判断受影响的行数是否小于1(不大于0)
        if (integer < 1) {
            // 是：抛出UpdateException
            throw new UpdateException("设置默认收货地址时出现未知错误[1]");
        }
        // 调用addressMapper的updateDefaultByAid()将指定aid的收货地址设置为默认，并获取返回的受影响的行数
        Integer updateDefaultByAid = addressMapper.updateDefaultByAid(aid, username, new Date());
        // 判断受影响的行数是否不为1
        if (updateDefaultByAid != 1) {
            // 是：抛出UpdateException
            throw new UpdateException("设置默认收货地址时出现未知错误[2]");
        }
    }

    @Override
    public void delete(Integer aid, Integer uid, String username) {
        // 根据参数aid，调用findByAid()查询收货地址数据
        Address result = addressMapper.findByAid(aid);
        // 判断查询结果是否为null
        if (result == null) {
            // 是：抛出AddressNotFoundException
            throw new AddressNotFoundException("尝试访问的收货地址数据不存在");
        }

        // 判断查询结果中的uid与参数uid是否不一致(使用equals()判断)
        if (!result.getUid().equals(uid)) {
            // 是：抛出AccessDeniedException：非法访问
            throw new AccessDeniedException("非法访问");
        }

        // 根据参数aid，调用deleteByAid()执行删除
        Integer rows1 = addressMapper.deleteByAid(aid);
        if (rows1 != 1) {
            throw new DeleteException("删除收货地址数据时出现未知错误，请联系系统管理员");
        }

        // 判断查询结果中的isDefault是否为0
        if (result.getIsDefault() == 0) {
            return;
        }

        // 调用持久层的countByUid()统计目前还有多少收货地址
        Integer count = addressMapper.countByUid(uid);
        // 判断目前的收货地址的数量是否为0
        if (count == 0) {
            return;
        }

        // 调用findLastModified()找出用户最近修改的收货地址数据
        Address lastModified = addressMapper.findLastModified(uid);
        // 从以上查询结果中找出aid属性值
        Integer lastModifiedAid = lastModified.getAid();
        // 调用持久层的updateDefaultByAid()方法执行设置默认收货地址，并获取返回的受影响的行数
        Integer rows2 = addressMapper.updateDefaultByAid(lastModifiedAid, username, new Date());
        // 判断受影响的行数是否不为1
        if (rows2 != 1) {
            // 是：抛出UpdateException
            throw new UpdateException("更新收货地址数据时出现未知错误，请联系系统管理员");
        }
    }

    @Override
    public Address getByAid(Integer aid, Integer uid) {
        Address address = addressMapper.findByAid(aid);
        if(address==null){
            throw new AddressNotFoundException("尝试访问的收货地址数据不存在");
        }
        if (!address.getUid().equals(uid)){
            throw new AccessDeniedException("非法访问");
        }
        address.setProvinceCode(null);
        address.setCityCode(null);
        address.setAreaCode(null);
        address.setCreatedUser(null);
        address.setCreatedTime(null);
        address.setModifiedUser(null);
        address.setModifiedTime(null);
        return address;
    }
}
