package GUI;

import Client.Client;
import Client.ServerInputThread;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientMainGUI extends JFrame {
    private JPanel serverPanel;
    private JPanel mainPanel;
    private JTable userTable;
    public ClientMainGUI(String username) {
        setLayout(new BorderLayout());
        serverPanel = new JPanel();
        mainPanel = new JPanel();
        serverPanel.setLayout(null);
        mainPanel.setLayout(null);
        this.setTitle(username);

        JLabel serverInfo = new JLabel("Chose a server to connect");
        serverInfo.setBounds(10, 245, 240, 25);
        JButton addServerBtn = new JButton("Add server");
        addServerBtn.setBackground(new Color(69,229, 33));
        addServerBtn.setForeground(Color.WHITE);
        addServerBtn.setBorder(BorderFactory.createEmptyBorder());
        addServerBtn.setBounds(10, 10, 100, 25);

        String column[] = {"SERVER PORT", "STATUS", "CONNECT"};
        DefaultTableModel model = new DefaultTableModel(column, 0);
        model.addRow(new Object[]{"3500", "Online", "Connect"});
        JTable serverTable = new JTable(model);
        ButtonEditor connCell = new ButtonEditor(new JTextField());
        ButtonRenderer connBtn = new ButtonRenderer();

        serverTable.getColumnModel().getColumn(2).setCellEditor(connCell);
        serverTable.getColumnModel().getColumn(2).setCellRenderer(connBtn);
        serverTable.setShowGrid(false);
        serverTable.setRowMargin(10);
        serverTable.setRowHeight(30);
        serverTable.getColumnModel().setColumnMargin(10);
        JTableHeader header = serverTable.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLUE);

        JScrollPane pane = new JScrollPane(serverTable);
        pane.getViewport().setBackground(Color.WHITE);
        pane.setBounds(10, 40, 550, 200);

        String columnUser[] = {"Username", "Chat"};
        DefaultTableModel modelUser = new DefaultTableModel(columnUser, 0);

        //modelUser.addRow(new Object[]{"Username", "Start chatting"});
        userTable = new JTable(modelUser);
        ButtonEditor chatCell = new ButtonEditor(new JTextField());
        ButtonRenderer chatBtn = new ButtonRenderer();

        userTable.getColumnModel().getColumn(1).setCellEditor(chatCell);
        userTable.getColumnModel().getColumn(1).setCellRenderer(chatBtn);
        userTable.setShowGrid(false);
        userTable.setRowMargin(10);
        userTable.getColumnModel().setColumnMargin(10);
        userTable.setRowHeight(30);
        JTableHeader userTbHead = userTable.getTableHeader();
        userTbHead.setBackground(Color.WHITE);
        userTbHead.setForeground(Color.BLUE);

        JScrollPane userPane = new JScrollPane(userTable);
        userPane.getViewport().setBackground(Color.WHITE);
        userPane.setBounds(608, 20, 255, 470);

        serverPanel.add(pane);
        serverPanel.add(addServerBtn);
        serverPanel.add(serverInfo);
        serverPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        serverPanel.setBackground(Color.WHITE);
        serverPanel.setBounds(20, 20, 570, 270);

        mainPanel.add(userPane);
        mainPanel.add(serverPanel);
        add(mainPanel);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(900, 550);
        setPreferredSize(new Dimension(900, 530));
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //event handle
        final Client[] connectClient = {null};

        //connect to a server
        final ServerInputThread[] inThread = {null};

        DefaultTableModel activeUserTblModel = (DefaultTableModel) userTable.getModel();
        connCell.getBtn().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = serverTable.getSelectedRow();
                if (index != -1) {
                    try {
                        int connectPort = Integer.parseInt((serverTable.getValueAt(index, 0).toString()));
                        connectClient[0] = new Client(username, connectPort);
                        if (connectClient[0].getSocket() != null) {
                            ArrayList<String> activeUsers = connectClient[0].getActivateUsers();

                            int rowCount =  activeUserTblModel.getRowCount();
                            //Remove rows one by one from the end of the table
                            for (int i = rowCount - 1; i >= 0; i--) {
                                activeUserTblModel.removeRow(i);
                            }
                            //adding new row
                            for (String item : activeUsers) {
                                activeUserTblModel.addRow(new Object[]{ item, "Start chatting"});
                            }
                            serverInfo.setText("Connected to server with port - " + connectPort);

                            //start a thread to listen for change from server
                            Socket clientSocket = connectClient[0].getSocket();
                            inThread[0] = new ServerInputThread(username, clientSocket,
                                    new DataInputStream(clientSocket.getInputStream()), activeUsers, activeUserTblModel);
                            inThread[0].start();
                        }
                        else
                            serverInfo.setText("No respond");
                    }
                    catch (NumberFormatException | IOException ex) {
                        JOptionPane.showMessageDialog(connCell.getBtn(), "Number format exception");
                    }
                }
            }
        });

        //start chat frame
        chatCell.getBtn().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = userTable.getSelectedRow();
                if (index != -1) {
                    try {
                        if (inThread[0].getChatFrame(userTable.getValueAt(index, 0).toString()) == null) {
                            ChatFrame newChatFrame = new ChatFrame(username, connectClient[0].getSocket(),
                                    userTable.getValueAt(index,
                                    0).toString(), inThread[0].getListPopChats(), inThread[0].getModel());
                            newChatFrame.setVisible(true);
                            inThread[0].setListPopChatsValue(userTable.getValueAt(index, 0).toString(), newChatFrame);
                        }
                        else
                            JOptionPane.showMessageDialog(chatCell.getBtn(), "Chat is in progress");
                    } catch (NumberFormatException | IOException ex) {
                        JOptionPane.showMessageDialog(chatCell.getBtn(), "Number format exception");
                    }
                }
            }
        });
    }
}
