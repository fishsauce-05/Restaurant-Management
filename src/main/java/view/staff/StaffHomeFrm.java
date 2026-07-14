package view.staff;

import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StaffHomeFrm extends JFrame implements ActionListener {
    private User user;
    private JButton btnBookTable;
    private JButton btnEditBooking;
    private JButton btnOrderDish;
    private JButton btnPayment;
    private JButton btnLogout;

    public StaffHomeFrm(User user) {
        super("Trang chủ - Nhân viên phục vụ");
        this.user = user;

        this.setSize(600, 450);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(208, 232, 247));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JLabel lblStep = new JLabel("(1)");
        lblStep.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel lblTitle = new JLabel("Trang chủ nhân viên", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 24));

        JLabel lblWelcome = new JLabel("Chào mừng " + user.getName());
        lblWelcome.setFont(new Font("SansSerif", Font.PLAIN, 14));

        headerPanel.add(lblStep, BorderLayout.WEST);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        headerPanel.add(lblWelcome, BorderLayout.EAST);

        JPanel btnPanel = new JPanel(new GridLayout(5, 1, 0, 20));
        btnPanel.setOpaque(false);

        btnPanel.setPreferredSize(new Dimension(300, 320));

        btnBookTable = createButton("Đặt bàn");
        btnEditBooking = createButton("Sửa đặt bàn");
        btnOrderDish = createButton("Gọi món");
        btnPayment = createButton("Thanh toán");
        btnLogout = createButton("Đăng xuất");

        btnLogout.setForeground(Color.RED);

        btnPanel.add(btnBookTable);
        btnPanel.add(btnEditBooking);
        btnPanel.add(btnOrderDish);
        btnPanel.add(btnPayment);
        btnPanel.add(btnLogout);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(btnPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        this.add(mainPanel);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnBookTable)) {
            new SearchFreeTableFrm(user).setVisible(true);
            this.dispose();
        }
        else if (e.getSource().equals(btnEditBooking)) {
            new view.staff.SearchBookingFrm(user).setVisible(true);
            this.dispose();
        }
        else if (e.getSource().equals(btnOrderDish)) {
            new view.staff.SelectTableFrm(user).setVisible(true);
            this.dispose();
        }
        else if (e.getSource().equals(btnPayment)) {
            new view.staff.SelectTableToPayFrm(user).setVisible(true);
            this.dispose();
        }
        else if (e.getSource().equals(btnLogout)) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn đăng xuất?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {

                new view.manager.LoginFrm().setVisible(true);

                this.dispose();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                User dummyUser = new User();
                dummyUser.setId(1);
                dummyUser.setName("Nguyễn Kim An");
                dummyUser.setUsername("staff01");
                dummyUser.setPassword("123");
                dummyUser.setRole("STAFF");
                dummyUser.setPhone("0123456789");
                dummyUser.setEmail("annguyen@gmail.com");

                new StaffHomeFrm(dummyUser).setVisible(true);
            }
        });
    }
}
