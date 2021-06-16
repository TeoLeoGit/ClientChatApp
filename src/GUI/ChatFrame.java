package GUI;

import Client.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatFrame extends JFrame {
    public JTextPane mainChat;
    public ChatFrame(String username, Socket clientSocket, String client2Name, HashMap<String,
            ChatFrame> listActiveChats, DefaultTableModel model) throws IOException {
        setTitle(client2Name);
        JPanel mainPanel = new JPanel();
        mainChat = new JTextPane();
        JTextPane senderText = new JTextPane();
        JPanel emojiBar = new JPanel();
        JButton sendTextBtn = new JButton("Send");
        JButton sendFileBtn = new JButton("Send file");
        JButton voiceChatBtn = new JButton("Voice chat");
        JButton cameraChatBtn = new JButton("Camera chat");

        mainPanel.setLayout(null);

        mainChat.setEditable(false);
        JScrollPane mainChatScroll = new JScrollPane(mainChat);
        mainChatScroll.setBounds(20, 20, 585, 330);

        JScrollPane textScroll = new JScrollPane(senderText);
        textScroll.setBounds(20, 360, 585, 96);

        emojiBar.setLayout(new FlowLayout());
        emojiBar.setBounds(20, 470, 460,35);

        sendTextBtn.setBounds(490, 470, 115, 35);

        sendFileBtn.setBounds(610, 375, 255, 40);

        voiceChatBtn.setBounds(610, 420, 255, 40);

        cameraChatBtn.setBounds(610, 465, 255, 40);

        mainPanel.add(mainChatScroll); mainPanel.add(textScroll); mainPanel.add(emojiBar);
        mainPanel.add(sendTextBtn); mainPanel.add(voiceChatBtn);
        mainPanel.add(sendFileBtn); mainPanel.add(cameraChatBtn);

        add(mainPanel);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(900, 550);
        setPreferredSize(new Dimension(900, 530));
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //event handle
        if (!listActiveChats.containsKey(client2Name)) {
            model.addRow(new Object[]{ client2Name, "Start chatting"});
        }
        StyledDocument doc = mainChat.getStyledDocument();
        //sending message
        sendTextBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (true) {
                    try {
                        doc.insertString(doc.getLength(),"You:\n" + senderText.getText() + "\n", null);
                        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                        Thread outThread = new ServerOutputThread(clientSocket, dos, client2Name +
                                "-" + username + "@" + senderText.getText(), "String");
                        outThread.start();
                        senderText.setText("");
                    } catch (BadLocationException | IOException badLocationException) {
                        badLocationException.printStackTrace();
                    }

                }
            }
        });

        //closing chat
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                listActiveChats.replace(client2Name, null);
                super.windowClosing(e);
            }
        });
    }

    public JTextPane getMainChat() {
        return mainChat;
    }
}
