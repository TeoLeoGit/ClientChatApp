package Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerOutputThread extends Thread {
    final DataOutputStream dos;
    final Socket s;
    final byte[] sendingMsg;
    final String typeOfData;

    public ServerOutputThread(Socket s, DataOutputStream dos, byte[] sendingMsg, String typeOfData) {
        this.dos = dos;
        this.s = s;
        this.sendingMsg = sendingMsg;
        this.typeOfData = typeOfData;
    }

    public void run() {
        try {
            dos.writeUTF(typeOfData);
            dos.writeInt(sendingMsg.length);
            dos.write(sendingMsg);
            dos.flush();
            System.out.println(sendingMsg.length);
            System.out.println(sendingMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
