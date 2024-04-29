package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer {

    private int port;
    private ClientHandler ch;
    private volatile boolean stop;

    public MyServer(int port, ClientHandler ch){
        this.port=port;
        this.ch=ch;
        this.stop=false;
    }
    
    private void runServer(){
        try {
            ServerSocket server = new ServerSocket(this.port);
            server.setSoTimeout(1000);
            while (!stop) {
                Socket aClienSocket = server.accept(); // Waiting for a call
                try {
                    ch.handleClient(aClienSocket.getInputStream(), aClienSocket.getOutputStream());
                    ch.close();
                    aClienSocket.close();
                } catch (SocketTimeoutException e) {
                    /*...*/
                }
                finally {
                    aClienSocket.close();
                }
            } 
            server.close();
            } catch (IOException e){
                /*...*/
            }
        }

    public void start(){
        new Thread(() -> {
            try {
                runServer();
            } catch (Exception e){
                /*...*/
            }
        }).start();
    }

    public void close(){
        this.stop=true;
    }
}
