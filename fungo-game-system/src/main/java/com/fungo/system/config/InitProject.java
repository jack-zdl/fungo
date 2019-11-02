package com.fungo.system.config;

import com.baomidou.mybatisplus.mapper.Condition;
import com.fungo.system.entity.BasFilterword;
import com.fungo.system.service.impl.BasFilterwordService;
import com.game.common.util.SensitiveWordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/10/31
 */
@Component
public class InitProject implements ApplicationRunner {


    @Autowired
    private BasFilterwordService filterService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("-----------启动工程后执行初始化过滤非法关键字------------------------");
        initFilterWord();
    }

    private void initFilterWord() {
        Set<String> filterWord = new HashSet<>();
        List<BasFilterword> wordList = filterService.selectList( Condition.create().setSqlSelect("key_word as keyWord"));
        for (BasFilterword word : wordList) {
            filterWord.add(word.getKeyWord());
        }
        SensitiveWordUtil.init(filterWord);
    }
}
