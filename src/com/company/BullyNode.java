package com.company;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class BullyNode {

    private static final int AliveTimeOut = 200;
    private static final int VICTORY_TIMEOUT = 100;
    private static final  String host="127.0.0.1";
    private boolean isCoordinator;
    private static HashMap<Long,Integer> processesId;
    private static Socket clientSocket;
    private static int port;
    private static ServerSocket serverSocket;
    public static void main(String[] args) throws IOException {
        port=Integer.parseInt(args[0]);
        processesId=ReceiveIds();
        System.out.println(processesId);
    }
    public static void SendMessage(int port,Object message) throws IOException {
        clientSocket=new Socket(host,port);
    }
    public static String ReceiveMessage() throws IOException {
        serverSocket = new ServerSocket(port+10000);
        return null;
    }
    public static HashMap<Long,Integer> ReceiveIds() throws IOException {
        serverSocket = new ServerSocket(port+10000);
        Socket sock=serverSocket.accept();
        try{
            ObjectInputStream object=new ObjectInputStream(sock.getInputStream());
            return (HashMap<Long, Integer>) object.readObject();
        }catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
