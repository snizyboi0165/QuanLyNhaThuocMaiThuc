# 🏥 Mai Thức Pharmacy Management System (Quản Lý Hiệu Thuốc Tây Mai Thức)

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Swing](https://img.shields.io/badge/GUI-Java%20Swing-blue?style=for-the-badge)](https://en.wikipedia.org/wiki/Swing_(Java))
[![FlatLaf](https://img.shields.io/badge/UI%20Theme-FlatLaf%203.0-4caf50?style=for-the-badge)](https://www.formdev.com/flatlaf/)
[![SQL Server](https://img.shields.io/badge/Database-SQL%20Server-CC292B?style=for-the-badge&logo=microsoftsqlserver&logoColor=white)](https://www.microsoft.com/sql-server)
[![iText](https://img.shields.io/badge/PDF-iText%205.5-red?style=for-the-badge)](https://itextpdf.com/)
[![Architecture](https://img.shields.io/badge/Architecture-MVC%20%2F%20DAO-orange?style=for-the-badge)](#kiến-trúc-hệ-thống)

> **Hệ thống phần mềm máy tính (Desktop Application) chuyên nghiệp dành cho việc quản lý toàn diện các hoạt động vận hành tại nhà thuốc tây: từ bán hàng, quản lý kho thuốc theo lô/hạn dùng, nhập hàng, quản lý đổi trả, đến báo cáo thống kê doanh thu và phân quyền nhân sự.**

---

## 📌 Giới thiệu dự án

**Quản Lý Hiệu Thuốc Tây Mai Thức** được thiết kế nhằm giải quyết bài toán quản lý thuốc phức tạp tại các quầy thuốc và chuỗi bán lẻ dược phẩm. Hệ thống tự động hóa các quy trình từ nhập kho, phân loại thuốc theo danh mục, quản lý lô hạn dùng, xuất hóa đơn VAT, tích hợp chương trình khuyến mãi, đến quản lý đổi trả hàng và phân quyền bảo mật chặt chẽ giữa Quản lý và Nhân viên bán hàng.

Giao diện người dùng được hiện đại hóa với thư viện **FlatLaf** kết hợp đồ họa vector **SVG**, mang lại trải nghiệm mượt mà, trực quan và đáp ứng linh hoạt theo độ phân giải màn hình.

---

## ✨ Tính năng nổi bật

### 1. 🛒 Quản lý Bán hàng & Lập Hóa đơn (POS)
- **Lập hóa đơn nhanh chóng:** Tìm kiếm thuốc theo tên, mã hoặc quét mã; tự động tính giá bán theo đơn vị tính (viên, vỉ, hộp, chai,...).
- **Tự động áp dụng khuyến mãi & thuế:** Tính toán thuế VAT và giảm giá theo các chương trình khuyến mãi đang có hiệu lực.
- **Xuất và in hóa đơn PDF chuyên nghiệp:** Sử dụng thư viện **iTextPDF** để xuất hóa đơn bán hàng ra file PDF hoặc in trực tiếp.
- **Quản lý phiếu đặt thuốc:** Tạo và theo dõi các đơn đặt hàng thuốc của khách hàng trước khi xuất bán chính thức.

### 2. 🔄 Quản lý Đổi - Trả thuốc nâng cao
- Hỗ trợ nghiệp vụ đổi thuốc lỗi, đổi thuốc theo yêu cầu hoặc trả hàng hoàn tiền.
- Kiểm tra tính hợp lệ của hóa đơn cũ, tự động hoàn trả số lượng vào kho và điều chỉnh tiền chênh lệch chính xác.

### 3. 💊 Quản lý Thuốc & Kho Dược phẩm theo Lô
- **Quản lý chi tiết danh mục thuốc:** Hoạt chất, thành phần, công dụng, chống chỉ định, dạng bào chế, đơn vị tính.
- **Quản lý theo Lô (`LoThuoc`):** Theo dõi số lô, ngày sản xuất, hạn sử dụng để đưa ra cảnh báo thuốc sắp hết hạn (FEFO/FIFO).
- **Nhập hàng & Quản lý Nhà cung cấp:** Lập phiếu nhập kho từ nhà cung cấp (`PhieuNhapThuoc`), tự động cập nhật số lượng tồn kho và giá vốn.

### 4. 👥 Quản lý Khách hàng & Nhân sự (RBAC)
- **Phân quyền người dùng (Role-Based Access Control):**
  - 👑 **Nhân viên quản lý:** Toàn quyền truy cập quản lý nhân sự, phân quyền tài khoản, cấu hình thuế, khuyến mãi, quản lý nhà cung cấp và xem báo cáo tài chính.
  - 👨‍⚕️ **Nhân viên bán thuốc:** Giới hạn nghiệp vụ bán lẻ, lập hóa đơn, tra cứu thuốc, xem thông tin khuyến mãi.
- **Quản lý khách hàng:** Lưu trữ thông tin khách hàng, số điện thoại và lịch sử giao dịch.

### 5. 📈 Báo cáo & Thống kê Doanh thu
- Biểu đồ và bảng thống kê doanh thu, lợi nhuận theo ngày, tháng, năm.
- Thống kê danh sách thuốc bán chạy, thuốc tồn kho lâu ngày và danh sách thuốc cận hạn sử dụng.

---

## 🏛️ Kiến trúc hệ thống & Công nghệ

Dự án áp dụng mô hình phân tầng **Multi-tier (MVC / DAO Pattern)** giúp tách biệt logic nghiệp vụ, giao diện và truy xuất dữ liệu:

```
QuanLyNhaThuocMaiThuc/
├── src/
│   ├── ConnectDB/          # Quản lý kết nối Database (Singleton Pattern)
│   ├── entity/             # Data Models (Thuoc, LoThuoc, HoaDon, NhanVien, ...)
│   ├── dao/                # Data Access Objects (Thực thi truy vấn SQL / CRUD)
│   ├── gui/                
│   │   ├── form/           # Các màn hình chính (Lập HĐ, Bán hàng, Thống kê, ...)
│   │   └── dialog/         # Hộp thoại Modal (Thêm/Sửa, Chi tiết, Thanh toán, ...)
│   ├── utils/              # Tiện ích bổ trợ (ImageHelper, TableUtils, ...)
│   └── img/                # Tài nguyên đồ họa (Icons SVG, ảnh nền)
├── sql/                    # Script khởi tạo Database & Dữ liệu mẫu
└── lib/                    # Các thư viện phụ thuộc (JAR files)
```

### Công nghệ sử dụng:
| Phân hệ | Công nghệ / Thư viện | Mô tả |
|---|---|---|
| **Core Language** | Java (JDK 11 / 17+) | Ngôn ngữ hướng đối tượng chính |
| **GUI Framework** | Java Swing + AWT | Nền tảng giao diện Desktop |
| **UI Look & Feel** | FlatLaf 3.0 + Roboto Font | Giao diện phẳng, hiện đại và thanh lịch |
| **Icon & Graphics**| svgSalamander 1.1.4 | Hiển thị icon vector SVG sắc nét trên mọi tỷ lệ màn hình |
| **Database** | Microsoft SQL Server | Hệ quản trị cơ sở dữ liệu quan hệ |
| **Connection** | JDBC + Singleton Pattern | Kết nối cơ sở dữ liệu an toàn, đóng mở kết nối chuẩn |
| **PDF Export** | iText 5.5.4 | Tạo và xuất hóa đơn PDF chuyên nghiệp |
| **Component** | JCalendar 1.4 | Bộ chọn ngày tháng tương tác trực quan |

---

## 🗄️ Thiết kế Cơ sở dữ liệu

Hệ thống được chuẩn hóa với hơn **17 thực thể quan hệ chặt chẽ**:
- `NhanVien`, `TaiKhoan`: Quản lý thông tin và phân quyền đăng nhập.
- `KhachHang`, `NhaCungCap`: Quản lý đối tác và khách mua lẻ.
- `DanhMucThuoc`, `Thuoc`, `LoThuoc`: Phân loại và quản lý hạn sử dụng của thuốc.
- `HoaDon`, `ChiTietHoaDon`: Nghiệp vụ bán hàng và xuất phiếu.
- `PhieuNhapThuoc`, `ChiTietPhieuNhap`: Nghiệp vụ nhập kho.
- `PhieuDatThuoc`, `ChiTietPhieuDatThuoc`: Quản lý đơn đặt trước.
- `PhieuDoiTra`, `ChiTietPhieuDoiTra`: Quản lý đổi trả và hoàn tiền.
- `KhuyenMai`, `Thue`: Quản lý chính sách giá và chiết khấu.

---

## 🚀 Hướng dẫn cài đặt & Chạy ứng dụng

### Yêu cầu hệ thống (Prerequisites)
- **Java Development Kit (JDK):** Phiên bản 11, 17 hoặc mới hơn.
- **Cơ sở dữ liệu:** Microsoft SQL Server (2014 trở lên).
- **IDE đề xuất:** Eclipse IDE, IntelliJ IDEA, hoặc Apache NetBeans.

### Các bước cài đặt chi tiết

#### Bước 1: Khởi tạo Cơ sở dữ liệu
1. Mở **SQL Server Management Studio (SSMS)**.
2. Mở file script `sql/QuanLyHieuThuocMaiThuc2.sql` trong thư mục dự án.
3. Nhấn `Execute` (F5) để tạo Database `QLHieuThuocTayMaiThuc` cùng các bảng và dữ liệu mẫu.

#### Bước 2: Cấu hình kết nối Cơ sở dữ liệu
Mở file `src/ConnectDB/DatabaseConnection.java` và điều chỉnh thông tin tài khoản SQL Server của máy bạn:
```java
private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QLHieuThuocTayMaiThuc;encrypt=true;trustServerCertificate=true";
private static final String USER = "sa";              // Thay đổi nếu cần
private static final String PASSWORD = "your_password"; // Mật khẩu sa của bạn
```

#### Bước 3: Thêm thư viện (JAR Files)
Đảm bảo tất cả các file `.jar` trong thư mục `lib/` đã được thêm vào **Build Path / Project Libraries** của IDE:
- `flatlaf-3.0.jar`
- `flatlaf-extras-3.1.jar`
- `flatlaf-fonts-roboto-2.137.jar`
- `itextpdf-5.5.4.jar`
- `jcalendar-1.4.jar`
- `sqljdbc4.jar` (hoặc mssql-jdbc mới hơn nếu dùng Java 17)
- `svgSalamander-1.1.4.jar`

#### Bước 4: Khởi chạy chương trình
1. Tìm class `gui.form.Login.java`.
2. Chạy hàm `main()` để mở màn hình Đăng nhập.
3. Đăng nhập bằng tài khoản mẫu trong cơ sở dữ liệu để trải nghiệm hệ thống.

---

## 👨‍💻 Tác giả & Giấy phép
- **Dự án:** Phát triển ứng dụng - Khoa Công nghệ Thông tin (IUH).
- **Phát triển bởi:** Nhóm sinh viên thực hiện.
- **License:** Dự án phục vụ mục đích học tập và làm Portfolio cá nhân.