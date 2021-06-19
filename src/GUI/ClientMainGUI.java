package GUI;

import Client.*;
import Client.ServerInputThread;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientMainGUI extends JFrame {
    private JPanel serverPanel;
    private JPanel mainPanel;
    private JTable userTable;
    private ArrayList<String> serverList;
    private String loggerUsername;
    public ClientMainGUI(Client loggin) throws IOException {
        setLayout(new BorderLayout());
        serverPanel = new JPanel();
        mainPanel = new JPanel();
        serverPanel.setLayout(null);
        mainPanel.setLayout(null);

        loggerUsername = loggin.getUsername().replace("Login>", "").split("##", 2)[0];
        this.setTitle(loggerUsername);
        //get server form file
        serverList = new ArrayList<>();
        try {
            File myObj = new File("server_config.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                serverList.add(data.replace("Port: ", ""));
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //3500 is default server port, dont need to add, will always show in GUI
        if (!serverList.isEmpty() && serverList.contains("3500"))
            serverList.remove("3500");

        JLabel serverInfo = new JLabel("Chose a server to connect");
        serverInfo.setBounds(10, 245, 240, 25);
        JButton addServerBtn = new JButton("Add server");
        addServerBtn.setBackground(new Color(69,229, 33));
        addServerBtn.setForeground(Color.WHITE);
        addServerBtn.setBorder(BorderFactory.createEmptyBorder());
        addServerBtn.setBounds(10, 10, 100, 25);

        String column[] = {"SERVER PORT", "EDIT", "REMOVE", "CONNECT"};
        DefaultTableModel model = new DefaultTableModel(column, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        final int[] numberOfServer = {1};
        model.addRow(new Object[]{"3500", "Edit", "Remove" , "Connect"});
        if (!serverList.isEmpty()) {
            for (String i : serverList) {
                model.addRow(new Object[]{i, "Edit", "Remove" , "Connect"});
            }
        }
        JTable serverTable = new JTable(model);

        ButtonEditor connCell = new ButtonEditor(new JTextField());
        ButtonRenderer connBtn = new ButtonRenderer();
        ButtonEditor editCell = new ButtonEditor(new JTextField());
        ButtonRenderer editBtn = new ButtonRenderer();
        ButtonEditor removeCell = new ButtonEditor(new JTextField());
        ButtonRenderer removeBtn = new ButtonRenderer();

        serverTable.getColumnModel().getColumn(1).setCellEditor(editCell);
        serverTable.getColumnModel().getColumn(1).setCellRenderer(editBtn);

        serverTable.getColumnModel().getColumn(2).setCellEditor(removeCell);
        serverTable.getColumnModel().getColumn(2).setCellRenderer(removeBtn);

        serverTable.getColumnModel().getColumn(3).setCellEditor(connCell);
        serverTable.getColumnModel().getColumn(3).setCellRenderer(connBtn);

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
        DefaultTableModel modelUser = new DefaultTableModel(columnUser, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        int rowCount = modelUser.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            modelUser.removeRow(i);
        }
        //adding new row
        if (loggin.getActivateUsers() != null)
            for (String item : loggin.getActivateUsers()) {
                modelUser.addRow(new Object[]{item, "Start chatting"});
            }
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
        final Client[] connectClient = {loggin};
        System.out.println(connectClient[0].getSocket());
        System.out.println(loggin.getSocket());
        //add server
        addServerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame addServerFrame = new AddServerFrame(model, serverList);
                addServerFrame.setVisible(true);
                numberOfServer[0]++;
            }
        });
        //remove server
        removeCell.getBtn().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = serverTable.getSelectedRow();
                if (index != -1) {
                    int modelIndex = serverTable.convertRowIndexToModel(index);
                    DefaultTableModel svModel = (DefaultTableModel) serverTable.getModel();
                    System.out.println(numberOfServer[0]);
                    if(modelIndex == svModel.getRowCount() - 1 && svModel.getRowCount() > 1) {
                        JOptionPane.showMessageDialog(removeCell.getBtn(), "Removing last row will cause a bug" +
                                " that we haven't fixed yet so we move it on top and u can delete it :D");
                        svModel.moveRow(modelIndex, modelIndex, 0);

                    } else {
                        serverList.remove(serverTable.getValueAt(index, 0).toString());
                        svModel.removeRow(modelIndex);
                    }
                }
            }
        });

        //edit server
        editCell.getBtn().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = serverTable.getSelectedRow();
                if (index != -1) {
                    int modelIndex = serverTable.convertRowIndexToModel(index);
                    DefaultTableModel svModel = (DefaultTableModel) serverTable.getModel();
                    JFrame editFrame = new EditServerFrame(svModel, modelIndex, serverList);
                    editFrame.setVisible(true);
                }
            }
        });

        final ServerInputThread[] inThread = {new ServerInputThread(loggerUsername, connectClient[0].getSocket(),
                new DataInputStream(connectClient[0].getSocket().getInputStream()), connectClient[0].getActivateUsers(),
                (DefaultTableModel) userTable.getModel())};
        inThread[0].start();
        connCell.getBtn().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = serverTable.getSelectedRow();
                if (index != -1) {
                    try {
                        System.out.println(serverTable.getValueAt(index, 0));
                        int connectPort = Integer.parseInt((serverTable.getValueAt(index, 0).toString()));
                        int oldPort = -1;
                        if (connectClient[0].getSocket() != null)
                            oldPort = connectClient[0].getSocket().getPort();
                        connectClient[0] = new Client(loggerUsername, connectPort);

                        if (connectClient[0] != null && connectClient[0].getSocket() != null) {
                            if (oldPort != connectPort) {
                                    //close old connection
                                String closingStr = loggerUsername;
                                byte[] b = closingStr.getBytes(StandardCharsets.UTF_8);
                                DataOutputStream dos = new DataOutputStream(connectClient[0].getSocket().getOutputStream());
                                Thread outThread = new ServerOutputThread(connectClient[0].getSocket(), dos, b,
                                            "Close-" + loggerUsername);
                                outThread.start();
                                TimeUnit.SECONDS.sleep(1);
                                ArrayList<String> activeUsers = connectClient[0].getActivateUsers();

                                //adding new row
                                DefaultTableModel activeUserTblModel = (DefaultTableModel) userTable.getModel();
                                int rowCountIn = activeUserTblModel.getRowCount();
                                for (int i = 0; i < rowCountIn; i++) {
                                    activeUserTblModel.removeRow(i);
                                }

                                if (activeUsers != null)
                                    for (String item : activeUsers) {
                                        activeUserTblModel.addRow(new Object[]{item, "Start chatting"});
                                }
                                if (connectClient[0].getSocket() != null)
                                    serverInfo.setText("Connected to server with port - " + connectPort);

                                //start a thread to listen for change from server
                                Socket clientSocket = connectClient[0].getSocket();
                                inThread[0] = new ServerInputThread(loggerUsername, clientSocket,
                                        new DataInputStream(clientSocket.getInputStream()), activeUsers, activeUserTblModel);
                                inThread[0].start();
                                } else
                                    serverInfo.setText("You are already connected to this server");
                            }
                            else
                                serverInfo.setText("Server is not responding");
                    }
                    catch (NumberFormatException | IOException | InterruptedException ex) {
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
                            ChatFrame newChatFrame = new ChatFrame(loggerUsername, connectClient[0].getSocket(),
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

        //group chat handle
        JButton groupChatBtn = new JButton("Create group chat");
        groupChatBtn.setBackground(Color.WHITE);
        groupChatBtn.setBounds(390, 315,200, 40);
        mainPanel.add(groupChatBtn);
        groupChatBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(inThread[0] != null) {
                    JFrame createGroupChat = new CreateGroupChatFrame(loggerUsername, connectClient[0].getSocket(),
                            inThread[0].getListPopChats(), inThread[0].getModel());
                    createGroupChat.setVisible(true);
                }
                else
                    JOptionPane.showMessageDialog(groupChatBtn, "There's no connection!");
            }
        });

        //exit GUI and save to file
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //write server_config when exit
                try {
                    File myObj = new File("server_config.txt");
                    FileWriter myWriter = new FileWriter("server_config.txt");
                    System.out.println("File created: " + myObj.getName());
                    for (String i : serverList) {
                        myWriter.write("Port: " + i + "\n");
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                //close connection
                String closingStr = loggerUsername;
                try {
                    byte[] b = closingStr.getBytes(StandardCharsets.UTF_8);
                    DataOutputStream dos = new DataOutputStream(connectClient[0].getSocket().getOutputStream());
                    Thread outThread = new ServerOutputThread(connectClient[0].getSocket(), dos, b,
                            "Close-" + loggerUsername);
                    outThread.start();
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException | IOException interruptedException) {
                    interruptedException.printStackTrace();
                }
                super.windowClosing(e);
            }
        });
    }
}
