package com.Instrumentation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

public class BCELTiming implements ClassFileTransformer{

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
                                                                                      throws IllegalClassFormatException {
        
        try {
            JavaClass javaClass = new ClassParser(new ByteArrayInputStream(classfileBuffer),className+".java").parse();
        }catch (IOException e) {
            e.printStackTrace();
            //logger.error("", e);
        }
        return null;
    }

}
