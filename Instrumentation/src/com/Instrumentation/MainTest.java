package com.Instrumentation;

import java.io.File;
import java.util.List;
import java.util.jar.JarFile;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;


public class MainTest {

    
    public static void main(String[] args) throws Exception{
        String work =  System.getProperty("user.dir");
        JarFile jarFile  = new JarFile(new File(work+File.separator+"lib"+File.separator+"bcel-6.0.jar"));
        System.out.println(jarFile.getManifest().getMainAttributes().getValue("Bnd-LastModified"));
        System.out.println("main ");
        
        List<VirtualMachineDescriptor> vList =   VirtualMachine.list();
        for(VirtualMachineDescriptor vt:vList){
            System.out.println(vt.provider().name());
        }
    }
}
