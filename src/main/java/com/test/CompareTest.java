package com.test;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class CompareTest {

    class Test{
        public int a;
        public int b;
        public void test(){}
    }

    static class Test1{
        public int a;
        public int b;
        public void test(){}
    }

    public static synchronized void compare(int[] value){
        int tmp = value[0];
        value[0] = value[1];
        value[1] = tmp;
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
//        int[] value = {1,2};
//        CompareTest.compare(value);
//
//        System.out.println("a="+value[0]+",b="+value[1]);

        CompareTest c = new CompareTest();
        Test t =  c.new Test(); //內部類　，需要父實例創建
        Test1 t1 =  new Test1(); //內部靜態類　，可以直接實例化


//        Field f =Unsafe.class.getDeclaredField("theUnsafe");
//        f.setAccessible(true);
//        Unsafe unsafe = (Unsafe) f.get(null);



    }

}
