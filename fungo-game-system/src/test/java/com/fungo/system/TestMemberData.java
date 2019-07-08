package com.fungo.system;

import com.baomidou.mybatisplus.annotations.TableId;
import com.fungo.system.controller.MemberDataController;
import com.fungo.system.dao.BasVideoJobDao;
import com.fungo.system.entity.BasVideoJob;
import com.fungo.system.service.IVdService;
import com.fungo.system.service.impl.VdServiceImpl;
import com.game.common.dto.StreamInfo;
import org.junit.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * <p>用户徽章测试类</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/7
 */
//////Junit4运行环境
//@RunWith(SpringJUnit4ClassRunner.class)
//////单元测试时需要执行的SpringBoot启动类
//@SpringBootTest(classes = FungoGameSystemApplication.class)
// junit 5 运行
//@ExtendWith(SpringExtension.class) //导入spring测试框架[2]
//@SpringBootTest  //提供spring依赖注入
//@Transactional  //事务管理，默认回滚,如果配置了多数据源记得指定事务管理器
//@DisplayName("TestMemberData")
public class TestMemberData {

//    @Mock
//    MyDatabase databaseMock;

//    @Rule
//    public MockitoRule mockitoRule = MockitoJUnit.rule();

    //在所有测试方法前执行一次，一般在其中写上整体初始化的代码
//    @BeforeClass
//   public static void initAll() {
//    }
//
//    //在所有测试方法后执行一次，一般在其中写上销毁和释放资源的代码
//    @AfterClass
//   public static void init() {
//    }
//
//    @Before
//    public void before(){
//
//    }
//
//    //正则表达式
//    @After
//    public void after(){
//        assertEquals("结果", "结果");
//        // 创建正则表达式
//        Pattern pattern = Pattern.compile("([^0]\\d{0,3})-((0[1-9])|(1[12]))");
//        // 获取 Matcher 对象
//        Matcher matcher = pattern.matcher("2018-01");
//        // 是否匹配成功
//        System.out.println(matcher.matches()); // true
//    }
//
//    @Ignore //忽略的测试方法
//    @Tag("failingTest")
//    @Test() //timeout = 1000,expected = Exception.class
//    @DisplayName("failingTest")  // 自定义名称
//    public void failingTest() {
//        List mockList = Mockito.mock(List.class);
//        mockList.add("1");
//        Mockito.when(mockList.get(0)).thenReturn("helloworld");
//        System.out.println(mockList.get(0));//返回null，说明并没有调用真正的方法
//        //验证方法调用(是否调用了get(0))
//        Mockito.verify(mockList).get(0);
//        String result = (String) mockList.get(0);
//        //junit测试
//        Assert.assertEquals("helloworld", result);

//        Mockito.when(mockList.size()).thenReturn(100);//stub
//        System.out.println(mockList.size());//size() method was stubbed，返回100
//
//        //test for Spy
//        List list = new LinkedList();
//        List spy = Mockito.spy(list);
//
//        //optionally, you can stub out some methods:
//        Mockito.when(spy.size()).thenReturn(100);
//
//        //using the spy calls real methods
//        spy.add("one");
//        spy.add("two");
//
//        //prints "one" - the first element of a list
//        System.out.println(spy.get(0));
//
//        //size() method was stubbed - 100 is printed
//        System.out.println(spy.size());
//        fail("a failing test");
//    }

//    @Ignore //忽略的测试方法
//    @ParameterizedTest
//    @ValueSource(ints = { 1, 2, 3 })
//    void testWithValueSource(int argument) {
//        assertTrue(argument > 0 && argument < 4);
//    }

//    @Ignore //忽略的测试方法
//    @ParameterizedTest
//    @EnumSource(TimeUnit.class)
//    void testWithEnumSource(TimeUnit timeUnit) {
//        assertNotNull(timeUnit);
//    }

//    @Ignore //忽略的测试方法
//    @ParameterizedTest
//    @MethodSource("stringProvider")
//    void testWithSimpleMethodSource(String argument) {
//        assertNotNull(argument);
//    }

    static Stream<String> stringProvider() {
        return Stream.of("foo", "bar");
    }

//    @Ignore //忽略的测试方法
//    @ParameterizedTest
//    @CsvSource({ "foo, 1", "bar, 2", "'baz, qux', 3" })
//    void testWithCsvSource(String first, int second) {
//        assertNotNull(first);
//        assertNotEquals(0, second);
//    }

//    @Ignore //忽略的测试方法
//    @ParameterizedTest
//    @CsvFileSource(resources = "/two-column.csv", numLinesToSkip = 1)
//    void testWithCsvFileSource(String first, int second) {
//        assertNotNull(first);
//        assertNotEquals(0, second);
//    }

//    @Autowired
//    private MemberDataController memberDataController;

//    @Ignore //忽略的测试方法

//    @Autowired
//    private VdServiceImpl iVdServiceImpl;
//
//    @Autowired
//    private BasVideoJobDao basVideoJobDao;

//    @Test
    public void testExportExcel(){
//        BasVideoJob job = new BasVideoJob();
//        job.setId("b82f6050597b4a6b826d4292afb4c4e9");
//        job = basVideoJobDao.selectById(job);
//        try {
//            iVdServiceImpl.updateUrlAndState(job,"http://outin-d8556079df3311e89ef400163e1c8a9c.oss-cn-beijing.aliyuncs.com/d08ebf1e9132480582eb96ff8dfcee27/10d746f128b24c4ab2e68ced7acbef4f-c8418229b79e9942748b767fae8dcef3-sd.mp4",
//                    Arrays.asList(new StreamInfo()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}


//class ThreadTestForInterrupt extends Thread{
//    volatile boolean isrun = true; // 标志位是 Thread 的子类字段
//    String name;
//    public ThreadTestForInterrupt(String name) {
//        this.name = name;
//    }
//    @Override
//    public void run() {
//        while (this.isrun && !isInterrupted()) {
//            try {
////                Thread.sleep(1000);
//                System.out.println("我是线程：" + this.name);
//            } catch (InterruptedException e) {
//                System.out.println("中断");
//                // 要添加 终止 break
//                break;
//            }
//        }
//        System.out.println("线程" + this.name + "结束");
//    }
//}
