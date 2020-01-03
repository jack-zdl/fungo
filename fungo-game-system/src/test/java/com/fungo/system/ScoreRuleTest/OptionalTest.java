package com.fungo.system.ScoreRuleTest;

import com.alibaba.fastjson.JSON;
import com.fungo.system.entity.Member;
import org.apache.catalina.User;
import org.junit.Test;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.*;

import static org.junit.Assert.assertEquals;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/20
 */

public class OptionalTest {

    @Test
    public void  test() {
        System.out.println("----------------");
        Member user = new Member();
        user.setId( "111111111111111" );
        Member user2 = new Member();
        user2.setId( "222222222222222222" );
        Member result = Optional.ofNullable(user).orElse(user2);

        assertEquals("111111111111111", result.getId());
    }

    @Test
    public void leambdaTest(){
        PrintStream ps = System.out;
        Consumer<String> con = (str) -> ps.println(str);
        con.accept("Hello World！");

        System.out.println("--------------------------------");

        Consumer<String> con2 = ps::println;
        con2.accept("Hello Java8！");

        Consumer<String> con3 = System.out::println;
    }


    @Test
    public void test3(){
        BiFunction<Double, Double, Double> fun = (x, y) -> Math.max(x, y);
        System.out.println(fun.apply(1.5, 22.2));

        System.out.println("--------------------------------------------------");
        BiFunction<Double, Double, Double> fun2 = Math::max;
        System.out.println(fun2.apply(1.5, 22.2));
    }

    @Test
    public void test1(){
        happy(10000, (m) -> System.out.println("你们刚哥喜欢大宝剑，每次消费：" + m + "元"));

        List<Integer> numList = getNumList(10, () -> (int)(Math.random() * 100));
        numList.stream().forEach( s -> {
            happy( s,(m) -> System.out.println("aaaa"+m) );
        } );
        String newStr = strHandler("\t\t\t 我大尚硅谷威武 ", (str) -> str.trim());
        System.out.println(newStr);

        List<String> list = Arrays.asList("Hello", "atguigu", "Lambda", "www", "ok");
        List<String> strList = filterStr(list, (s) -> s.length() > 3);
        System.out.println("----"+ JSON.toJSONString(strList));

    }


    public void happy(double money, Consumer<Double> con){
        con.accept(money);
    }

    //需求：产生指定个数的整数，并放入集合中
    public List<Integer> getNumList(int num, Supplier<Integer> sup){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Integer n = sup.get();
            list.add(n);
        }
        return list;
    }

    //需求：用于处理字符串
    public String strHandler(String str, Function<String, String> fun){
        return fun.apply(str);
    }

    //需求：将满足条件的字符串，放入集合中
    public List<String> filterStr(List<String> list, Predicate<String> pre){
        List<String> strList = new ArrayList<>();
        for (String str : list) {
            if(pre.test(str)){
                strList.add(str);
            }
        }
        return strList;
    }
}
