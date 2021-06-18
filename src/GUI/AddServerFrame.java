package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddServerFrame extends JFrame {
    public AddServerFrame(DefaultTableModel model) {
        JPanel mainPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        mainPanel.setLayout(new FlowLayout());

        //panel ministry setting
        JLabel serverPortLabel = new JLabel("Port");
        serverPortLabel.setPreferredSize(new Dimension(80, 25));
        JTextField serverPortText = new JTextField();
        serverPortText.setPreferredSize(new Dimension(165, 25));

        addBtn.setPreferredSize(new Dimension(80, 30));
        mainPanel.add(serverPortLabel);
        mainPanel.add(serverPortText);
        mainPanel.add(addBtn);
        addBtn.setBackground(Color.WHITE);
        add(mainPanel);

        //panel student setting
        this.setSize(380, 150);
        this.setPreferredSize(new Dimension(380, 60));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //event handle
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int portNumber = Integer.valueOf(serverPortText.getText());
                    int rowCount = model.getRowCount();
                    int existed = 0;
                    String portStr = serverPortText.getText();
                    for (int i = 0; i < rowCount; i++) {
                        if (model.getValueAt(i, 0).toString().equals(portStr)) {
                            existed = 1;
                            JOptionPane.showMessageDialog(addBtn, "Server port already existed");
                            break;
                        }
                    }
                    if (existed == 0 && portNumber > 0)
                        model.addRow(new Object[]{serverPortText.getText(), "Edit", "Remove", "Connect"});
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addBtn, "Illegal port format");
                }
            }
        });
    }
}
