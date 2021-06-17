package GUI;

import Client.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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
        emojiBar.setBounds(20, 460, 460,45);
        JButton likeBtn = new JButton();
        likeBtn.setPreferredSize(new Dimension(35, 35));
        JButton loveBtn = new JButton();
        loveBtn.setPreferredSize(new Dimension(35, 35));
        JButton pgBtn = new JButton();
        pgBtn.setPreferredSize(new Dimension(35, 35));
        JButton hahaBtn = new JButton();
        hahaBtn.setPreferredSize(new Dimension(35, 35));
        JButton sadBtn = new JButton();
        sadBtn.setPreferredSize(new Dimension(35, 35));

        StyledDocument doc = mainChat.getStyledDocument();

        //styling username in chat
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, Color.RED);
        StyleConstants.setBackground(keyWord, Color.YELLOW);
        StyleConstants.setBold(keyWord, true);

        //emoji part
        try {
            Image likeImg = ImageIO.read(getClass().getResource("likeEmoji.png"));
            Image loveImg = ImageIO.read(getClass().getResource("loveEmoji.png"));
            Image hahaImg = ImageIO.read(getClass().getResource("hahaEmoji.png"));
            Image pgImg = ImageIO.read(getClass().getResource("pgEmoji.png"));
            Image sadImg = ImageIO.read(getClass().getResource("sadEmoji.png"));
            likeImg = likeImg.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH );
            loveImg = loveImg.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH );
            hahaImg = hahaImg.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH );
            pgImg = pgImg.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH );
            sadImg = sadImg.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH );
            likeBtn.setIcon(new ImageIcon(likeImg));
            loveBtn.setIcon(new ImageIcon(loveImg));
            hahaBtn.setIcon(new ImageIcon(hahaImg));
            pgBtn.setIcon(new ImageIcon(pgImg));
            sadBtn.setIcon(new ImageIcon(sadImg));

            //event when chose an emoji
            Image finalPgImg = pgImg;
            Image finalLikeImg = likeImg;
            Image finalLoveImg = loveImg;
            Image finalHahaImg = hahaImg;
            Image finalSadImg = sadImg;
            sadBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SimpleAttributeSet style = new SimpleAttributeSet();
                    StyleConstants.setIcon(style, new ImageIcon(finalSadImg));
                    try {
                        doc.insertString(doc.getLength(),"You:\n", keyWord);
                        doc.insertString(doc.getLength(), "test", style);
                        doc.insertString(doc.getLength(),"\n", null);
                    } catch (BadLocationException badLocationException) {
                        badLocationException.printStackTrace();
                    }
                    try {
                        //image to byte
                        BufferedImage bImage = toBufferedImage(finalSadImg);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ImageIO.write(bImage, "png", bos );
                        byte [] data = bos.toByteArray();
                        //sending
                        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                        Thread outThread = new ServerOutputThread(clientSocket, dos, data,
                                "Image-" + client2Name + "@" + username);
                        outThread.start();
                    } catch (IOException badLocationException) {
                        badLocationException.printStackTrace();
                    }
                }
            });
            hahaBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SimpleAttributeSet style = new SimpleAttributeSet();
                    StyleConstants.setIcon(style, new ImageIcon(finalHahaImg));
                    try {
                        doc.insertString(doc.getLength(),"You:\n", keyWord);
                        doc.insertString(doc.getLength(), "test", style);
                        doc.insertString(doc.getLength(),"\n", null);
                    } catch (BadLocationException badLocationException) {
                        badLocationException.printStackTrace();
                    }
                    try {
                        //image to byte
                        BufferedImage bImage = toBufferedImage(finalHahaImg);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ImageIO.write(bImage, "png", bos );
                        byte [] data = bos.toByteArray();
                        //sending
                        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                        Thread outThread = new ServerOutputThread(clientSocket, dos, data,
                                "Image-" + client2Name + "@" + username);
                        outThread.start();
                    } catch (IOException badLocationException) {
                        badLocationException.printStackTrace();
                    }
                }
            });
            loveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SimpleAttributeSet style = new SimpleAttributeSet();
                    StyleConstants.setIcon(style, new ImageIcon(finalLoveImg));
                    try {
                        doc.insertString(doc.getLength(),"You:\n", keyWord);
                        doc.insertString(doc.getLength(), "test", style);
                        doc.insertString(doc.getLength(),"\n", null);
                    } catch (BadLocationException badLocationException) {
                        badLocationException.printStackTrace();
                    }
                    try {
                        //image to byte
                        BufferedImage bImage = toBufferedImage(finalLoveImg);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ImageIO.write(bImage, "png", bos );
                        byte [] data = bos.toByteArray();
                        //sending
                        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                        Thread outThread = new ServerOutputThread(clientSocket, dos, data,
                                "Image-" + client2Name + "@" + username);
                        outThread.start();
                    } catch (IOException badLocationException) {
                        badLocationException.printStackTrace();
                    }
                }
            });
            likeBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SimpleAttributeSet style = new SimpleAttributeSet();
                    StyleConstants.setIcon(style, new ImageIcon(finalLikeImg));
                    try {
                        doc.insertString(doc.getLength(),"You:\n", keyWord);
                        doc.insertString(doc.getLength(), "test", style);
                        doc.insertString(doc.getLength(),"\n", null);
                    } catch (BadLocationException badLocationException) {
                        badLocationException.printStackTrace();
                    }
                    try {
                        //image to byte
                        BufferedImage bImage = toBufferedImage(finalLikeImg);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ImageIO.write(bImage, "png", bos );
                        byte [] data = bos.toByteArray();
                        //sending
                        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                        Thread outThread = new ServerOutputThread(clientSocket, dos, data,
                                "Image-" + client2Name + "@" + username);
                        outThread.start();
                    } catch (IOException badLocationException) {
                        badLocationException.printStackTrace();
                    }
                }
            });
            pgBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SimpleAttributeSet style = new SimpleAttributeSet();
                    StyleConstants.setIcon(style, new ImageIcon(finalPgImg));
                    try {
                        doc.insertString(doc.getLength(),"You:\n", keyWord);
                        doc.insertString(doc.getLength(), "test", style);
                        doc.insertString(doc.getLength(),"\n", null);
                    } catch (BadLocationException badLocationException) {
                        badLocationException.printStackTrace();
                    }
                    try {
                        //image to byte
                        BufferedImage bImage = toBufferedImage(finalPgImg);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ImageIO.write(bImage, "png", bos );
                        byte [] data = bos.toByteArray();
                        //sending
                        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                        Thread outThread = new ServerOutputThread(clientSocket, dos, data,
                                "Image-" + client2Name + "@" + username);
                        outThread.start();
                    } catch (IOException badLocationException) {
                        badLocationException.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            System.out.println(ex);
        }
        emojiBar.setBackground(Color.WHITE);

        emojiBar.add(likeBtn); emojiBar.add(loveBtn); emojiBar.add(hahaBtn);
        emojiBar.add(pgBtn); emojiBar.add(sadBtn);

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

        //sending message
        sendTextBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    doc.insertString(doc.getLength(),"You:\n", keyWord);
                    doc.insertString(doc.getLength(),senderText.getText() + "\n", null);
                    byte[] b = senderText.getText().getBytes(StandardCharsets.UTF_8);
                    DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                    Thread outThread = new ServerOutputThread(clientSocket, dos, b,
                            "String-" + client2Name + "@" + username);
                    outThread.start();
                    senderText.setText("");
                } catch (BadLocationException | IOException badLocationException) {
                    badLocationException.printStackTrace();
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

    //tham khao chuyen doi image sang bufferedimage tai stackoverflow
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        // Return the buffered image
        return bimage;
    }
}
