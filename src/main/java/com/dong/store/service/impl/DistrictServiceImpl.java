package com.dong.store.service.impl;

import com.dong.store.entity.District;
import com.dong.store.mapper.DistrictMapper;
import com.dong.store.service.IDistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DistrictServiceImpl implements IDistrictService {
    @Autowired
    private DistrictMapper districtMapper;
    @Override
    public List<District> getByParent(String parent) {
        List<District> byParent = districtMapper.findByParent(parent);
        for (District d:byParent
             ) {
            d.setId(null);
            d.setParent(null);
        }
        return byParent;
    }

    @Override
    public String getNameByCode(String code) {
        String nameByCode = districtMapper.findNameByCode(code);
        return nameByCode;
    }
}
