package ru.polynkina.irina.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoFrame extends JDialog {

    public InfoFrame(JFrame owner, String name, String text) {
        super(owner, name, true);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(size.width / 4, size.height / 4);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelLabel = new JPanel();
        JLabel label = new JLabel();
        label.setText("<html><br>" + text + "<br></html>");
        label.setForeground(Color.BLUE);
        panelLabel.add(label, BorderLayout.NORTH);

        JPanel panelButton = new JPanel();
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panelButton.add(ok, BorderLayout.PAGE_END);

        add(panelLabel, BorderLayout.NORTH);
        add(panelButton, BorderLayout.SOUTH);
    }
}
