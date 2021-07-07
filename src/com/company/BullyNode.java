package com.company;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BullyNode {

    private static final  String host="127.0.0.1";
    private static boolean isCoordinator;
    private static HashMap<Long,Integer> processesId;
    private static Socket clientSocket;
    private static int myPort;
    private static Long myId;
    private static ServerSocket serverSocket;


    public static void main(String[] args) throws IOException, InterruptedException {
        myPort =Integer.parseInt(args[0])+10000;
        processesId= receiveIds();
        myId=ProcessHandle.current().pid();
        System.out.println(processesId);
        beginElection();
    }

    // election begin here just iterate on all process ids to check
    // the higher one and choose him as a Coordinator
    private static void beginElection() throws IOException, InterruptedException {
        int flag=0;
        for (Map.Entry<Long, Integer> entry : processesId.entrySet()) {
            Long processId = entry.getKey();
            Integer processPort = entry.getValue();
            if(processId>myId)
            {
                receiveMessage(processPort);
                sendMessage(processPort,"Election", myPort,myId);
                flag++;
            }
        }
        if(flag==0)
        {
            isCoordinator=true;
            coordinatorMessages("Coordinator Alive");
        }
    }

    // any process send message from here
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

    // and process receive message from here by it's socket
    // this function also recursive with sendMessage method to send and receive messages
    private static void receiveMessage(int port) throws IOException {
        try {

            System.out.println("Port = " + port + " id = " + myId + " My Port = " + myPort);
            serverSocket=new ServerSocket(port);
            Socket socket = serverSocket.accept();
            try {
                DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                String message = dataIn.readUTF();

                int senderPort = Integer.parseInt(dataIn.readUTF());

                Long senderId = Long.parseLong(dataIn.readUTF());

                dataIn.close();

                switch (message) {
                    case "Election":
                        printingMessage("Election", senderId);
                        beginElection();
                        break;
                    case "Coordinator Alive":
                        printingMessage("Coordinator Alive", senderId);
                        coordinatorMessages("YES");
                        break;
                    case "YES":
                        printingMessage("YES", senderId);
                        receiveMessage(senderPort);
                        sendMessage(senderPort, "Coordinator Alive", port, myId);
                        break;
                    default:
                        System.out.printf("Error happen");
                        beginElection();
                        break;
                }
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (SocketException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    // this method just fire for coordinator to send messages for all processes
    private static void coordinatorMessages(String message) throws IOException, InterruptedException {
        for (Map.Entry<Long, Integer> entry : processesId.entrySet()) {
            Long processId = entry.getKey();
            Integer processPort = entry.getValue();
            if(processPort!=myPort) {
                receiveMessage(processPort);
                sendMessage(processPort, message, myPort,myId);
            }
        }
    }



    // utility Function
    private static void printingMessage(String message,Long senderId)
    {
        Timestamp time=new Timestamp(System.currentTimeMillis());
        if(isCoordinator)
        {
            System.out.print("Coordinator ");
        }
        System.out.println("Process "+myId+": Receive '"+message+"' Message From Process "+senderId+" on "+ time);
    }

    // receiving the processes ids for each process in the system
    private static HashMap<Long,Integer> receiveIds() throws IOException {
        serverSocket = new ServerSocket(myPort);
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
