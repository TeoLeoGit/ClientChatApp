package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EditServerFrame extends JFrame {
    public EditServerFrame(DefaultTableModel model, int modelIndex, ArrayList<String> serverList) {
        JPanel mainPanel = new JPanel();
        JButton editBtn = new JButton("Edit");
        mainPanel.setLayout(new FlowLayout());

        //panel ministry setting
        JLabel serverPortLabel = new JLabel("Port");
        serverPortLabel.setPreferredSize(new Dimension(80, 25));
        JTextField serverPortText = new JTextField();
        serverPortText.setPreferredSize(new Dimension(165, 25));

        editBtn.setPreferredSize(new Dimension(80, 30));
        mainPanel.add(serverPortLabel);
        mainPanel.add(serverPortText);
        mainPanel.add(editBtn);
        editBtn.setBackground(Color.WHITE);
        add(mainPanel);

        //panel student setting
        this.setSize(380, 150);
        this.setPreferredSize(new Dimension(380, 60));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //event handle
        editBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int portNumber = Integer.valueOf(serverPortText.getText());
                    String newPort = serverPortText.getText();
                    if(serverList.contains(newPort) || portNumber == 3500)
                        JOptionPane.showMessageDialog(editBtn, "Port already existed in list or default port");
                    else {
                        serverList.remove(model.getValueAt(modelIndex, 0));
                        model.setValueAt(serverPortText.getText(), modelIndex, 0);
                        serverList.add(newPort);
                    }
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(editBtn, "Illegal port format");
                }
            }
        });
    }
}
