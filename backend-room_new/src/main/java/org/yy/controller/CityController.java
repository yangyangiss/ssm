package org.yy.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yy.model.City;
import org.yy.service.CityService;
import org.yy.utils.JsonObject;

import java.util.List;

@Controller
@RequestMapping("/v1/citys")
public class CityController {

    @Autowired
    private CityService citySerçvice;

    @RequestMapping("")
    public @ResponseBody Object showUserInfo(){
        JsonObject jsonObject  = new JsonObject();
        List<City> city = null;
        try {
          city = citySerçvice.findCityListByTop();
          jsonObject.setData(city);
        }catch (Exception e){
            jsonObject.setCode(500);
            jsonObject.setMsg(e.toString());
        }
        return jsonObject;
    }

}
