package com.fungo.system.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能说明: <br>
 * 系统版本: 2.0 <br>
 * 开发人员: zhangdl <br>
 * 开发时间: 2018/3/1 17:01<br>
 */

@RestController
@RequestMapping("/template")
public class TemplateController {

//    @Autowired
//    @Qualifier("otherTemplateServiceImpl")
//    private TemplateService otherTemplateServiceImpl;
//
//    @Autowired
//    @Qualifier("templateServiceImpl")
//    private TemplateService templateServiceImpl;

    @PostMapping(value = "/save")
    public String save() {
//        RespJson respJson = otherTemplateServiceImpl.save();
//        RespJson respJson1 = templateServiceImpl.save();
        return "hello "+"，this is biz";
    }

}
