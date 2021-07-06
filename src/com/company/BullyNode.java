package com.company;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BullyNode {
    private boolean isCoordinator;
    private static Process[] processes;
    private static ServerSocket[] sockets;
    private Socket[] clientSocket;
    private PrintWriter[] out;
    private BufferedReader[] in;

    public void setCoordinator(boolean coordinator) {
        isCoordinator = coordinator;
    }
    public boolean getCoordinator(){
        return isCoordinator;
    }
    public static void main(String[] args) throws IOException {
        processes=Main.processes;
        sockets=new ServerSocket[5];
        int id=Integer.parseInt(args[0]);
        try {
            sockets[id] = new ServerSocket(id + 10000);
            System.out.println("Process " + ProcessHandle.current().pid() + " Port Number = " + sockets[id].getLocalPort());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
