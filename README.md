# HỆ THỐNG QUẢN LÝ NHÀ HÀNG (RESTAURANT MANAGEMENT SYSTEM)

Dự án được xây dựng nhằm tối ưu hóa quy trình vận hành, gọi món và quản lý doanh thu cho các nhà hàng quy mô vừa và lớn.

---

## I. Kỹ thuật/công nghệ sử dụng

*   **Ngôn ngữ lập trình:** Java (JDK 17 trở lên)
*   **Công nghệ giao diện:** Java Swing (GUI)
*   **Cơ sở dữ liệu:** MySQL (Cổng mặc định: 3306)
*   **Trình quản lý dự án:** Maven
*   **Thư viện kết nối:** MySQL Connector/J (`mysql-connector-j-9.7.0.jar`) đi kèm sẵn trong thư mục `lib/`

---

## II. Giới thiệu bài toán và các chức năng

Dự án giải quyết bài toán quản lý quy trình khép kín tại nhà hàng từ khâu đón tiếp khách hàng, gọi món cho đến thanh toán hóa đơn và thống kê báo cáo doanh số cho cấp quản lý.

### 1. Phân hệ Nhân viên (Staff Module)
*   **Tìm kiếm bàn trống:** Kiểm tra tình trạng bàn ăn theo thời gian thực (ngày, giờ, sức chứa).
*   **Đặt bàn trước (Booking):** Đặt giữ chỗ cho khách hàng, liên kết thông tin khách hàng và vị trí bàn được chỉ định.
*   **Gọi món (Order):** Lên thực đơn gọi món trực tiếp khi khách tại bàn, hỗ trợ cập nhật danh sách món ăn đang phục vụ.
*   **Thanh toán hóa đơn (Billing):** Tính tiền dựa trên các món thực tế đã gọi, cập nhật trạng thái bàn sau khi thanh toán thành công và in hóa đơn bán hàng.

### 2. Phân hệ Quản lý (Manager Module)
*   **Đăng nhập & Phân quyền:** Xác thực người dùng và chuyển hướng giao diện phù hợp với vai trò `MANAGER` hoặc `STAFF`.
*   **Quản lý danh mục:** Thực hiện các tác vụ CRUD (Thêm, Sửa, Xóa mềm) đối với Khách hàng, Nhân viên, Bàn ăn và Món ăn.
*   **Báo cáo thống kê:**
    *   Thống kê món ăn bán chạy nhất (Best-selling) theo doanh thu và số lượng.
    *   Báo cáo tổng doanh thu của nhà hàng theo khoảng thời gian, tháng cụ thể hoặc theo khung giờ hoạt động.

---

## III. Giới thiệu cách tổ chức nhóm

Nhóm phát triển dự án gồm **03 thành viên** với sự phân chia vai trò rõ ràng như sau:

1.  **Thành viên A (Trưởng nhóm):** Thiết kế cấu trúc cơ sở dữ liệu MySQL, xây dựng các lớp xử lý DAO chung (`DAO`, `UserDAO`, `ClientDAO`), phát triển và kiểm thử nghiệp vụ cho Phân hệ Quản lý (Manager Module).
2.  **Thành viên B:** Thiết kế toàn bộ giao diện đồ họa Java Swing (`view/`), phát triển luồng tương tác thực tế cho Phân hệ Nhân viên (Staff Module: Đặt bàn, Gọi món, Thanh toán).
3.  **Thành viên C:** Xây dựng bộ dữ liệu kiểm thử tự động (Unit Test / Integration Test), chuẩn bị tài liệu dự án, kịch bản thuyết trình và hướng dẫn triển khai.

---

## IV. Cách chạy ở trong máy

### 1. Chuẩn bị
*   Đảm bảo máy đã cài đặt **Java (JDK 17+)** và **MySQL Server**.
*   Mở MySQL client, tạo database bằng câu lệnh:
    ```sql
    CREATE DATABASE IF NOT EXISTS restaurant_db CHARACTER SET utf8mb4;
    ```
*   Ứng dụng sẽ tự động khởi tạo các bảng và dữ liệu mẫu (Seed Data) ở lần đầu tiên kết nối. Tài khoản quản trị mặc định: `admin / 123456`, nhân viên: `staff01 / 123456`.

### 2. Chạy ứng dụng
Khuyến nghị sử dụng các file script đi kèm để tự động compile và cấu hình classpath JDBC MySQL chuẩn xác:

*   **Cách 1 (Sử dụng File Batch):** Double click trực tiếp vào file `run-app.bat` tại thư mục gốc của dự án.
*   **Cách 2 (Sử dụng PowerShell):** Mở cửa sổ PowerShell tại thư mục dự án và chạy câu lệnh:
    ```powershell
    powershell -ExecutionPolicy Bypass -File .\run-app.ps1
    ```

*Lưu ý: Mặc định ứng dụng sử dụng cấu hình kết nối tài khoản MySQL local là `root` và mật khẩu `123456`. Để thay đổi, bạn có thể thiết lập các biến môi trường hệ thống trước khi chạy:*
*   `RESTAURANT_DB_USERNAME` (Tên tài khoản MySQL)
*   `RESTAURANT_DB_PASSWORD` (Mật khẩu tài khoản MySQL)
