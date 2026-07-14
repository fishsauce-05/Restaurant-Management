package view.manager;

import model.User;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class ManagerHomeFrm extends JFrame {
    private final User currentUser;

    public ManagerHomeFrm(User currentUser) {
        super("Quản lý nhà hàng - Manager");
        this.currentUser = currentUser;
        configureFrm();
        buildContent();
    }

    private void configureFrm() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(560, 360);
        setLocationRelativeTo(null);
    }

    private void buildContent() {
        JPanel rootPanel = new JPanel(new BorderLayout(16, 16));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JLabel titleLabel = new JLabel("Quản lý nhà hàng", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(22.0f));

        JLabel userLabel = new JLabel(buildUserGreeting(), SwingConstants.CENTER);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        headerPanel.add(titleLabel);
        headerPanel.add(userLabel);

        JPanel navigationPanel = new JPanel(new GridLayout(6, 1, 12, 12));
        JButton dishButton = new JButton("Quản lý món ăn");
        JButton tableButton = new JButton("Quản lý bàn");
        JButton statButton = new JButton("Xem thống kê báo cáo");
        JButton clientButton = new JButton("Quản lý khách hàng");
        JButton staffButton = new JButton("Quản lý nhân viên");
        JButton logoutButton = new JButton("Đăng xuất");

        dishButton.addActionListener(event -> { new ManageDishFrm(currentUser).setVisible(true); dispose(); });
        tableButton.addActionListener(event -> { new ManageTableFrm(currentUser).setVisible(true); dispose(); });
        statButton.addActionListener(event -> new SelectStatFrm(currentUser).setVisible(true));
        clientButton.addActionListener(event -> new ManageClientFrm(this).setVisible(true));
        staffButton.addActionListener(event -> new ManageStaffFrm(currentUser).setVisible(true));
        logoutButton.addActionListener(event -> logout());

        navigationPanel.add(dishButton);
        navigationPanel.add(tableButton);
        navigationPanel.add(statButton);
        navigationPanel.add(clientButton);
        navigationPanel.add(staffButton);
        navigationPanel.add(logoutButton);

        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(navigationPanel, BorderLayout.CENTER);
        add(rootPanel);
    }

    private String buildUserGreeting() {
        if (currentUser == null) {
            return "Đang đăng nhập với quyền Manager";
        }

        String displayName = currentUser.getName();
        if (displayName == null || displayName.trim().isEmpty()) {
            displayName = currentUser.getUsername();
        }

        String username = currentUser.getUsername() == null ? "" : currentUser.getUsername();
        return "Xin chào " + displayName + " (" + username + ") - " + currentUser.getRole();
    }

    private void logout() {
        dispose();
        new LoginFrm().setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                User dummyUser = new User();
                dummyUser.setId(4);
                dummyUser.setName("Quản lý nhà hàng");
                dummyUser.setUsername("manager");
                dummyUser.setPassword("123456");
                dummyUser.setRole("manager");
                dummyUser.setPhone("0900000001");
                dummyUser.setEmail("manager@restaurant.com");

                new ManagerHomeFrm(dummyUser).setVisible(true);
            }
        });
    }
}
