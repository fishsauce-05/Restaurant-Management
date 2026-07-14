package com.mycompany.restaurant_module;

import javax.swing.SwingUtilities;

import view.manager.LoginFrm;

public class Restaurant_Module {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginFrm().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
