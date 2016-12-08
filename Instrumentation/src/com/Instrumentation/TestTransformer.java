package com.Instrumentation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class TestTransformer implements ClassFileTransformer{

    //通过这个方法，代理可以得到虚拟机载入的类的字节码（通过 classfileBuffer 参数）。代理的各种功能一般是通过操作这一串字节码得以实现的。
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
                                                                                      throws IllegalClassFormatException {
        System.out.println("transform :"+className);
        return null;
    }
    
    //transform 函数的最后，返回 null 值，表示不需要进行类字节码的转化

}
