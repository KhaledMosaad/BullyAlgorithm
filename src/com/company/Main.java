package com.company;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Main {
    public static HashMap<Long,Integer> processesId;
    public static ServerSocket myServerSocket;
    public static Socket myClientSocket;
    public static void main(String[] args) throws IOException, InterruptedException {

        processesId=new HashMap<Long,Integer>();
        for(int i=0;i<5;i++)
        {
            try {

                processesId.put(newBullyNode(String.valueOf(i+1)),i+10001);
            }
            catch (ClassCastException e)
            {
                e.printStackTrace();
            }
        }
        TimeUnit.SECONDS.sleep(1); // just wait until all processes created and listen to port
        SendProcesses();
    }
    public static void SendProcesses()
    {
        for(int i=0;i<5;i++)
        {
            try {
                myClientSocket = new Socket("127.0.0.1", i + 10001);
                ObjectOutputStream objectOutputStream=new ObjectOutputStream(myClientSocket.getOutputStream());
                objectOutputStream.writeObject(processesId);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public static Long newBullyNode(String args) throws IOException{
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
        return process.pid();
    }

}
