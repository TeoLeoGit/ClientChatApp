package Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerOutputThread extends Thread {
    final DataOutputStream dos;
    final Socket s;
    final String sendingMsg;
    final String typeOfData;

    public ServerOutputThread(Socket s, DataOutputStream dos, String sendingMsg, String typeOfData) {
        this.dos = dos;
        this.s = s;
        this.sendingMsg = sendingMsg;
        this.typeOfData = typeOfData;
    }

    public void run() {
        try {
            dos.writeUTF(typeOfData);
            dos.writeUTF(sendingMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
