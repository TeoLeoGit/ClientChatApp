package Client;

import GUI.ChatFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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
        //styling username in chat
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, new Color(69,229, 33));
        StyleConstants.setBackground(keyWord, Color.YELLOW);
        StyleConstants.setBold(keyWord, true);

        while(true) {
            try {
                String signal = dis.readUTF();
                String[] dataAndSender = signal.split("@", 2);
                int count = dis.readInt();
                byte[] receivedMsg = new byte[count];
                dis.readFully(receivedMsg);
                if(dataAndSender[0].equals("String")) {
                    String messageInStr = new String(receivedMsg, StandardCharsets.UTF_8);
                    if (listPopChats.containsKey(dataAndSender[1])) {
                        ChatFrame updateFrame = listPopChats.get(dataAndSender[1]);
                        if (updateFrame == null) {
                            ChatFrame newChat = new ChatFrame(username, s, dataAndSender[1], listPopChats, model);
                            newChat.setVisible(true);
                            StyledDocument doc = newChat.getMainChat().getStyledDocument();
                            doc.insertString(doc.getLength(),dataAndSender[1] + ":\n", keyWord);
                            doc.insertString(doc.getLength(), messageInStr + "\n", null);
                            listPopChats.replace(dataAndSender[1], newChat);
                        } else {
                            StyledDocument doc = updateFrame.getMainChat().getStyledDocument();
                            doc.insertString(doc.getLength(),dataAndSender[1] + ":\n", keyWord);
                            doc.insertString(doc.getLength(), messageInStr + "\n", null);
                        }
                    } else {
                        ChatFrame newChat = new ChatFrame(username, s, dataAndSender[1], listPopChats, model);
                        newChat.setVisible(true);
                        StyledDocument doc = newChat.getMainChat().getStyledDocument();
                        doc.insertString(doc.getLength(),dataAndSender[1] + ":\n", keyWord);
                        doc.insertString(doc.getLength(), messageInStr + "\n", null);
                        listPopChats.put(dataAndSender[1], newChat);
                    }
                }
                if (dataAndSender[0].equals("Image")) {
                    ImageIcon imgIcon = new ImageIcon(receivedMsg);
                    SimpleAttributeSet style = new SimpleAttributeSet();
                    StyleConstants.setIcon(style, imgIcon);
                    if (listPopChats.containsKey(dataAndSender[1])) {
                        ChatFrame updateFrame = listPopChats.get(dataAndSender[1]);
                        if (updateFrame == null) {
                            ChatFrame newChat = new ChatFrame(username, s, dataAndSender[1], listPopChats, model);
                            newChat.setVisible(true);
                            StyledDocument doc = newChat.getMainChat().getStyledDocument();
                            doc.insertString(doc.getLength(),dataAndSender[1] + ":\n", keyWord);
                            doc.insertString(doc.getLength(), "test", style);
                            doc.insertString(doc.getLength(),"\n", null);
                            listPopChats.replace(dataAndSender[1], newChat);
                        } else {
                            StyledDocument doc = updateFrame.getMainChat().getStyledDocument();
                            doc.insertString(doc.getLength(),dataAndSender[1] + ":\n", keyWord);
                            doc.insertString(doc.getLength(), "test", style);
                            doc.insertString(doc.getLength(),"\n", null);
                        }
                    } else {
                        ChatFrame newChat = new ChatFrame(username, s, dataAndSender[1], listPopChats, model);
                        newChat.setVisible(true);
                        StyledDocument doc = newChat.getMainChat().getStyledDocument();
                        doc.insertString(doc.getLength(),dataAndSender[1] + ":\n", keyWord);
                        doc.insertString(doc.getLength(), "test", style);
                        doc.insertString(doc.getLength(),"\n", null);
                        listPopChats.put(dataAndSender[1], newChat);
                    }
                }

                if (dataAndSender[0].contains("File")) {
                    String[] fileName = dataAndSender[0].split(">", 2);
                    JButton downloadBtn = new JButton(fileName[1]);
                    downloadBtn.setPreferredSize(new Dimension(100, 20));
                    downloadBtn.setBackground(Color.WHITE);
                    if (listPopChats.containsKey(dataAndSender[1])) {
                        ChatFrame updateFrame = listPopChats.get(dataAndSender[1]);
                        if (updateFrame == null) {
                            ChatFrame newChat = new ChatFrame(username, s, dataAndSender[1], listPopChats, model);
                            newChat.setVisible(true);
                            StyledDocument doc = newChat.getMainChat().getStyledDocument();
                            JTextPane mainChat = newChat.getMainChat();
                            doc.insertString(doc.getLength(), dataAndSender[1] + ":\n", keyWord);
                            mainChat.setCaretPosition(mainChat.getDocument().getLength());
                            mainChat.insertComponent(downloadBtn);
                            doc.insertString(doc.getLength(), "\n", null);
                            listPopChats.replace(dataAndSender[1], newChat);
                        } else {
                            StyledDocument doc = updateFrame.getMainChat().getStyledDocument();
                            JTextPane mainChat = updateFrame.getMainChat();
                            doc.insertString(doc.getLength(), dataAndSender[1] + ":\n", keyWord);
                            mainChat.setCaretPosition(mainChat.getDocument().getLength());
                            mainChat.insertComponent(downloadBtn);
                            doc.insertString(doc.getLength(), "\n", null);
                        }
                    } else {
                        ChatFrame newChat = new ChatFrame(username, s, dataAndSender[1], listPopChats, model);
                        newChat.setVisible(true);
                        StyledDocument doc = newChat.getMainChat().getStyledDocument();
                        JTextPane mainChat = newChat.getMainChat();
                        doc.insertString(doc.getLength(), dataAndSender[1] + ":\n", keyWord);
                        mainChat.setCaretPosition(mainChat.getDocument().getLength());
                        mainChat.insertComponent(downloadBtn);
                        doc.insertString(doc.getLength(), "\n", null);
                        listPopChats.put(dataAndSender[1], newChat);
                    }
                    downloadBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JFileChooser chooser = new JFileChooser();
                            chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
                            int option = chooser.showOpenDialog(downloadBtn);
                            if (option == JFileChooser.APPROVE_OPTION) {
                                File selectedFile = chooser.getSelectedFile();
                                String path = selectedFile.getAbsolutePath();
                                try (FileOutputStream fos = new FileOutputStream(path + "\\\\" + fileName[1])) {
                                    fos.write(receivedMsg);
                                    System.out.println(path + "\\\\" + fileName[1]);
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                            }
                        }
                    });
                }

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