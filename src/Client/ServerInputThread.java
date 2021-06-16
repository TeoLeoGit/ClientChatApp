package Client;

import GUI.ChatFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerInputThread extends Thread {
    final DataInputStream dis;
    final Socket s;
    private HashMap<String, ChatFrame> listPopChats;
    final String username;
    private DefaultTableModel model;

    public ServerInputThread(String username, Socket s, DataInputStream dis, ArrayList<String> activeUsers,
                             DefaultTableModel model) {
        this.s = s;
        this.dis = dis;
        listPopChats = new HashMap<String, ChatFrame>();
        for (String item : activeUsers) {
            listPopChats.put(item, null);
        }
        this.username = username;
        this.model = model;
    }

    public void run() {
        String receivedMsg;
        while(true) {
            try {
                receivedMsg = dis.readUTF();
                String[] arrOfData = receivedMsg.split("@", 2);
                if(listPopChats.containsKey(arrOfData[0])) {
                    ChatFrame updateFrame = listPopChats.get(arrOfData[0]);
                    if (updateFrame == null) {
                        ChatFrame newChat = new ChatFrame(username, s, arrOfData[0], listPopChats, model);
                        newChat.setVisible(true);
                        StyledDocument doc = newChat.getMainChat().getStyledDocument();
                        doc.insertString(doc.getLength(), arrOfData[0] + "\n" + arrOfData[1] + "\n", null);
                        listPopChats.replace(arrOfData[0], newChat);
                    } else {
                        StyledDocument doc = updateFrame.getMainChat().getStyledDocument();
                        doc.insertString(doc.getLength(), arrOfData[0] + "\n" + arrOfData[1] + "\n", null);
                    }
                } else {
                    ChatFrame newChat = new ChatFrame(username, s, arrOfData[0], listPopChats, model);
                    newChat.setVisible(true);
                    StyledDocument doc = newChat.getMainChat().getStyledDocument();
                    doc.insertString(doc.getLength(), arrOfData[0] + "\n" + arrOfData[1] + "\n", null);
                    listPopChats.put(arrOfData[0], newChat);
                }
                System.out.println(receivedMsg);
            } catch (IOException | BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    public void setListPopChatsValue(String key, ChatFrame value) {
        listPopChats.replace(key, value);
    }

    public ChatFrame getChatFrame(String key) {
        return listPopChats.get(key);
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public HashMap<String, ChatFrame> getListPopChats() {
        return listPopChats;
    }
}