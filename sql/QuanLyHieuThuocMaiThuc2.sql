use master
GO

-- Drop database nếu đã tồn tại
IF EXISTS (SELECT name FROM sys.databases WHERE name = N'QLHieuThuocTayMaiThuc')
BEGIN
    ALTER DATABASE QLHieuThuocTayMaiThuc SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QLHieuThuocTayMaiThuc;
END
GO

CREATE DATABASE QLHieuThuocTayMaiThuc
GO

use QLHieuThuocTayMaiThuc
GO

-- =============================================
-- TẠO CÁC BẢNG
-- =============================================

CREATE TABLE NhaCungCap (
    maNCC NVARCHAR(20) PRIMARY KEY,
    tenNCC NVARCHAR(100) NOT NULL,
    soDienThoai NVARCHAR(15),
    daXoa BIT DEFAULT 0 NOT NULL
);

CREATE TABLE NhanVien (
    maNV NVARCHAR(20) PRIMARY KEY,
    hoTen NVARCHAR(100) NOT NULL,
    chucVu NVARCHAR(50) NOT NULL CHECK (chucVu IN (N'Nhân viên bán thuốc', N'Nhân viên quản lý')),
    soDienThoai NVARCHAR(15),
    ngaySinh DATE,
    gioiTinh NVARCHAR(10) CHECK (gioiTinh IN (N'Nam', N'Nữ')),
    diaChi NVARCHAR(200) NOT NULL,
    email NVARCHAR(100),
    daXoa BIT DEFAULT 0 NOT NULL
);

CREATE TABLE KhachHang (
    maKH NVARCHAR(20) PRIMARY KEY,
    hoTen NVARCHAR(100) NOT NULL,
    soDienThoai NVARCHAR(15),
    email NVARCHAR(100),
    daXoa BIT DEFAULT 0 NOT NULL
);

CREATE TABLE Thue (
    maThue NVARCHAR(20) PRIMARY KEY,
    tenThue NVARCHAR(100) NOT NULL,
    phanTramThue DECIMAL(5,2) CHECK (phanTramThue >= 0)
);

CREATE TABLE DanhMucThuoc (
    maDanhMuc NVARCHAR(20) PRIMARY KEY,
    tenDanhMuc NVARCHAR(100) NOT NULL
);

CREATE TABLE KhuyenMai (
    maKM NVARCHAR(20) PRIMARY KEY,
    tenKM NVARCHAR(100) NOT NULL,
    ngayBatDau DATE NOT NULL,
    ngayKetThuc DATE NOT NULL,
    phanTramGiamGia DECIMAL(5,2) CHECK (phanTramGiamGia > 0 AND phanTramGiamGia <= 100),
    lapHangNam BIT DEFAULT 1 NOT NULL,
    daXoa BIT DEFAULT 0 NOT NULL,
    CONSTRAINT CK_KhuyenMai_NgayKetThuc CHECK (ngayKetThuc >= ngayBatDau)
);

CREATE TABLE Thuoc (
    maThuoc NVARCHAR(20) PRIMARY KEY,
    tenThuoc NVARCHAR(100) NOT NULL,
    donViTinh NVARCHAR(20) NOT NULL,
    giaBan DECIMAL(18,2) CHECK (giaBan >= 0),
    moTa NVARCHAR(255) NOT NULL,
    maDanhMuc NVARCHAR(20),
    hinhAnh NVARCHAR(255) NOT NULL,
    thanhPhan NVARCHAR(255) NOT NULL,
    xuatXu NVARCHAR(100) NOT NULL,
    FOREIGN KEY (maDanhMuc) REFERENCES DanhMucThuoc(maDanhMuc)
);

CREATE TABLE PhieuNhapHang (
    maPhieuNhap NVARCHAR(20) PRIMARY KEY,
    maNV NVARCHAR(20) NOT NULL,
    maNCC NVARCHAR(20) NOT NULL,
    ngayNhap DATETIME NOT NULL,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maNCC) REFERENCES NhaCungCap(maNCC)
);

CREATE TABLE LoThuoc (
    maLo NVARCHAR(20) PRIMARY KEY,
    soLo NVARCHAR(50) NOT NULL,
    maThuoc NVARCHAR(20) NOT NULL,
    maPhieuNhap NVARCHAR(20) NOT NULL,
    ngaySanXuat DATE,
    hanSuDung DATE NOT NULL,
    soLuongNhap INT NOT NULL CHECK (soLuongNhap > 0),
    soLuongConLai INT NOT NULL CHECK (soLuongConLai >= 0),
    donGiaNhap DECIMAL(18,2) NOT NULL CHECK (donGiaNhap >= 0),
    trangThai NVARCHAR(30) DEFAULT N'Còn hàng'
        CHECK (trangThai IN (N'Còn hàng', N'Hết hàng', N'Hết hạn')),
    FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc),
    FOREIGN KEY (maPhieuNhap) REFERENCES PhieuNhapHang(maPhieuNhap),
    CONSTRAINT CK_LoThuoc_MaLo CHECK (maLo LIKE 'L[0-9][0-9][0-9][0-9]'),
    CONSTRAINT CK_LoThuoc_Ngay CHECK (ngaySanXuat IS NULL OR ngaySanXuat <= hanSuDung),
    CONSTRAINT CK_LoThuoc_SoLuong CHECK (soLuongConLai <= soLuongNhap)
);

CREATE TABLE ChiTietPhieuNhap (
    maPhieuNhap NVARCHAR(20),
    maLo NVARCHAR(20),
    soLuong INT NOT NULL CHECK (soLuong > 0),
    donGia DECIMAL(18,2) CHECK (donGia >= 0),
    PRIMARY KEY (maPhieuNhap, maLo),
    FOREIGN KEY (maPhieuNhap) REFERENCES PhieuNhapHang(maPhieuNhap),
    FOREIGN KEY (maLo) REFERENCES LoThuoc(maLo)
);

GO
CREATE VIEW vw_TonKhoThuoc AS
SELECT
    t.maThuoc,
    t.tenThuoc,
    t.donViTinh,
    t.giaBan,
    t.moTa,
    t.maDanhMuc,
    t.hinhAnh,
    t.thanhPhan,
    t.xuatXu,
    ISNULL(SUM(l.soLuongConLai), 0) AS soLuongTon,
    MIN(CASE WHEN l.soLuongConLai > 0 THEN l.hanSuDung END) AS hanSuDungGanNhat
FROM Thuoc t
LEFT JOIN LoThuoc l ON t.maThuoc = l.maThuoc
GROUP BY
    t.maThuoc, t.tenThuoc, t.donViTinh, t.giaBan,
    t.moTa, t.maDanhMuc, t.hinhAnh, t.thanhPhan, t.xuatXu;
GO

CREATE TABLE PhieuDatThuoc (
    maPhieuDat NVARCHAR(20) PRIMARY KEY,
    ngayDat DATETIME NOT NULL,
    maKH NVARCHAR(20) NOT NULL,
    maNV NVARCHAR(20),
    maThue NVARCHAR(20),
    maKM NVARCHAR(20),
    diaChi NVARCHAR(200) NOT NULL,
    hinhThucThanhToan NVARCHAR(50) CHECK (hinhThucThanhToan IN (N'Thanh toán online', N'Tại chỗ')),
    trangThai NVARCHAR(20) CHECK (trangThai IN (N'Đã hoàn thành', N'Chưa hoàn thành')),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maThue) REFERENCES Thue(maThue),
    FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM)
);

CREATE TABLE ChiTietPhieuDatThuoc (
    maPhieuDat NVARCHAR(20),
    maThuoc NVARCHAR(20),
    soLuong INT NOT NULL CHECK (soLuong > 0),
    donGia DECIMAL(18,2) CHECK (donGia >= 0),
    PRIMARY KEY (maPhieuDat, maThuoc),
    FOREIGN KEY (maPhieuDat) REFERENCES PhieuDatThuoc(maPhieuDat),
    FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc)
);

CREATE TABLE HoaDon (
    maHD NVARCHAR(20) PRIMARY KEY,
    ngayLap DATETIME NOT NULL,
    maThue NVARCHAR(20),
    maNV NVARCHAR(20),
    maKH NVARCHAR(20),
    maKM NVARCHAR(20),
    maPhieuDat NVARCHAR(20),
    FOREIGN KEY (maThue) REFERENCES Thue(maThue),
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM),
    FOREIGN KEY (maPhieuDat) REFERENCES PhieuDatThuoc(maPhieuDat)
);

CREATE TABLE ChiTietHoaDon (
    maHD NVARCHAR(20),
    maThuoc NVARCHAR(20),
    soLuong INT NOT NULL CHECK (soLuong >= 0),
    donGia DECIMAL(18,2) CHECK (donGia >= 0),
    PRIMARY KEY (maHD, maThuoc),
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc)
);

CREATE TABLE TaiKhoan (
    tenDangNhap NVARCHAR(50) PRIMARY KEY,
    matKhau NVARCHAR(100) NOT NULL,
    trangThai NVARCHAR(20) CHECK (trangThai IN (N'Hoạt động', N'Ngừng hoạt động')),
    maNV NVARCHAR(20) UNIQUE,
    vaiTro NVARCHAR(50) CHECK (vaiTro IN (N'Nhân viên bán thuốc', N'Nhân viên quản lý')),
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);

GO
ALTER TABLE HoaDon
ALTER COLUMN maKH NVARCHAR(20) NULL;

ALTER TABLE HoaDon
ALTER COLUMN maPhieuDat NVARCHAR(20) NULL;

GO
-- =============================================
-- INSERT DỮ LIỆU
-- =============================================

-- NhaCungCap (NCC00001, NCC00002, ...)
INSERT INTO NhaCungCap (maNCC, tenNCC, soDienThoai) VALUES
(N'NCC00001', N'Công ty Dược Hòa Bình', '0912345678'),
(N'NCC00002', N'Công ty Dược Bình Minh', '0987654321'),
(N'NCC00003', N'Công ty Dược An Khang', '0909999999'),
(N'NCC00004', N'Công ty Dược Phẩm Việt', '0918888888'),
(N'NCC00005', N'Công ty TNHH Dược Quốc Tế', '0927777777');

-- NhanVien (NV00001, NV00002, ...) - CHỈ CÓ 2 CHỨC VỤ
INSERT INTO NhanVien (maNV, hoTen, chucVu, soDienThoai, ngaySinh, gioiTinh, diaChi, email, daXoa) VALUES
(N'NV00001', N'Nguyễn Văn An', N'Nhân viên quản lý', '0911111111', '1990-05-10', N'Nam', N'123 Lê Lợi, Q1, TP.HCM', 'vanan@gmail.com', 0),
(N'NV00002', N'Trần Thị Bình', N'Nhân viên bán thuốc', '0922222222', '1995-03-15', N'Nữ', N'456 Nguyễn Huệ, Q1, TP.HCM', 'thibinh@gmail.com', 0),
(N'NV00003', N'Lê Văn Cường', N'Nhân viên bán thuốc', '0933333333', '1988-07-20', N'Nam', N'789 Trần Hưng Đạo, Q5, TP.HCM', 'vancuong@gmail.com', 0),
(N'NV00004', N'Phạm Thị Dung', N'Nhân viên bán thuốc', '0944444444', '1998-11-25', N'Nữ', N'321 Võ Văn Tần, Q3, TP.HCM', 'thidung@gmail.com', 0),
(N'NV00005', N'Hoàng Minh Em', N'Nhân viên quản lý', '0955555555', '1992-09-08', N'Nam', N'654 Lý Thường Kiệt, Q10, TP.HCM', 'minhem@gmail.com', 0);

-- KhachHang (KH00001, KH00002, ...)
INSERT INTO KhachHang (maKH, hoTen, soDienThoai, email) VALUES
(N'KH00001', N'Phạm Thị Diệu', '0961111111', 'dieu@gmail.com'),
(N'KH00002', N'Ngô Văn Em', '0962222222', 'em@gmail.com'),
(N'KH00003', N'Võ Thị Phương', '0963333333', 'phuong@gmail.com'),
(N'KH00004', N'Đặng Quốc Gia', '0964444444', 'gia@gmail.com'),
(N'KH00005', N'Trương Thị Hà', '0965555555', 'ha@gmail.com'),
(N'KH00006', N'Lý Văn Hùng', '0966666666', 'hung@gmail.com'),
(N'KH00007', N'Bùi Thị Lan', '0967777777', 'lan@gmail.com'),
(N'KH00008', N'Nguyễn Minh Khoa', '0968888888', 'khoa@gmail.com'),
(N'KH00009', N'Lê Thanh Mai', '0969999999', 'mai@gmail.com'),
(N'KH00010', N'Phan Quốc Bảo', '0970000000', 'bao@gmail.com');

-- Thue (TE00001, TE00002, ...)
INSERT INTO Thue (maThue, tenThue, phanTramThue) VALUES
(N'TE00001', N'Thuế VAT 5%', 5.00)

-- DanhMucThuoc (DM00001, DM00002, ...)
INSERT INTO DanhMucThuoc (maDanhMuc, tenDanhMuc) VALUES
(N'DM00001', N'Giảm đau - Hạ sốt'),
(N'DM00002', N'Tiêu hóa'),
(N'DM00003', N'Vitamin - Khoáng chất'),
(N'DM00004', N'Thực phẩm chức năng'),
(N'DM00005', N'Tim mạch'),
(N'DM00006', N'Kháng sinh'),
(N'DM00007', N'Hô hấp'),
(N'DM00008', N'Chống dị ứng'),
(N'DM00009', N'Sát khuẩn - Khử trùng');

-- KhuyenMai (KM00001, KM00002, ...)
INSERT INTO KhuyenMai (maKM, tenKM, ngayBatDau, ngayKetThuc, phanTramGiamGia, lapHangNam) VALUES
(N'KM00001', N'Khuyến mãi Tết Nguyên Đán', '2026-01-01', '2026-01-31', 25.00, 1),
(N'KM00002', N'Khuyến mãi mùa hè', '2026-04-30', '2026-06-30', 15.00, 1),
(N'KM00003', N'Khuyến mãi 30/4 - 1/5', '2026-04-28', '2026-05-03', 20.00, 1),
(N'KM00004', N'Khuyến mãi Black Friday', '2026-11-25', '2026-11-30', 25.00, 1);

-- Thuoc (TH00001, TH00002, ...)
INSERT INTO Thuoc (maThuoc, tenThuoc, donViTinh, giaBan, moTa, maDanhMuc, hinhAnh, thanhPhan, xuatXu) VALUES
(N'TH00001', N'Hapacol 650 DHG', N'Hộp 10 vỉ x 10 viên', 25000, N'Thuốc giảm đau, hạ sốt hiệu quả', N'DM00001', N'img_product/hapacol_650_extra_dhg.png', N'Paracetamol 650mg', N'Việt Nam'),
(N'TH00002', N'Bột pha hỗn dịch uống Smecta vị cam', N'Hộp 10 gói', 4000, N'Điều trị tiêu chảy cấp và mạn tính', N'DM00002', N'img_product/bot-pha-hon-dich-uong-smecta.jpg', N'Diosmectite 3g', N'Pháp'),
(N'TH00003', N'Siro C.C Life 100mg/5ml Foripharm', N'Chai 120ml', 30000, N'Bổ sung vitamin C cho cơ thể', N'DM00003', N'img_product/C.c-Life-100MgChai.jpg', N'Vitamin C 100mg/5ml', N'Việt Nam'),
(N'TH00004', N'Panadol Extra đỏ', N'Hộp 12 vỉ x 10 viên', 250000, N'Giảm đau nhanh, hạ sốt hiệu quả', N'DM00001', N'img_product/Panadol-Extra.png', N'Paracetamol 500mg, Caffeine 65mg', N'Anh'),
(N'TH00005', N'Viên sủi Vitatrum C BRV', N'Tuýp 20 viên', 24000, N'Bổ sung vitamin C, tăng cường đề kháng', N'DM00003', N'img_product/vitatrum-c-brv.png', N'Vitamin C 1000mg', N'Việt Nam'),
(N'TH00006', N'Bổ Gan Trường Phúc', N'Hộp 30 viên', 95000, N'Hỗ trợ bảo vệ và phục hồi chức năng gan', N'DM00004', N'img_product/bo-gan-tuong-phu.jpg', N'Diệp hạ châu, Đảng Sâm, Bạch truật, Cam thảo, Phục Linh, Nhân trần, Trần bì', N'Việt Nam'),
(N'TH00007', N'Bài Thạch Trường Phúc', N'Hộp 30 viên', 95000, N'Hỗ trợ điều trị sỏi thận, sỏi mật', N'DM00004', N'img_product/bai-trang-truong-phuc.jpg', N'Xa tiền tử, Bạch mao căn, Sinh Địa, Ý Dĩ, Kim tiền thảo', N'Việt Nam'),
(N'TH00008', N'Đại Tràng Trường Phúc', N'Hộp 30 viên', 105000, N'Hỗ trợ điều trị viêm đại tràng, rối loạn tiêu hóa', N'DM00004', N'img_product/dai-trang-truong-phuc.jpg', N'Hoàng liên, Mộc hương, Bạch truật, Bạch thược, Ngũ bội tử, Hậu phác, Cam thảo, Xa tiền tử, Hoạt thạch', N'Việt Nam'),
(N'TH00009', N'Ninh Tâm Vương Hồng Bàng', N'Hộp 60 viên', 180000, N'Hỗ trợ tim mạch, giảm stress, cải thiện giấc ngủ', N'DM00005', N'img_product/ninh-tam-vuong-hong-bang.png', N'L-Carnitine, Taurine, Đan sâm, Khổ sâm bắc, Nattokinase, Hoàng đằng, Magie', N'Việt Nam'),
(N'TH00010', N'Paracetamol 500mg', N'Hộp 10 vỉ x 10 viên', 15000, N'Thuốc giảm đau, hạ sốt phổ biến', N'DM00001', N'img_product/paracetamol.png', N'Paracetamol 500mg', N'Việt Nam'),
(N'TH00011', N'Amoxicillin 500mg', N'Hộp 10 vỉ x 10 viên', 45000, N'Kháng sinh điều trị nhiễm khuẩn đường hô hấp', N'DM00006', N'img_product/amoxicillin.png', N'Amoxicillin 500mg', N'Ấn Độ'),
(N'TH00012', N'ORS', N'Hộp 20 gói', 50000, N'Bù nước và điện giải khi tiêu chảy', N'DM00002', N'img_product/ors.png', N'Natri clorua, Kali clorua, Natri citrat, Glucose', N'Thái Lan'),
(N'TH00013', N'Vitamin B Complex', N'Chai 100 viên', 85000, N'Bổ sung vitamin B tổng hợp', N'DM00003', N'img_product/vitamin-b.png', N'Vitamin B1, B2, B6, B12', N'Mỹ'),
(N'TH00014', N'Tây Thi Hoàn', N'Hộp 10 viên', 120000, N'Hỗ trợ tiêu hóa, giảm đầy hơi, khó tiêu', N'DM00004', N'img_product/tay-thi-hoan.png', N'Trần bì, Bạch truật, Hậu phác, Mộc hương', N'Trung Quốc'),
(N'TH00015', N'Eugica', N'Hộp 20 gói x 5ml', 85000, N'Siro ho, long đờm cho trẻ em', N'DM00007', N'img_product/eugica.png', N'Cao lá bạc hà, Cao lá khuynh diệp, Tinh dầu húng', N'Việt Nam'),
(N'TH00016', N'Cetirizine 10mg', N'Hộp 10 vỉ x 10 viên', 35000, N'Thuốc chống dị ứng', N'DM00008', N'img_product/cetirizine.png', N'Cetirizine dihydrochloride 10mg', N'Việt Nam'),
(N'TH00017', N'Glucosamine 1500mg', N'Hộp 30 gói', 420000, N'Hỗ trợ xương khớp', N'DM00004', N'img_product/glucosamine.png', N'Glucosamine sulfate 1500mg', N'Hàn Quốc'),
(N'TH00018', N'Omega 3 Fish Oil', N'Chai 100 viên', 350000, N'Bổ sung Omega 3 tốt cho tim mạch', N'DM00003', N'img_product/omega3.png', N'EPA 180mg, DHA 120mg', N'Na Uy'),
(N'TH00019', N'Betadine 10%', N'Chai 125ml', 65000, N'Dung dịch sát khuẩn vết thương', N'DM00009', N'img_product/betadine.png', N'Povidone Iodine 10%', N'Thái Lan'),
(N'TH00020', N'Cảm Xuyên Hương', N'Hộp 50 viên', 55000, N'Thuốc cảm cúm theo đông y', N'DM00004', N'img_product/cam-xuyen-huong.png', N'Hương nhu, Tử tô, Hoa cúc, Bạc hà', N'Việt Nam');

-- Tạo Phiếu Nhập Hàng 
INSERT INTO PhieuNhapHang (maPhieuNhap, maNV, maNCC, ngayNhap) VALUES
-- Tháng 12/2025
(N'PN00001', N'NV00003', N'NCC00001', '2025-12-01'),
(N'PN00002', N'NV00003', N'NCC00002', '2025-12-05'),
(N'PN00003', N'NV00002', N'NCC00003', '2025-12-09'),

-- Tháng 12/2025
(N'PN00004', N'NV00003', N'NCC00001', '2025-12-13'),
(N'PN00005', N'NV00002', N'NCC00002', '2025-12-17'),
(N'PN00006', N'NV00003', N'NCC00003', '2025-12-21'),

-- Tháng 12/2025
(N'PN00007', N'NV00003', N'NCC00001', '2025-12-24'),
(N'PN00008', N'NV00002', N'NCC00002', '2025-12-27'),
(N'PN00009', N'NV00003', N'NCC00003', '2025-12-30'),

-- Tháng 1/2026
(N'PN00010', N'NV00002', N'NCC00001', '2026-01-04'),
(N'PN00011', N'NV00003', N'NCC00002', '2026-01-10'),
(N'PN00012', N'NV00002', N'NCC00003', '2026-01-16'),

-- Tháng 1/2026
(N'PN00013', N'NV00003', N'NCC00001', '2026-01-22'),
(N'PN00014', N'NV00002', N'NCC00002', '2026-01-27'),
(N'PN00015', N'NV00003', N'NCC00003', '2026-01-31'),

-- Tháng 2/2026
(N'PN00016', N'NV00002', N'NCC00001', '2026-02-05'),
(N'PN00017', N'NV00003', N'NCC00002', '2026-02-11'),
(N'PN00018', N'NV00002', N'NCC00003', '2026-02-18'),

-- Tháng 2/2026
(N'PN00019', N'NV00003', N'NCC00001', '2026-02-23'),
(N'PN00020', N'NV00002', N'NCC00002', '2026-02-27'),
(N'PN00021', N'NV00003', N'NCC00003', '2026-02-28'),

-- Tháng 3/2026
(N'PN00022', N'NV00002', N'NCC00001', '2026-03-05'),
(N'PN00023', N'NV00003', N'NCC00002', '2026-03-12'),
(N'PN00024', N'NV00002', N'NCC00003', '2026-03-20'),

-- Tháng 3/2026
(N'PN00025', N'NV00003', N'NCC00001', '2026-03-24'),
(N'PN00026', N'NV00002', N'NCC00002', '2026-03-28'),
(N'PN00027', N'NV00003', N'NCC00003', '2026-03-31'),

-- Tháng 4/2026
(N'PN00028', N'NV00002', N'NCC00001', '2026-04-10'),
(N'PN00029', N'NV00003', N'NCC00002', '2026-04-20');

GO
-- Chi tiết phiếu nhập để đạt đúng số lượng tồn kho
-- Lô thuốc phát sinh từ dữ liệu chi tiết phiếu nhập cũ
INSERT INTO LoThuoc (maLo, soLo, maThuoc, maPhieuNhap, ngaySanXuat, hanSuDung, soLuongNhap, soLuongConLai, donGiaNhap) VALUES
(N'L0001', N'SL-TH00001-001', N'TH00001', N'PN00001', '2024-02-15', '2025-10-15', 300, 300, 20000),
(N'L0002', N'SL-TH00001-002', N'TH00001', N'PN00007', '2024-02-15', '2025-10-15', 400, 400, 21000),
(N'L0003', N'SL-TH00001-003', N'TH00001', N'PN00013', '2024-02-15', '2025-10-15', 321, 321, 22000),
(N'L0004', N'SL-TH00002-004', N'TH00002', N'PN00002', '2024-05-21', '2025-11-21', 500, 500, 3000),
(N'L0005', N'SL-TH00002-005', N'TH00002', N'PN00008', '2024-05-21', '2025-11-21', 321, 321, 3200),
(N'L0006', N'SL-TH00002-006', N'TH00002', N'PN00014', '2024-05-21', '2025-11-21', 200, 200, 3500),
(N'L0007', N'SL-TH00003-007', N'TH00003', N'PN00003', '2024-03-01', '2025-12-01', 400, 400, 25000),
(N'L0008', N'SL-TH00003-008', N'TH00003', N'PN00009', '2024-03-01', '2025-12-01', 332, 332, 26000),
(N'L0009', N'SL-TH00003-009', N'TH00003', N'PN00015', '2024-03-01', '2025-12-01', 300, 300, 27000),
(N'L0010', N'SL-TH00004-010', N'TH00004', N'PN00004', '2024-08-07', '2026-01-07', 500, 500, 240000),
(N'L0011', N'SL-TH00004-011', N'TH00004', N'PN00010', '2024-08-07', '2026-01-07', 334, 334, 245000),
(N'L0012', N'SL-TH00004-012', N'TH00004', N'PN00016', '2024-08-07', '2026-01-07', 200, 200, 248000),
(N'L0013', N'SL-TH00005-013', N'TH00005', N'PN00005', '2024-12-31', '2026-02-28', 400, 400, 20000),
(N'L0014', N'SL-TH00005-014', N'TH00005', N'PN00011', '2024-12-31', '2026-02-28', 376, 376, 21000),
(N'L0015', N'SL-TH00005-015', N'TH00005', N'PN00017', '2024-12-31', '2026-02-28', 300, 300, 22000),
(N'L0016', N'SL-TH00006-016', N'TH00006', N'PN00006', '2024-02-15', '2026-03-15', 500, 500, 85000),
(N'L0017', N'SL-TH00006-017', N'TH00006', N'PN00012', '2024-02-15', '2026-03-15', 334, 334, 88000),
(N'L0018', N'SL-TH00006-018', N'TH00006', N'PN00018', '2024-02-15', '2026-03-15', 200, 200, 90000),
(N'L0019', N'SL-TH00007-019', N'TH00007', N'PN00001', '2024-02-10', '2026-04-10', 400, 400, 85000),
(N'L0020', N'SL-TH00007-020', N'TH00007', N'PN00013', '2024-02-10', '2026-04-10', 376, 376, 88000),
(N'L0021', N'SL-TH00007-021', N'TH00007', N'PN00019', '2024-02-10', '2026-04-10', 300, 300, 90000),
(N'L0022', N'SL-TH00008-022', N'TH00008', N'PN00002', '2024-09-03', '2026-05-03', 350, 350, 95000),
(N'L0023', N'SL-TH00008-023', N'TH00008', N'PN00014', '2024-09-03', '2026-05-03', 371, 371, 98000),
(N'L0024', N'SL-TH00008-024', N'TH00008', N'PN00020', '2024-09-03', '2026-05-03', 300, 300, 100000),
(N'L0025', N'SL-TH00009-025', N'TH00009', N'PN00003', '2024-08-15', '2026-06-15', 400, 400, 170000),
(N'L0026', N'SL-TH00009-026', N'TH00009', N'PN00015', '2024-08-15', '2026-06-15', 354, 354, 175000),
(N'L0027', N'SL-TH00009-027', N'TH00009', N'PN00021', '2024-08-15', '2026-06-15', 300, 300, 178000),
(N'L0028', N'SL-TH00010-028', N'TH00010', N'PN00004', '2024-06-01', '2026-07-31', 600, 600, 12000),
(N'L0029', N'SL-TH00010-029', N'TH00010', N'PN00010', '2024-06-01', '2026-07-31', 700, 700, 12500),
(N'L0030', N'SL-TH00010-030', N'TH00010', N'PN00016', '2024-06-01', '2026-07-31', 700, 700, 13000),
(N'L0031', N'SL-TH00011-031', N'TH00011', N'PN00005', '2024-04-20', '2026-08-20', 500, 500, 35000),
(N'L0032', N'SL-TH00011-032', N'TH00011', N'PN00011', '2024-04-20', '2026-08-20', 500, 500, 37000),
(N'L0033', N'SL-TH00011-033', N'TH00011', N'PN00022', '2024-04-20', '2026-08-20', 500, 500, 38000),
(N'L0034', N'SL-TH00012-034', N'TH00012', N'PN00006', '2025-06-15', '2026-09-15', 300, 300, 45000),
(N'L0035', N'SL-TH00012-035', N'TH00012', N'PN00017', '2025-06-15', '2026-09-15', 300, 300, 46000),
(N'L0036', N'SL-TH00012-036', N'TH00012', N'PN00023', '2025-06-15', '2026-09-15', 200, 200, 48000),
(N'L0037', N'SL-TH00013-037', N'TH00013', N'PN00007', '2025-03-30', '2026-10-30', 250, 250, 80000),
(N'L0038', N'SL-TH00013-038', N'TH00013', N'PN00018', '2025-03-30', '2026-10-30', 200, 200, 82000),
(N'L0039', N'SL-TH00013-039', N'TH00013', N'PN00024', '2025-03-30', '2026-10-30', 150, 150, 83000),
(N'L0040', N'SL-TH00014-040', N'TH00014', N'PN00008', '2024-11-25', '2026-11-25', 200, 200, 110000),
(N'L0041', N'SL-TH00014-041', N'TH00014', N'PN00019', '2024-11-25', '2026-11-25', 150, 150, 115000),
(N'L0042', N'SL-TH00014-042', N'TH00014', N'PN00025', '2024-11-25', '2026-11-25', 100, 100, 118000),
(N'L0043', N'SL-TH00015-043', N'TH00015', N'PN00009', '2024-07-10', '2026-12-10', 300, 300, 78000),
(N'L0044', N'SL-TH00015-044', N'TH00015', N'PN00020', '2024-07-10', '2026-12-10', 250, 250, 80000),
(N'L0045', N'SL-TH00015-045', N'TH00015', N'PN00026', '2024-07-10', '2026-12-10', 200, 200, 82000),
(N'L0046', N'SL-TH00016-046', N'TH00016', N'PN00010', '2024-09-18', '2027-01-18', 400, 400, 28000),
(N'L0047', N'SL-TH00016-047', N'TH00016', N'PN00016', '2024-09-18', '2027-01-18', 400, 400, 30000),
(N'L0048', N'SL-TH00016-048', N'TH00016', N'PN00027', '2024-09-18', '2027-01-18', 400, 400, 32000),
(N'L0049', N'SL-TH00017-049', N'TH00017', N'PN00011', '2025-01-20', '2027-02-20', 150, 150, 400000),
(N'L0050', N'SL-TH00017-050', N'TH00017', N'PN00021', '2025-01-20', '2027-02-20', 150, 150, 410000),
(N'L0051', N'SL-TH00018-051', N'TH00018', N'PN00012', '2025-04-15', '2027-03-15', 200, 200, 330000),
(N'L0052', N'SL-TH00018-052', N'TH00018', N'PN00022', '2025-04-15', '2027-03-15', 200, 200, 340000),
(N'L0053', N'SL-TH00019-053', N'TH00019', N'PN00013', '2024-06-30', '2027-04-30', 350, 350, 60000),
(N'L0054', N'SL-TH00019-054', N'TH00019', N'PN00023', '2024-06-30', '2027-04-30', 300, 300, 62000),
(N'L0055', N'SL-TH00019-055', N'TH00019', N'PN00028', '2024-06-30', '2027-04-30', 200, 200, 63000),
(N'L0056', N'SL-TH00020-056', N'TH00020', N'PN00014', '2024-08-22', '2027-10-01', 400, 400, 50000),
(N'L0057', N'SL-TH00020-057', N'TH00020', N'PN00024', '2024-08-22', '2027-10-01', 350, 350, 52000),
(N'L0058', N'SL-TH00020-058', N'TH00020', N'PN00029', '2024-08-22', '2027-10-01', 200, 200, 53000);

-- Chi ti?t phi?u nh?p tham chi?u theo l? thu?c
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maLo, soLuong, donGia) VALUES
(N'PN00001', N'L0001', 300, 20000),
(N'PN00007', N'L0002', 400, 21000),
(N'PN00013', N'L0003', 321, 22000),
(N'PN00002', N'L0004', 500, 3000),
(N'PN00008', N'L0005', 321, 3200),
(N'PN00014', N'L0006', 200, 3500),
(N'PN00003', N'L0007', 400, 25000),
(N'PN00009', N'L0008', 332, 26000),
(N'PN00015', N'L0009', 300, 27000),
(N'PN00004', N'L0010', 500, 240000),
(N'PN00010', N'L0011', 334, 245000),
(N'PN00016', N'L0012', 200, 248000),
(N'PN00005', N'L0013', 400, 20000),
(N'PN00011', N'L0014', 376, 21000),
(N'PN00017', N'L0015', 300, 22000),
(N'PN00006', N'L0016', 500, 85000),
(N'PN00012', N'L0017', 334, 88000),
(N'PN00018', N'L0018', 200, 90000),
(N'PN00001', N'L0019', 400, 85000),
(N'PN00013', N'L0020', 376, 88000),
(N'PN00019', N'L0021', 300, 90000),
(N'PN00002', N'L0022', 350, 95000),
(N'PN00014', N'L0023', 371, 98000),
(N'PN00020', N'L0024', 300, 100000),
(N'PN00003', N'L0025', 400, 170000),
(N'PN00015', N'L0026', 354, 175000),
(N'PN00021', N'L0027', 300, 178000),
(N'PN00004', N'L0028', 600, 12000),
(N'PN00010', N'L0029', 700, 12500),
(N'PN00016', N'L0030', 700, 13000),
(N'PN00005', N'L0031', 500, 35000),
(N'PN00011', N'L0032', 500, 37000),
(N'PN00022', N'L0033', 500, 38000),
(N'PN00006', N'L0034', 300, 45000),
(N'PN00017', N'L0035', 300, 46000),
(N'PN00023', N'L0036', 200, 48000),
(N'PN00007', N'L0037', 250, 80000),
(N'PN00018', N'L0038', 200, 82000),
(N'PN00024', N'L0039', 150, 83000),
(N'PN00008', N'L0040', 200, 110000),
(N'PN00019', N'L0041', 150, 115000),
(N'PN00025', N'L0042', 100, 118000),
(N'PN00009', N'L0043', 300, 78000),
(N'PN00020', N'L0044', 250, 80000),
(N'PN00026', N'L0045', 200, 82000),
(N'PN00010', N'L0046', 400, 28000),
(N'PN00016', N'L0047', 400, 30000),
(N'PN00027', N'L0048', 400, 32000),
(N'PN00011', N'L0049', 150, 400000),
(N'PN00021', N'L0050', 150, 410000),
(N'PN00012', N'L0051', 200, 330000),
(N'PN00022', N'L0052', 200, 340000),
(N'PN00013', N'L0053', 350, 60000),
(N'PN00023', N'L0054', 300, 62000),
(N'PN00028', N'L0055', 200, 63000),
(N'PN00014', N'L0056', 400, 50000),
(N'PN00024', N'L0057', 350, 52000),
(N'PN00029', N'L0058', 200, 53000);

-- PhieuDatThuoc (PDT00001, PDT00002, ...)
INSERT INTO PhieuDatThuoc (maPhieuDat, ngayDat, maKH, diaChi, hinhThucThanhToan, trangThai) VALUES
(N'PDT00001', '2025-12-15', N'KH00001', N'123 Lê Lợi, Q1, TP.HCM', N'Tại chỗ', N'Đã hoàn thành'),
(N'PDT00002', '2026-01-12', N'KH00002', N'456 Nguyễn Huệ, Q1, TP.HCM', N'Thanh toán online', N'Đã hoàn thành'),
(N'PDT00003', '2026-03-15', N'KH00003', N'789 Trần Hưng Đạo, Q5, TP.HCM', N'Tại chỗ', N'Đã hoàn thành'),
(N'PDT00004', '2026-04-10', N'KH00004', N'321 Võ Văn Tần, Q3, TP.HCM', N'Thanh toán online', N'Đã hoàn thành'),
(N'PDT00005', '2026-05-06', N'KH00008', N'99 Nguyễn Trãi, Q5, TP.HCM', N'Thanh toán online', N'Chưa hoàn thành'),
(N'PDT00006', '2026-05-20', N'KH00009', N'15 Lý Tự Trọng, Q1, TP.HCM', N'Tại chỗ', N'Chưa hoàn thành');

-- ChiTietPhieuDatThuoc
INSERT INTO ChiTietPhieuDatThuoc (maPhieuDat, maThuoc, soLuong, donGia) VALUES
(N'PDT00001', N'TH00001', 5, 25000),
(N'PDT00001', N'TH00003', 2, 30000),
(N'PDT00002', N'TH00010', 10, 15000),
(N'PDT00002', N'TH00002', 5, 4000),
(N'PDT00003', N'TH00005', 3, 24000),
(N'PDT00003', N'TH00016', 4, 35000),
(N'PDT00004', N'TH00011', 2, 45000),
(N'PDT00004', N'TH00019', 1, 65000),
(N'PDT00005', N'TH00006', 2, 95000),
(N'PDT00005', N'TH00015', 3, 85000),
(N'PDT00006', N'TH00003', 4, 30000),
(N'PDT00006', N'TH00012', 2, 50000);

-- HoaDon (HD00001, HD00002, ...) - ma hoa don tang dan theo ngay thanh toan
INSERT INTO HoaDon (maHD, ngayLap, maThue, maNV, maKH, maKM, maPhieuDat) VALUES
(N'HD00001', '2025-12-15', N'TE00001', N'NV00002', N'KH00001', N'KM00001', N'PDT00001'),
(N'HD00002', '2025-12-22', N'TE00001', N'NV00002', N'KH00005', NULL, NULL),
(N'HD00003', '2026-01-05', N'TE00001', N'NV00004', N'KH00006', N'KM00001', NULL),
(N'HD00004', '2026-01-12', N'TE00001', N'NV00002', N'KH00002', NULL, N'PDT00002'),
(N'HD00005', '2026-01-28', N'TE00001', N'NV00003', N'KH00007', NULL, NULL),
(N'HD00006', '2026-02-08', N'TE00001', N'NV00002', N'KH00001', NULL, NULL),
(N'HD00007', '2026-02-19', N'TE00001', N'NV00004', N'KH00008', NULL, NULL),
(N'HD00008', '2026-03-03', N'TE00001', N'NV00003', N'KH00009', NULL, NULL),
(N'HD00009', '2026-03-12', N'TE00001', N'NV00002', N'KH00010', N'KM00003', NULL),
(N'HD00010', '2026-03-16', N'TE00001', N'NV00003', N'KH00003', NULL, N'PDT00003'),
(N'HD00011', '2026-03-25', N'TE00001', N'NV00004', N'KH00002', NULL, NULL),
(N'HD00012', '2026-04-05', N'TE00001', N'NV00003', N'KH00008', NULL, NULL),
(N'HD00013', '2026-04-12', N'TE00001', N'NV00002', N'KH00005', NULL, NULL),
(N'HD00014', '2026-04-18', N'TE00001', N'NV00004', N'KH00004', N'KM00002', N'PDT00004'),
(N'HD00015', '2026-04-24', N'TE00001', N'NV00004', N'KH00001', NULL, NULL);

-- ChiTietHoaDon
INSERT INTO ChiTietHoaDon (maHD, maThuoc, soLuong, donGia) VALUES
(N'HD00001', N'TH00001', 5, 25000),
(N'HD00001', N'TH00003', 2, 30000),
(N'HD00002', N'TH00004', 1, 250000),
(N'HD00002', N'TH00012', 2, 50000),
(N'HD00003', N'TH00010', 8, 15000),
(N'HD00004', N'TH00010', 10, 15000),
(N'HD00004', N'TH00002', 5, 4000),
(N'HD00005', N'TH00015', 2, 85000),
(N'HD00005', N'TH00002', 4, 4000),
(N'HD00006', N'TH00001', 3, 25000),
(N'HD00006', N'TH00011', 1, 45000),
(N'HD00007', N'TH00006', 2, 95000),
(N'HD00008', N'TH00003', 2, 30000),
(N'HD00008', N'TH00014', 1, 120000),
(N'HD00009', N'TH00017', 1, 420000),
(N'HD00010', N'TH00005', 3, 24000),
(N'HD00010', N'TH00016', 4, 35000),
(N'HD00011', N'TH00019', 2, 65000),
(N'HD00011', N'TH00016', 2, 35000),
(N'HD00012', N'TH00008', 1, 105000),
(N'HD00012', N'TH00009', 1, 180000),
(N'HD00013', N'TH00012', 3, 50000),
(N'HD00014', N'TH00011', 2, 45000),
(N'HD00014', N'TH00019', 1, 65000),
(N'HD00015', N'TH00018', 1, 350000),
(N'HD00015', N'TH00020', 2, 55000);

-- TaiKhoan - CHỈ CÓ 2 VAI TRÒ
INSERT INTO TaiKhoan (tenDangNhap, matKhau, trangThai, maNV, vaiTro) VALUES
(N'admin', N'Admin@123', N'Hoạt động', N'NV00001', N'Nhân viên quản lý'),
(N'nvbanthuoc1', N'Pass@123', N'Hoạt động', N'NV00002', N'Nhân viên bán thuốc'),
(N'nvbanthuoc2', N'Pass@123', N'Hoạt động', N'NV00003', N'Nhân viên bán thuốc'),
(N'nvbanthuoc3', N'Pass@123', N'Hoạt động', N'NV00004', N'Nhân viên bán thuốc'),
(N'quanly1', N'Manager@2025', N'Hoạt động', N'NV00005', N'Nhân viên quản lý');

GO

-- =============================================
-- HÀM TỰ ĐỘNG TẠO MÃ
-- =============================================

-- Hàm tạo mã Thuốc
CREATE FUNCTION fn_GenerateMaThuoc()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maThuoc, 3, 5) AS INT)), 0)
    FROM Thuoc
    WHERE maThuoc LIKE 'TH%';
    
    SET @newMa = 'TH' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Nhà Cung Cấp
CREATE FUNCTION fn_GenerateMaNCC()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maNCC, 4, 5) AS INT)), 0)
    FROM NhaCungCap
    WHERE maNCC LIKE 'NCC%';
    
    SET @newMa = 'NCC' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Khách Hàng
CREATE FUNCTION fn_GenerateMaKH()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maKH, 3, 5) AS INT)), 0)
    FROM KhachHang
    WHERE maKH LIKE 'KH%';
    
    SET @newMa = 'KH' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Nhân Viên
CREATE FUNCTION fn_GenerateMaNV()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maNV, 3, 5) AS INT)), 0)
    FROM NhanVien
    WHERE maNV LIKE 'NV%';
    
    SET @newMa = 'NV' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Danh Mục
CREATE FUNCTION fn_GenerateMaDanhMuc()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maDanhMuc, 3, 5) AS INT)), 0)
    FROM DanhMucThuoc
    WHERE maDanhMuc LIKE 'DM%';
    
    SET @newMa = 'DM' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Hóa Đơn
CREATE FUNCTION fn_GenerateMaHD()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maHD, 3, 5) AS INT)), 0)
    FROM HoaDon
    WHERE maHD LIKE 'HD%';
    
    SET @newMa = 'HD' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Khuyến Mãi
CREATE FUNCTION fn_GenerateMaKM()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maKM, 3, 5) AS INT)), 0)
    FROM KhuyenMai
    WHERE maKM LIKE 'KM%';
    
    SET @newMa = 'KM' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Phiếu Đặt Thuốc
CREATE FUNCTION fn_GenerateMaPhieuDat()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maPhieuDat, 4, 5) AS INT)), 0)
    FROM PhieuDatThuoc
    WHERE maPhieuDat LIKE 'PDT%';
    
    SET @newMa = 'PDT' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Phiếu Nhập
CREATE FUNCTION fn_GenerateMaPhieuNhap()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maPhieuNhap, 3, 5) AS INT)), 0)
    FROM PhieuNhapHang
    WHERE maPhieuNhap LIKE 'PN%';
    
    SET @newMa = 'PN' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Thuế
-- Hàm tạo mã Lô Thuốc
CREATE FUNCTION fn_GenerateMaLo()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);

    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maLo, 2, 4) AS INT)), 0)
    FROM LoThuoc
    WHERE maLo LIKE 'L[0-9][0-9][0-9][0-9]';

    SET @newMa = 'L' + RIGHT('0000' + CAST(@maxNumber + 1 AS VARCHAR), 4);
    RETURN @newMa;
END;
GO

CREATE FUNCTION fn_GenerateMaThue()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maThue, 3, 5) AS INT)), 0)
    FROM Thue
    WHERE maThue LIKE 'TE%';
    
    SET @newMa = 'TE' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- =============================================
-- TEST HÀM TẠO MÃ
-- =============================================
PRINT N'=== TEST CÁC HÀM TẠO MÃ ===';
SELECT dbo.fn_GenerateMaThuoc() AS MaThuocMoi;
SELECT dbo.fn_GenerateMaNCC() AS MaNCCMoi;
SELECT dbo.fn_GenerateMaKH() AS MaKHMoi;
SELECT dbo.fn_GenerateMaNV() AS MaNVMoi;
SELECT dbo.fn_GenerateMaDanhMuc() AS MaDanhMucMoi;
SELECT dbo.fn_GenerateMaHD() AS MaHDMoi;
SELECT dbo.fn_GenerateMaKM() AS MaKMMoi;
SELECT dbo.fn_GenerateMaPhieuDat() AS MaPhieuDatMoi;
SELECT dbo.fn_GenerateMaPhieuNhap() AS MaPhieuNhapMoi;
SELECT dbo.fn_GenerateMaLo() AS MaLoMoi;
SELECT dbo.fn_GenerateMaThue() AS MaThueMoi;

PRINT N'';
PRINT N' Tạo database thành công!';
PRINT N' Database: QLHieuThuocTay';
PRINT N' Ngày tạo: 2025-10-13';
PRINT N' Tạo bởi: anhhoang19122005';
PRINT N'';
PRINT N'=== THÔNG TIN RÀNG BUỘC ===';
PRINT N' Chức vụ nhân viên: Nhân viên bán thuốc | Nhân viên quản lý';
PRINT N' Vai trò tài khoản: Nhân viên bán thuốc | Nhân viên quản lý';
PRINT N' Trạng thái tài khoản: Hoạt động | Ngừng hoạt động';
PRINT N' Giới tính: Nam | Nữ';
PRINT N' Hình thức thanh toán: Thanh toán online | Tại chỗ';
PRINT N' Trạng thái phiếu đặt: Đã hoàn thành | Chưa hoàn thành';
PRINT N' Phần trăm thuế: >= 0';
PRINT N' Phần trăm giảm giá: 0 < % <= 100';
PRINT N' Giá bán, đơn giá, số lượng: >= 0';

GO
