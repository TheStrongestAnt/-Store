package com.dong.store.controller;

import com.dong.store.entity.District;
import com.dong.store.service.IDistrictService;
import com.dong.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("districts")
public class DistrictController extends BaseController{
    @Autowired
    private IDistrictService districtService;
    @GetMapping({"","/"})
    public JsonResult<List<District>> getByParent(String parent){
        List<District> byParent = districtService.getByParent(parent);
        return new JsonResult<>(OK,byParent);
    }
}
