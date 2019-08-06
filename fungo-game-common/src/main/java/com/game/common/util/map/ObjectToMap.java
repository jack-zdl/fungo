package com.game.common.util.map;

import java.lang.reflect.Field;
import java.util.*;


/**
 *  对象转map
 *  ysx
 */
public class ObjectToMap{

    /**
     *  缓存要转化为Map属性的类的字段信息
     */
    private static  final Map<String,List<Field>> objFieldsCatch = new HashMap<>();
    /**
     *  字段缓存map key前缀 防止其他项目或jar使用相同字符串作为锁发生互斥
     */
    private static final String keyPrefix = "objectToMap-";

    private ObjectToMap(){}


    /**
     * 对象属性转map
     * @param sourceObject 要转化为map的对象
     * @param stopClass 要停止在对象父类的层级，不传或传递的非父类则默认Object 即只会转化继承结构中Object类以下的属性
     * @return 转化后的map
     */
    public static Map<String,Object> objectToMap(Object sourceObject,Class<?> stopClass){
        if(sourceObject==null){
            return new HashMap<>(2);
        }
        if(stopClass == null ||!stopClass.isAssignableFrom(sourceObject.getClass())){
            stopClass = Object.class;
        }
        List<Field> fields = getAllDeclaredField(sourceObject.getClass(),stopClass);
        HashMap<String, Object> resultMap = new HashMap<>(fields.size());
        for (Field field : fields) {
            field.setAccessible(true);
            MapKeyMapping annotation = field.getAnnotation(MapKeyMapping.class);
            //添加了忽略属性，属性忽略转为map
            if(annotation!=null&&annotation.ignore()){
                continue;
            }
            //获取map key 默认为属性名
            String key = field.getName();
            if(annotation!=null&&!"".equals(annotation.value())){
                key = annotation.value();
            }

            try {
                resultMap.put(key,field.get(sourceObject));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    /**
     * 递归获取指定类的所有成员变量，允许设置停止递归的父类
     * @param clazz 要获取所有字段的类
     * @param stopSuperClass 类的停止父类，即获取到该父类后，不在向上获取,默认Object
     * @return 指定类的所有成员变量
     */
    private static List<Field> getAllDeclaredField(Class clazz, Class stopSuperClass){
        Class tmpClazz;
        if(clazz!=null){
            tmpClazz = clazz;
        }else {
            return new ArrayList<>();
        }
        //到缓存中查找对应的字段信息
        String fieldCatchKey = getFieldCatchKey(clazz, stopSuperClass);
        List<Field> fieldList  = objFieldsCatch.get(fieldCatchKey);
        if(fieldList != null){
            return fieldList;
        }
        // 加锁 保证同一个要获取字段的类和同一个停止类情况下 只执行一次获取该类所有字段操作 对于不同类可串行执行
        synchronized (getFieldCatchKey(clazz, stopSuperClass)){
            //双重检查
            fieldList  = objFieldsCatch.get(fieldCatchKey);
            if(fieldList != null){
                return fieldList;
            }
            fieldList = new ArrayList<>() ;
            while (tmpClazz != null&&tmpClazz!=stopSuperClass) {
                fieldList.addAll(Arrays.asList(tmpClazz .getDeclaredFields()));
                //得到父类,然后赋给自己
                tmpClazz = tmpClazz.getSuperclass();
            }
            objFieldsCatch.put(fieldCatchKey,fieldList);
        }
        return fieldList;
    }

    /**
     *  获取缓存的字段的字符串key
     * @param objClass 要获取所有字段对象的类型
     * @param stopClass 类的停止父类，即获取到该父类后，不在向上获取
     * @return  返回存入到字符串常量池中key的引用
     */
    private static String  getFieldCatchKey(Class objClass,Class stopClass){
        return (keyPrefix+objClass.getName()+"-"+stopClass.getName()).intern();
    }

 /*   public static void main(String[] args) {
        for (int i=0;i<3;i++){
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BuriedPointPostModel model = new BuriedPointPostModel();
                    model.setDistinctId("111");
                    model.setTitle("我是标题");
                    model.setHasPic(true);
                    ArrayList<String> channel = new ArrayList<>();
                    channel.add("测试圈");
                    model.setChannel(channel);
                    System.out.println("线程"+ finalI +"开始");
                    tt(model);
                    System.out.println("线程"+ finalI +"结束");
                }
            }).start();
        }

        BuriedPointLikeModel pointLikeModel = new BuriedPointLikeModel();
        pointLikeModel.setNickname("lalla");
        tt(pointLikeModel);


    }

    private static void tt(Object o){
        Map<String, Object> map = objectToMap(o, BuriedPointModel.class);
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            System.out.println(entry.getKey()+"---"+entry.getValue());
        }
    }*/


}


