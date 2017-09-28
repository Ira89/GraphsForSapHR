package ru.polynkina.irina.main;

import ru.polynkina.irina.gui.UserFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { new UserFrame(); }
        });
    }
}