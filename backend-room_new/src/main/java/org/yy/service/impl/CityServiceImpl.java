package org.yy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yy.dao.CityMapper;
import org.yy.model.City;
import org.yy.service.CityService;

import java.util.List;

/**
 * 城市服务接口实现类
 * @author yangyang
 * @create 2018-02-08 下午2:59
 **/
@Service("cityService")
public class CityServiceImpl  implements CityService{

    @Autowired
    public CityMapper cityMapper;

    public List<City> findCityListByTop() {

        return cityMapper.findCityListByTop();
    }
}
