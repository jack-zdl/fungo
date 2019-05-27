package com.fungo.system.controller.portal;

import com.fungo.system.service.BannerService;
import com.game.common.repo.cache.facade.FungoCacheAdvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 *  PC端门户-首页
 * @author mxf
 * @update 2019/5/5 16:26
 */
@RestController
public class PortalSystemController {


    @Autowired
    private BannerService bannerService;

    @Autowired
    private FungoCacheAdvert fungoCacheAdvert;



}
