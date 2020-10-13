package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Class that handles TCP Socket connection to send and receive messages with a separate chat server.
 */
public class ConnectClient {
    private String host;
    private int port;
    private BufferedReader in;
    private PrintWriter out;
    private boolean isConnectionActive;
    private ObservableList<String> messages;

    private Socket socket;
    private static String USER_NAME= "You"; //TODO: replace later with username input from user.


    /**
     * Constructor to create a connection using two parameters: Host and Port
     * @param host the host to be used when creating a TCP socket.
     * @param port The port to be used when creating the TCP socket.
     */
    public ConnectClient(String host, int port) {
        this.host = host;
        this.port = port;
        //Container for the conversation/chat messages.
        messages= FXCollections.observableArrayList();
    }

    /**
     * Method to create the socket connection and keep it alive to send and receive data from the Input/Output Stream.
     * @return true on successful connection, false on failed attempt.
     */
    public boolean connect()
    {
        try{
            socket= new Socket(host,port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

            isConnectionActive = true;

            Thread readMessage = new Thread(() -> {
                while (isConnectionActive)
                {
                    try{
                        String message= in.readLine();
                        Platform.runLater(()->{
                            messages.add("Server: " + message);
                        });
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            readMessage.start();
        }catch(IOException e)
        {return false;
        }
        return true;
    }

    /**
     * Method to send a message with the socket's output stream.
     * @param message string of the message.
     */
    public void sendMessage(String message){
        messages.add(USER_NAME+ ": "+ message);
        out.println(message);
    }

    /**
     * Message to break the loop and disconnect the server.
     */
    public void disconnect() {
        isConnectionActive = false;
    }

    /**
     * Getter method to fetch the list of messages from the server.
     * @return Observable list of messages.
     */
    public ObservableList<String> getMessages() {
        return messages;
    }

    //TODO: Add a method to check if server is alive or disconnected.
    //TODO: Implement text-to-speech function.
}
