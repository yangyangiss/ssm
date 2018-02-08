package org.yy.service;

import org.yy.model.City;

import java.util.List;

/**
 * 城市服务接口
 * @author yangyang
 * @create 2018-02-08 下午2:57
 **/
public interface CityService {

    List<City> findCityListByTop();

}
