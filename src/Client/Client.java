package Client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
                System.out.println("Sended signature: " + username);

                //receiving answer
                String respond = dis.readUTF();
                String[] action = respond.split(">", 2);
                if (action.length > 1) {
                    switch (action[0]) {
                        case "Reject":
                            byte[] b = username.getBytes(StandardCharsets.UTF_8);
                            Thread outThread = new ServerOutputThread(s, dos, b,
                                    "Close-" + username);
                            outThread.start();
                            TimeUnit.SECONDS.sleep(1);
                            dos.close();
                            s.close();
                            break;
                        case "Registered":
                            username = action[1];
                            byte[] b1 = username.getBytes(StandardCharsets.UTF_8);
                            Thread outThread1 = new ServerOutputThread(s, dos, b1,
                                    "Close-" + username);
                            outThread1.start();
                            TimeUnit.SECONDS.sleep(1);
                            dos.close();
                            s.close();
                            break;
                        case "Already Registered":
                            username = action[1];
                            byte[] b2 = username.getBytes(StandardCharsets.UTF_8);
                            Thread outThread2 = new ServerOutputThread(s, dos, b2,
                                    "Close-" + username);
                            outThread2.start();
                            TimeUnit.SECONDS.sleep(1);
                            dos.close();
                            break;
                        default:
                            String[] arrOfUsers = respond.split("-", -2);
                            activeUsers = new ArrayList<String>();
                            for (int i = 1; i < arrOfUsers.length; i++) {
                                activeUsers.add(arrOfUsers[i]);
                            }
                    }
                } else {
                    //receiving active users list
                    String listUserString = respond;

                    activeUsers = new ArrayList<String>();
                    String[] arrOfUsers = listUserString.split("-", -2);
                    for (int i = 1; i < arrOfUsers.length; i++) {
                        activeUsers.add(arrOfUsers[i]);
                    }
                }
            } catch (ConnectException ce) {
                ce.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void setUsername(String username) {username = username;}
}




