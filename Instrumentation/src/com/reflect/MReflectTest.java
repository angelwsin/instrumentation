package com.reflect;

import java.lang.reflect.Method;

public class MReflectTest {

    
    public static void main(String[] args) throws Exception{
        long start = System.currentTimeMillis();
        Object obj = TestRef.class.newInstance();
        Method m = TestRef.class.getDeclaredMethod("print",String.class);
        m.invoke(obj,"ref");
        System.out.println("ref :"+(System.currentTimeMillis()-start));
        start = System.currentTimeMillis();
         TestRef ref  = new TestRef();
         ref.print("new");
         System.out.println("new :"+(System.currentTimeMillis()-start));
    }
}
