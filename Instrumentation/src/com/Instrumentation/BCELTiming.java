package com.Instrumentation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

public class BCELTiming implements ClassFileTransformer{
    
     private String methodName;
     
     public BCELTiming(String methodName){
         this.methodName = methodName;
     }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
                                                                                      throws IllegalClassFormatException {
        try {
            Class.forName("org.apache.bcel.generic.Type");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
            //logger.error("", e);
        }
        try {
            JavaClass javaClass = new ClassParser(new ByteArrayInputStream(classfileBuffer),className+".java").parse();
            ClassGen cgen = new ClassGen(javaClass);
            Method[] methods =  javaClass.getMethods();
            int index = 0;
            for(Method m : methods){
                if(m.getName().equals(methodName)){
                    break;
                }
                index++;
            }
            if(index<methods.length){
              //  addTime(cgen, methods[index]);
            }
        }catch (IOException e) {
            e.printStackTrace();
            //logger.error("", e);
        }
        return null;
    }

    private static void addTime(ClassGen cgen, Method method){
        InstructionFactory ifact = new InstructionFactory(cgen);
        InstructionList ilist = new InstructionList();
        ConstantPoolGen pgen = cgen.getConstantPool();
        String cname = cgen.getClassName();
        MethodGen wrapgen = new MethodGen(method, cname, pgen);
        wrapgen.setInstructionList(ilist);
        MethodGen methgen = new MethodGen(method, cname, pgen);
        cgen.removeMethod(method);
        String iname = methgen.getName()+"_timing";
        methgen.setName(iname);
        cgen.addMethod(methgen.getMethod());
        Type result = methgen.getReturnType();
        Type[] parameters = methgen.getArgumentTypes();
        int stackIndex = methgen.isStatic()?0:1;
        for(int i=0;i<parameters.length;i++){
            stackIndex +=parameters[i].getSize();
        }
        ilist.append(ifact.createInvoke("java.lang.System", "currentTimeMillis",  Type.LONG, Type.NO_ARGS,Constants.INVOKESTATIC));
        ilist.append(InstructionFactory.createStore(Type.LONG, stackIndex));
     // call the wrapped method
        int offset = 0;
        short invoke = Constants.INVOKESTATIC;
        if (!methgen.isStatic()) {
            ilist.append(InstructionFactory.
                createLoad(Type.OBJECT, 0));
            offset = 1;
            invoke = Constants.INVOKEVIRTUAL;
        }
        for (int i = 0; i < parameters.length; i++) {
            Type type = parameters[i];
            ilist.append(InstructionFactory.
                createLoad(type, offset));
            offset += type.getSize();
        }
        ilist.append(ifact.createInvoke(cname, 
            iname, result, parameters, invoke));
        
        // store result for return later
        if (result != Type.VOID) {
            ilist.append(InstructionFactory.
                createStore(result, stackIndex+2));
        }
        
        // print time required for method call
        ilist.append(ifact.createFieldAccess("java.lang.System",
            "out",  new ObjectType("java.io.PrintStream"),
            Constants.GETSTATIC));
        ilist.append(InstructionConstants.DUP);
        ilist.append(InstructionConstants.DUP);
        String text = "Call to method " + methgen.getName() +
            " took ";
        ilist.append(new PUSH(pgen, text));
        ilist.append(ifact.createInvoke("java.io.PrintStream",
            "print", Type.VOID, new Type[] { Type.STRING },
            Constants.INVOKEVIRTUAL));
        ilist.append(ifact.createInvoke("java.lang.System", 
            "currentTimeMillis", Type.LONG, Type.NO_ARGS, 
            Constants.INVOKESTATIC));
        ilist.append(InstructionFactory.
            createLoad(Type.LONG, stackIndex));
        ilist.append(InstructionConstants.LSUB);
        ilist.append(ifact.createInvoke("java.io.PrintStream",
            "print", Type.VOID, new Type[] { Type.LONG },
            Constants.INVOKEVIRTUAL));
        ilist.append(new PUSH(pgen, " ms."));
        ilist.append(ifact.createInvoke("java.io.PrintStream",
            "println", Type.VOID, new Type[] { Type.STRING },
            Constants.INVOKEVIRTUAL));
            
        // return result from wrapped method call
        if (result != Type.VOID) {
            ilist.append(InstructionFactory.
                createLoad(result, stackIndex+2));
        }
        ilist.append(InstructionFactory.createReturn(result));
        
        // finalize the constructed method
        wrapgen.stripAttributes(true);
        wrapgen.setMaxStack();
        wrapgen.setMaxLocals();
        cgen.addMethod(wrapgen.getMethod());
        ilist.dispose();
    }
}
