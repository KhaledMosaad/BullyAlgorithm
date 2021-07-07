package com.company;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Main {
    public static HashMap<Long,Integer> processesId;
    public static ServerSocket myServerSocket;
    public static Socket myClientSocket;
    public static void main(String[] args) throws IOException, InterruptedException {

        processesId=new HashMap<>();
        for(int i=0;i<2;i++) // it's just fixed length right now
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


    // sending processes ids and ports numbers to all processes
    public static void SendProcesses() throws IOException {
        for(int i=0;i<2;i++)
        {
            try {
                myClientSocket = new Socket("127.0.0.1", i + 10001);
                ObjectOutputStream objectOutputStream=new ObjectOutputStream(myClientSocket.getOutputStream());
                objectOutputStream.writeObject(processesId);
                objectOutputStream.close();
            } catch (ConnectException e)
            {
                e.printStackTrace();

            }
        }
    }

    // fire a new process
    public static Long newBullyNode(String args) throws IOException{
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = "com.company.BullyNode";

        List<String> command = new ArrayList<>();
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
