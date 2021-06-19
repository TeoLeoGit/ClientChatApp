package Client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    String username;
    ArrayList<String> activeUsers;
    private Socket s;
    public Client(String username, int portNumber) {
        this.username = username;
        try {
            try {
                s = new Socket("localhost", portNumber);
                System.out.println(s.getPort());
                System.out.println("Talking to server");
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                //sending username to server
                dos.writeUTF(username);
                //receiving active users list
                String listUserString = dis.readUTF();
                ;

                //receiving list user
                activeUsers = new ArrayList<String>();
                String[] arrOfUsers = listUserString.split("-", -2);
                for (int i = 1; i < arrOfUsers.length;i++) {
                    activeUsers.add(arrOfUsers[i]);
                }
            } catch (ConnectException ce) {
                ce.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Socket getSocket() {
        return s;
    }
    public ArrayList<String> getActivateUsers() {
        return this.activeUsers;
    }
    public String getUsername() {return username;}
}




