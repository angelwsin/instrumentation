package com.Instrumentation;

import java.lang.instrument.Instrumentation;

public class Premain {
    
    public static void premain(String agentArgs, Instrumentation inst){//1
        System.out.println(" premain  agentArgs:"+agentArgs);
       // inst.addTransformer(new TestTransformer());
       /* String work =  System.getProperty("java.class.path");
        try {
            JarFile jarFile  = new JarFile(new File(work+File.separator+"lib"+File.separator+"bcel-6.0.jar"));
            inst.appendToSystemClassLoaderSearch(jarFile);
        } catch (IOException e) {
            e.printStackTrace();
           // logger.error("", e);
        }
        */
        inst.addTransformer(new BCELTiming(agentArgs));
    }
    
   public static void premain(String agentArgs){//2
       
   }
   
   /**
    * 其中，[1] 的优先级比 [2] 高，将会被优先执行（[1] 和 [2] 同时存在时，[2] 被忽略）。
在这个 premain 函数中，开发者可以进行对类的各种操作。
agentArgs 是 premain 函数得到的程序参数，随同 “– javaagent”一起传入。与 main 函数不同的是，这个参数是一个字符串而不是一个字符串数组，如果程序参数有多个，程序将自行解析这个字符串。
Inst 是一个 java.lang.instrument.Instrumentation 的实例，由 JVM 自动传入。
java.lang.instrument.Instrumentation 是 instrument 包中定义的一个接口，也是这个包的核心部分，集中了其中几乎所有的功能方法，例如类定义的转换和操作等等。

   java -javaagent:instrumentation.jar=agentAgs com.Instrumentation.MainTest
    */

}
