# Restaurant Module

README này dùng để clone project, tạo CSDL, chạy app, và hiểu rõ những thay đổi đã được tích hợp thêm vào project gốc.

## 1. Mục tiêu hiện tại

Project hiện tại gồm 2 nhóm chức năng:

- Staff module của project nhóm gốc:
  - đặt bàn
  - tìm bàn trống
  - tìm và thêm khách hàng cho booking
  - sửa booking
  - gọi món
  - xác nhận order
- Manager module được tích hợp thêm:
  - login
  - phân quyền `MANAGER` / `STAFF`
  - quản lý khách hàng
  - quản lý nhân viên

Luồng chạy hiện tại:

- `MANAGER` đăng nhập -> vào màn hình manager
- `STAFF` đăng nhập -> vào `StaffHomeFrm`

## 2. Yêu cầu môi trường

Cần có các thành phần sau:

- Windows
- JDK 17 hoặc mới hơn
- MySQL Server đang chạy local (cổng mặc định 3306)
- MySQL Workbench hoặc công cụ quản trị MySQL tương đương

Project đã kèm sẵn JDBC driver MySQL (`mysql-connector-j-9.7.0.jar`) trong thư mục `lib`, nên không cần cài thêm thư viện JDBC bằng tay để chạy app.

## 3. Cấu hình CSDL

Project đang dùng database:

- `restaurant_db`

Project đang kết nối MySQL local theo thứ tự ưu tiên sau:

1. JDBC URL được override bằng system property hoặc environment variable
2. `jdbc:mysql://localhost:3306/restaurant_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8`
3. `jdbc:mysql://127.0.0.1:3306/restaurant_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8`

Mặc định hiện tại cho MySQL login trong app:

- username: `root`
- password: `123456`

Nếu máy khác không có login này, có thể override trong môi trường chạy bằng cách thiết lập biến môi trường.

## 4. Tạo database trên máy mới

Mở MySQL client (ví dụ MySQL Workbench) và chạy:

```sql
CREATE DATABASE IF NOT EXISTS restaurant_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Nếu database đã tồn tại thì bỏ qua bước này.

## 5. Tạo bảng và seed dữ liệu

Trong MySQL client:

1. Chọn database `restaurant_db` bằng câu lệnh: `USE restaurant_db;`
2. Mở và chạy file database script (hoặc để ứng dụng tự động chạy `ensureManagerTables()` tạo các bảng cốt lõi khi kết nối lần đầu).
3. Đảm bảo cấu trúc cơ sở dữ liệu có đầy đủ các bảng:
   - `tblUser`
   - `tblClient`
   - `tblTable`
   - `tblDish`
   - `tblBooking`
   - `tblBookedTable`
   - `tblOrder`
   - `tblOrderDish`
   - `tblBill`

Các tài khoản mặc định được seed sẵn:
- user `admin / 123456` với role `MANAGER`
- user `staff01 / 123456` với role `STAFF`

## 6. Cách chạy app

Khuyến nghị dùng script thay vì bấm Run trực tiếp từ IDE.

### Cách 1: file batch

Chạy:

```bat
run-app.bat
```

### Cách 2: PowerShell

Chạy:

```powershell
powershell -ExecutionPolicy Bypass -File .\run-app.ps1
```

Script sẽ tự động:

1. xóa class cũ trong `target/classes`
2. compile lại toàn bộ source bằng `javac`
3. nạp JDBC driver MySQL từ thư mục `lib`
4. mở app

Nếu chỉ bấm Run trong IDE, có thể gặp lỗi classpath JDBC hoặc build class cũ. Nếu muốn an toàn, dùng script.

## 7. Tài khoản demo và kết quả mong đợi

Tài khoản demo:

- `admin / 123456`
- `staff01 / 123456`

Kết quả mong đợi:

- `admin / 123456` -> vào màn hình manager
- `staff01 / 123456` -> vào màn hình staff

Nếu login thất bại, kiểm tra theo thứ tự:

1. MySQL service đã chạy chưa (cổng 3306)
2. database `restaurant_db` đã tạo chưa
3. tài khoản MySQL `root` và mật khẩu `123456` có đúng không
4. app đang chạy bằng `run-app.bat` hoặc `run-app.ps1` chưa

## 8. Override thông tin kết nối

App hỗ trợ override bằng system property hoặc environment variable.

Override JDBC URL:

- system property: `restaurant.db.url`
- environment variable: `RESTAURANT_DB_URL`

Override username:

- system property: `restaurant.db.username`
- environment variable: `RESTAURANT_DB_USERNAME`

Override password:

- system property: `restaurant.db.password`
- environment variable: `RESTAURANT_DB_PASSWORD`

Ví dụ PowerShell:

```powershell
$env:RESTAURANT_DB_USERNAME="your_mysql_user"
$env:RESTAURANT_DB_PASSWORD="your_mysql_password"
powershell -ExecutionPolicy Bypass -File .\run-app.ps1
```

Nếu muốn override URL:

```powershell
$env:RESTAURANT_DB_URL="jdbc:mysql://localhost:3306/restaurant_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8"
powershell -ExecutionPolicy Bypass -File .\run-app.ps1
```

## 9. Những thay đổi đã được tích hợp so với project nhóm ban đầu

### 9.1. Phần đã có sẵn từ project nhóm gốc

Project nhóm ban đầu đã có staff UI và một số DAO/model cho:

- đặt bàn
- tìm bàn trống
- tìm khách hàng
- thêm khách hàng cho booking
- sửa booking
- gọi món
- xác nhận order

### 9.2. Phần được thêm vào

Đã thêm manager module:

- login
- phân quyền role
- manager home
- manage clients
- manage staff

Đã thêm service layer:

- `AuthService`
- `ClientService`
- `UserService`

Đã thêm DAO:

- `UserDAO`

Đã mở rộng DAO sẵn có:

- `ClientDAO` giữ chức năng staff cũ và thêm CRUD/search/soft delete cho manager

Đã mở rộng model dùng chung:

- `User`
- `Client`

Đã bổ sung schema MySQL tự động khởi tạo bảng.

Đã đổi entry point:

- trước đây main chỉ in `Hello World!`
- hiện tại main mở màn hình login

Đã đổi luồng điều hướng:

- `MANAGER` -> manager module
- `STAFF` -> staff module

### 9.3. Phần đã sửa để app chạy được trên máy thật

Đã sửa kết nối MySQL:

- hỗ trợ override bằng env var / system property

Đã thêm cơ chế tự nạp JDBC driver nếu IDE không cấp classpath đúng.

Đã thêm script chạy project để tránh tình trạng build xong nhưng vẫn nạp class cũ.

## 10. Các luồng cần test sau khi clone

Cần test tối thiểu:

1. login bằng `admin`
2. login bằng `staff01`
3. manager -> quản lý khách hàng
4. manager -> quản lý nhân viên
5. staff -> đặt bàn
6. staff -> sửa booking
7. staff -> gọi món

## 11. Các lưu ý cho thành viên khác

- Không xóa `tblUser`
- Không bỏ cột `status` của `tblClient`
- Không sửa `run-app.ps1` nếu chưa hiểu rõ cách build và classpath hiện tại
- Nếu thay đổi role/login flow thì phải sửa đồng bộ login + manager/staff navigation
- Nếu đổi tên bảng trong SQL thì phải sửa toàn bộ DAO liên quan

## 13. Cách xử lý khi máy khác vẫn không chạy

Nếu máy khác vẫn lỗi, cần gửi lại đầy đủ các thông tin sau:

1. Lỗi hiện trên hộp thoại hoặc stack trace
2. Đã chạy `schema_seed.sql` hay chưa
3. Có tạo SQL login `restaurant_app` hay không
4. SQL Server đang là default instance hay named instance
5. Đang chạy bằng `run-app.bat` hay bấm Run trong IDE

Chỉ cần 5 thông tin đó là đủ để debug nhanh.
