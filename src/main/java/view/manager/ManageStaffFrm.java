package view.manager;

import model.User;
import service.user.UserServiceImpl;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

public class ManageStaffFrm extends JFrame {
    private static final String[] STAFF_COLUMNS = {
            "ID", "Mã NV", "Tên đăng nhập", "Họ tên", "Vai trò", "Số điện thoại", "Email", "Trạng thái"
    };

    private final User currentUser;
    private final UserServiceImpl userService;
    private final JTextField searchField = new JTextField(24);
    private final DefaultTableModel tableModel = createTableModel();
    private final JTable staffTable = new JTable(tableModel);
    private List<User> displayedUsers = new ArrayList<>();

    public ManageStaffFrm(User currentUser) {
        super("Quản lý nhân viên");
        this.currentUser = currentUser;
        this.userService = new UserServiceImpl();
        configureFrm();
        buildContent();
        loadUsers();
    }

    private void configureFrm() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1060, 580);
        setLocationRelativeTo((Frame) null);
    }

    private void buildContent() {
        JPanel rootPanel = new JPanel(new BorderLayout(12, 12));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel titleLabel = new JLabel("Quản lý nhân viên đang hoạt động");
        titleLabel.setFont(titleLabel.getFont().deriveFont(20.0f));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton searchButton = new JButton("Tìm kiếm");
        JButton reloadButton = new JButton("Tải lại");
        searchPanel.add(new JLabel("Từ khóa:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(reloadButton);

        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        configureTable();

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton closeButton = new JButton("Đóng");
        actionPanel.add(addButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        actionPanel.add(closeButton);

        searchButton.addActionListener(event -> searchUsers());
        searchField.addActionListener(event -> searchUsers());
        reloadButton.addActionListener(event -> reloadUsers());
        addButton.addActionListener(event -> showAddDialog());
        editButton.addActionListener(event -> showEditDialog());
        deleteButton.addActionListener(event -> deleteSelectedUser());
        closeButton.addActionListener(event -> dispose());

        rootPanel.add(topPanel, BorderLayout.NORTH);
        rootPanel.add(new JScrollPane(staffTable), BorderLayout.CENTER);
        rootPanel.add(actionPanel, BorderLayout.SOUTH);
        add(rootPanel);
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(STAFF_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void configureTable() {
        staffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        staffTable.setDefaultEditor(Object.class, null);
        staffTable.setAutoCreateRowSorter(true);
        staffTable.getTableHeader().setReorderingAllowed(false);
        staffTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    showEditDialog();
                }
            }
        });
    }

    private void loadUsers() {
        try {
            setUsers(userService.getActiveUsers());
        } catch (Exception exception) {
            showDatabaseError("Không thể tải danh sách nhân viên", exception);
        }
    }

    private void searchUsers() {
        try {
            setUsers(userService.searchActiveUsers(searchField.getText()));
        } catch (Exception exception) {
            showDatabaseError("Không thể tìm kiếm nhân viên", exception);
        }
    }

    private void reloadUsers() {
        searchField.setText("");
        loadUsers();
    }

    private void setUsers(List<User> users) {
        displayedUsers = new ArrayList<>(users);
        tableModel.setRowCount(0);
        for (User user : displayedUsers) {
            tableModel.addRow(new Object[] {
                    user.getId(),
                    safeText(user.getUserCode()),
                    safeText(user.getUsername()),
                    safeText(user.getName()),
                    safeText(user.getRole()),
                    safeText(user.getPhone()),
                    safeText(user.getEmail()),
                    safeText(user.getStatus())
            });
        }
    }

    private void showAddDialog() {
        StaffFormDialog dialog = new StaffFormDialog(this, userService);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadUsers();
            JOptionPane.showMessageDialog(this, "Đã thêm nhân viên thành công.");
        }
    }

    private void showEditDialog() {
        User selectedUser = getSelectedUser();
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để sửa.");
            return;
        }

        StaffFormDialog dialog = new StaffFormDialog(this, userService, selectedUser);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadUsers();
            JOptionPane.showMessageDialog(this, "Đã cập nhật nhân viên thành công.");
        }
    }

    private void deleteSelectedUser() {
        User selectedUser = getSelectedUser();
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để xóa.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa nhân viên \"" + selectedUser.getName() + "\"?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            userService.softDeleteUser(selectedUser.getId(), currentUser);
            loadUsers();
            JOptionPane.showMessageDialog(this, "Đã xóa nhân viên khỏi danh sách đang hoạt động.");
        } catch (Exception exception) {
            showDatabaseError("Không thể xóa nhân viên", exception);
        }
    }

    private User getSelectedUser() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        int modelRow = staffTable.convertRowIndexToModel(selectedRow);
        return modelRow >= 0 && modelRow < displayedUsers.size() ? displayedUsers.get(modelRow) : null;
    }

    private String safeText(String value) {
        return value == null ? "" : value;
    }

    private void showDatabaseError(String message, Exception exception) {
        JOptionPane.showMessageDialog(this, message + ": " + exception.getMessage(),
                "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
    }
}
