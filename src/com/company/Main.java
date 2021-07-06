package com.company;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static Process[] processes;
    public static void main(String[] args) throws IOException {
        processes=new Process[5];
        for(int i=0;i<5;i++)
        {
            processes[i]=newProcess(String.valueOf(i));
        }
    }
    public static Process newProcess(String args) throws IOException{
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = "com.company.BullyNode";

        List<String> command = new ArrayList<String>();
        command.add(javaBin);
        command.add("-cp");
        command.add(classpath);
        command.add(className);
        if (args != null) {
            command.add(args);
        }
        ProcessBuilder builder = new ProcessBuilder(command);

        Process process = builder.inheritIO().start();
        return process;
    }

}
