package com.fungo.system.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>
 *    系统index controller
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Controller
public class SystemDefaultController {

    @RequestMapping(value = "/api/sys/index", method = {RequestMethod.POST, RequestMethod.GET})
    public String index() throws Exception {

        return "index";
    }
    
}
