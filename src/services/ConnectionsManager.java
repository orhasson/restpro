package services;

import api.AddChild;
import api.AddParent;
import api.ChildLocation;
import api.Login;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import providers.APIActions;
import providers.CommonAction;
import java.io.*;
import java.net.*;

public class ConnectionsManager extends Thread {
    private ServerSocket server;

    public ConnectionsManager(int port) throws IOException {
        server = new ServerSocket(port);
        server.setSoTimeout(600000);
    }

    public void run() {
        while (true) {
            try {
                /* keep listens indefinitely until receives 'exit' call or program terminates
                 - creating socket and waiting for client connection
                 - read from socket to ObjectInputStream object
                 - convert ObjectInputStream object to String */
                while (true) {
                    CommonAction commonAction;
                    String messageToClient = "General Error While Reading the Message";
                    System.out.println("Waiting for client request");
                    Socket socket = server.accept();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    String messageFromClient = (String) ois.readObject();
                    System.out.println("Message Received: " + messageFromClient);

                    //Business logic:
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                    try {
                        commonAction = objectMapper.readValue(messageFromClient, CommonAction.class);
                        switch (commonAction.getAction()){
                            case APIActions.LOGIN:
                                messageToClient = Login.login(commonAction.getMsg());
                                break;
                            case APIActions.ADD_PARENT:
                                messageToClient = AddParent.addGivenParent(commonAction.getMsg());
                                break;
                            case APIActions.ADD_CHILD:
                                messageToClient = AddChild.addGivenChild(commonAction.getMsg());
                                break;
                            case APIActions.GET_CHILD_LOCATION:
                                messageToClient = ChildLocation.getChildLocation(commonAction.getMsg());
                                break;
                            case APIActions.ADD_EXISTING_CHILD:

                                break;
                        }
                        System.out.println("Msg to client is : " + messageToClient);
                    } catch (IOException e) {
                        System.out.println("General error while reading common action.");
                        e.printStackTrace();
                    }
                    /* create ObjectOutputStream object
                     - write object to Socket
                     - close resources */
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(messageToClient);
                    ois.close();
                    oos.close();
                    socket.close();

                    //terminate the server if client sends exit request
                    if (messageFromClient.equalsIgnoreCase("exit")) break;

                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

