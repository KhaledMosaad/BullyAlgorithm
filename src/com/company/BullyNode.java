package com.company;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class BullyNode {

    private static final  String host="127.0.0.1";
    private static boolean isCoordinator;
    private static HashMap<Long,Integer> processesId;
    private static Socket clientSocket;
    private static int port;
    private static Long myId;
    private static ServerSocket serverSocket;


    public static void main(String[] args) throws IOException {
        port=Integer.parseInt(args[0])+10000;
        processesId= receiveIds();
        myId=ProcessHandle.current().pid();
        System.out.println(processesId);
    }
    private static void sendMessage(int receiverPort, String message, int senderPort,Long senderId) throws IOException {
        clientSocket=new Socket(host,receiverPort);
        DataOutputStream dataOut=new DataOutputStream(clientSocket.getOutputStream());
        // first send the message
        dataOut.writeUTF(message);
        dataOut.flush();
        // second send the sender port number in case it will reply
        dataOut.writeUTF(String.valueOf(senderPort));
        dataOut.flush();
        // third message is the id in case of printing sender id
        dataOut.writeUTF(String.valueOf(senderId));
        dataOut.flush();
        // close the stream
        dataOut.close();
    }
    private static String ReceiveMessage() throws IOException {
        serverSocket = new ServerSocket(port);
        Socket socket=serverSocket.accept();
        try {
            DataInputStream dataIn = new DataInputStream(socket.getInputStream());
            String message=dataIn.readUTF();

            int senderPort=Integer.parseInt(dataIn.readUTF());

            Long senderId=Long.parseLong(dataIn.readUTF());

            dataIn.close();
            switch (message)
            {
                case "Election":
                    printingMessage("Election",senderId);
                    beginElection();
                    break;
                case "Coordinator Alive":
                    printingMessage("Coordinator Alive",senderId);
                    coordinatorMessages("YES");
                    break;
                case "YES":
                    printingMessage("YES",senderId);
                    sendMessage(senderPort,"Coordinator Alive",port,myId);
                    break;
                default:
                    System.out.printf("Error happen");
                    beginElection();
                    break;
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private static void coordinatorMessages(String message) throws IOException {
        for (Map.Entry<Long, Integer> entry : processesId.entrySet()) {
            Long key = entry.getKey();
            Integer value = entry.getValue();
            if(key!=myId) {
                sendMessage(value, message, port,myId);
            }
        }
    }
    // utility Function
    private static void printingMessage(String message,Long senderId)
    {
        Timestamp time=new Timestamp(System.currentTimeMillis());
        System.out.println("Process "+myId+": Receive "+message+" From Process "+senderId+" on "+ time);
    }
    private static void beginElection() throws IOException {
        int flag=0;
        for (Map.Entry<Long, Integer> entry : processesId.entrySet()) {
            Long key = entry.getKey();
            Integer value = entry.getValue();
            if(key>myId)
            {
                sendMessage(value,"Election",port,myId);
                flag++;
            }
        }
        if(flag==0)
        {
            isCoordinator=true;
            coordinatorMessages("Coordinator Alive");

        }
    }
    private static HashMap<Long,Integer> receiveIds() throws IOException {
        serverSocket = new ServerSocket(port);
        Socket sock=serverSocket.accept();
        try{
            ObjectInputStream object=new ObjectInputStream(sock.getInputStream());
            HashMap<Long,Integer> ids= (HashMap<Long, Integer>) object.readObject();
            object.close();
            return ids;
        }catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
