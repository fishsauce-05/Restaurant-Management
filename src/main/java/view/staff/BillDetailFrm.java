package view.staff;

import dao.bill.BillDAO;
import dao.table.TableDAO;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class BillDetailFrm extends JFrame implements ActionListener {
    private User user;
    private Order order;
    private JTable tblOrderedDishes;
    private DefaultTableModel tableModel;
    private JLabel lblTotalAmount;
    private JComboBox<String> cboPaymentMethod;
    private JButton btnConfirmPayment;
    private JButton btnBack;

    public BillDetailFrm(User u, Order order) {
        super("Chi tiết hóa đơn");
        this.user = u;
        this.order = order;
        initComponents();
        loadOrderDetail();
        setSize(680, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(12, 15, 5, 15));
        pnlHeader.setBackground(new Color(41, 128, 185));

        JLabel lblTitle = new JLabel("CHI TIẾT HÓA ĐƠN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitle.setForeground(Color.WHITE);

        String tableInfo = (order != null && order.getTable() != null)
                ? "Bàn: " + order.getTable().getTableCode() + " - " + order.getTable().getName()
                : "Chi tiết hóa đơn";
        JLabel lblTableInfo = new JLabel(tableInfo);
        lblTableInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTableInfo.setForeground(Color.WHITE);

        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        pnlHeader.add(lblTableInfo, BorderLayout.SOUTH);
        add(pnlHeader, BorderLayout.NORTH);

        String[] columns = {"STT", "Tên món", "Danh mục", "SL", "Đơn giá (VNĐ)", "Thành tiền (VNĐ)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblOrderedDishes = new JTable(tableModel);
        tblOrderedDishes.setRowHeight(28);
        tblOrderedDishes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblOrderedDishes.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(tblOrderedDishes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách món đã gọi"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel();
        pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
        pnlBottom.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));

        JPanel pnlTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblTotalLabel = new JLabel("TỔNG CỘNG:  ");
        lblTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTotalAmount = new JLabel("0 VNĐ");
        lblTotalAmount.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTotalAmount.setForeground(new Color(192, 57, 43));
        pnlTotal.add(lblTotalLabel);
        pnlTotal.add(lblTotalAmount);

        JPanel pnlPayMethod = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlPayMethod.add(new JLabel("Phương thức thanh toán:"));
        cboPaymentMethod = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản", "Thẻ tín dụng"});
        cboPaymentMethod.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlPayMethod.add(cboPaymentMethod);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnBack = new JButton("← Quay lại");
        btnBack.addActionListener(this);
        btnBack.setFocusPainted(false);

        btnConfirmPayment = new JButton("✅  Xác nhận thanh toán");
        btnConfirmPayment.setBackground(new Color(39, 174, 96));
        btnConfirmPayment.setForeground(Color.WHITE);
        btnConfirmPayment.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirmPayment.setFocusPainted(false);
        btnConfirmPayment.addActionListener(this);

        pnlButtons.add(btnBack);
        pnlButtons.add(btnConfirmPayment);

        pnlBottom.add(pnlTotal);
        pnlBottom.add(pnlPayMethod);
        pnlBottom.add(pnlButtons);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void loadOrderDetail() {
        tableModel.setRowCount(0);
        if (order == null) {
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy đơn đặt món chưa thanh toán cho bàn này.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            btnConfirmPayment.setEnabled(false);
            return;
        }

        int stt = 1;
        int total = 0;

        for (OrderDish item : order.getOrderDishes()) {

            int thanhTien = item.getQuantity() * item.getCurrentPrice();
            total += thanhTien;

            tableModel.addRow(new Object[]{
                stt++,
                item.getDish().getName(),
                item.getDish().getCategory(),
                item.getQuantity(),
                String.format("%,d", item.getCurrentPrice()),
                String.format("%,d", thanhTien)
            });
        }

        order.setTotalAmount(total);

        lblTotalAmount.setText(String.format("%,d VNĐ", total));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnConfirmPayment)) {
            if (order == null) return;
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Xác nhận thanh toán " + String.format("%,d VNĐ", order.getTotalAmount()) + " ?",
                    "Xác nhận thanh toán", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            Bill bill = new Bill();
            bill.setCreatedTime(new Date());
            bill.setTotalAmount(order.getTotalAmount());
            bill.setPaymentMethod((String) cboPaymentMethod.getSelectedItem());
            bill.setOrder(order);
            bill.setUser(user);

            BillDAO billDAO = new BillDAO();
            if (billDAO.createBill(bill)) {

                if (order.getTable() != null) {
                    TableDAO tableDAO = new TableDAO();
                    tableDAO.updateTableStatus(order.getTable().getId(), "Trống");
                    tableDAO.checkoutTable(order.getTable().getId());
                }
                JOptionPane.showMessageDialog(this,
                        "Thanh toán thành công!\nMã hóa đơn: #" + bill.getId(),
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                new StaffHomeFrm(user).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Thanh toán thất bại! Vui lòng thử lại.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource().equals(btnBack)) {
            new SelectTableToPayFrm(user).setVisible(true);
            this.dispose();
        }
    }
}
