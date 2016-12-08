package com.reflect;

public class TestRef {
    
    
    public void  print(String flag){
        long start = System.currentTimeMillis();
        for(int i=0;i<100000;i++){
            System.out.print(i);
        }
        System.out.println(" 总时间："+flag+(System.currentTimeMillis()-start));
    }

}
