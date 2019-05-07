package com.fungo.games.controller;



import com.fungo.games.service.IGameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 辅助 计数器
 * @Author lyc
 * @create 2019/5/7 11:48
 */
@RestController
@Api(value = "", description = "计数器")
public class UpdateCountorController {

    @Autowired
    private IGameService iGameService;


    @ApiOperation(value = "更新计数器", notes = "")
    @RequestMapping(value = "/api/update/counter", method = RequestMethod.POST)
    public Boolean updateCounter(@RequestBody Map<String,String> map) {
//        根据表名(动态)修改
        return iGameService.updateCountor(map);
    }
}
