package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class CreateGroupChatFrame extends JFrame {
    private JPanel mainPanel;
    public CreateGroupChatFrame(String username, Socket clientSocket, HashMap<String,
            ChatFrame> listActiveChats, DefaultTableModel model) {
        setTitle("Create new group chat");
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());

        //table drawing
        String columnUser[] = {"Username", "Add to chat"};
        DefaultTableModel modelUser = new DefaultTableModel(columnUser, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        for (String item : listActiveChats.keySet()) {
            if (!item.contains(", "))
                modelUser.addRow(new Object[]{item, "Add to group"});
        }

        JTable userTable = new JTable(modelUser);
        ButtonEditor inviteCell = new ButtonEditor(new JTextField());
        ButtonRenderer inviteBtn = new ButtonRenderer();

        userTable.getColumnModel().getColumn(1).setCellEditor(inviteCell);
        userTable.getColumnModel().getColumn(1).setCellRenderer(inviteBtn);
        userTable.setShowGrid(false);
        userTable.setRowMargin(10);
        userTable.getColumnModel().setColumnMargin(10);
        userTable.setRowHeight(30);
        JTableHeader userTbHead = userTable.getTableHeader();
        userTbHead.setBackground(Color.WHITE);
        userTbHead.setForeground(Color.BLUE);

        JScrollPane userPane = new JScrollPane(userTable);
        userPane.getViewport().setBackground(Color.WHITE);
        userPane.setPreferredSize(new Dimension(255, 470));
        mainPanel.add(userPane);

        JButton confirmBtn = new JButton("Confirm");
        confirmBtn.setBackground(Color.WHITE);
        confirmBtn.setPreferredSize(new Dimension(200, 35));
        mainPanel.add(confirmBtn);
        add(mainPanel);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(330, 570);
        setPreferredSize(new Dimension(330, 570));
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        //creating group chat event
        //adding users to group
        ArrayList<String> invitedUsers = new ArrayList<>();
        final String[] newChatName = {username};
        inviteCell.getBtn().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = userTable.getSelectedRow();
                if (index != -1) {
                    int modelIndex = userTable.convertRowIndexToModel(index);
                    String addingUser = userTable.getValueAt(index, 0).toString();
                    invitedUsers.add(addingUser);
                    newChatName[0] = newChatName[0] + ", " + addingUser;

                    DefaultTableModel model = (DefaultTableModel) userTable.getModel();
                    model.removeRow(modelIndex);
                }
            }
        });

        //create group chat
        confirmBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //not adding new users
                int lastIndex = 0;
                int count = 0;
                while (lastIndex != -1) {
                    lastIndex = newChatName[0].indexOf(", ", lastIndex);
                    if (lastIndex != -1) {
                        count++;
                        lastIndex += 2;
                    }
                }
                if (count < 2 )
                    JOptionPane.showMessageDialog(confirmBtn, "Need to add at least 2 users " +
                            "to start a group chat");
                else {
                    boolean checkExisted = false;
                    for (String item : listActiveChats.keySet())
                        if (item.contains(", ")) {
                            checkExisted = true;
                            for (String invited : invitedUsers)
                                if (!item.contains(invited)) {
                                    checkExisted = false;
                                }
                        }

                    if (!checkExisted) {
                        try {
                            ChatFrame newChatFrame = new ChatFrame(username, clientSocket, newChatName[0], listActiveChats, model);
                            newChatFrame.setVisible(true);
                            listActiveChats.put(newChatName[0], newChatFrame);

                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    } else
                        JOptionPane.showMessageDialog(confirmBtn, "This group is already created");
                }
                dispose();
            }
        });
    }
}

