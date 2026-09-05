package gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.NhaCungCapDAO;
import dao.NhanVienDAO;
import dao.PhieuNhapThuocDAO;
import dao.ThuocDAO;
import entity.ChiTietNhapThuoc;
import entity.NhaCungCap;
import entity.NhanVien;
import entity.PhieuNhapThuoc;
import entity.Thuoc;

public class DialogThongTinPhieuNhap extends JDialog {
    private static final Color PRIMARY = new Color(0, 0, 205);
    private static final Color HEADER_BLUE = new Color(0, 102, 204);
    private static final Color FOOTER_BG = new Color(230, 245, 245);

    private final PhieuNhapThuoc phieuNhap;
    private final PhieuNhapThuocDAO pnhDAO = new PhieuNhapThuocDAO();
    private final ThuocDAO thuocDAO = new ThuocDAO();
    private final NhanVienDAO nvDAO = new NhanVienDAO();
    private final NhaCungCapDAO nccDAO = new NhaCungCapDAO();
    private final DefaultTableModel model;
    private final JLabel lblTongTien;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public DialogThongTinPhieuNhap(Frame parent, PhieuNhapThuoc pnh) {
        super(parent, "Chi tiết phiếu nhập thuốc", true);
        this.phieuNhap = pnh;
        setSize(980, 620);
        setResizable(false);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(Color.WHITE);

        add(createInfoPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(16, 24, 12, 24));

        JPanel tableTitle = new JPanel(new BorderLayout());
        tableTitle.setBackground(PRIMARY);
        tableTitle.setPreferredSize(new Dimension(930, 38));
        JLabel lblTitle = new JLabel("  DANH SÁCH CHI TIẾT NHẬP THUỐC");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);
        tableTitle.add(lblTitle, BorderLayout.WEST);
        centerPanel.add(tableTitle, BorderLayout.NORTH);

        String[] cols = { "STT", "Mã lô", "Số lô", "Mã thuốc", "Tên thuốc", "NSX", "HSD",
                "Số lượng", "Đơn giá", "Thành tiền" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Roboto", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 12));
        table.getTableHeader().setBackground(HEADER_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(225, 225, 225));
        table.setShowHorizontalLines(true);
        centerTableCells(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(190, 210, 230), 1));
        centerPanel.add(scroll, BorderLayout.CENTER);

        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        totalPanel.setPreferredSize(new Dimension(930, 48));
        JLabel lblTotalTitle = new JLabel("  TỔNG TIỀN NHẬP:");
        lblTotalTitle.setFont(new Font("Roboto", Font.BOLD, 13));
        lblTongTien = new JLabel("0 VNĐ  ");
        lblTongTien.setHorizontalAlignment(JLabel.RIGHT);
        lblTongTien.setFont(new Font("Roboto", Font.BOLD, 14));
        lblTongTien.setForeground(Color.RED);
        totalPanel.add(lblTotalTitle, BorderLayout.WEST);
        totalPanel.add(lblTongTien, BorderLayout.EAST);
        centerPanel.add(totalPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 16));
        footer.setBackground(FOOTER_BG);
        footer.setPreferredSize(new Dimension(980, 72));
        JButton btnDong = new JButton("ĐÓNG");
        btnDong.setIcon(null);
        btnDong.setFont(new Font("Roboto", Font.BOLD, 13));
        btnDong.setPreferredSize(new Dimension(120, 38));
        btnDong.setBackground(PRIMARY);
        btnDong.setForeground(Color.WHITE);
        btnDong.setFocusPainted(false);
        btnDong.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDong.setBorder(new LineBorder(PRIMARY, 2, true));
        btnDong.addActionListener(e -> dispose());
        footer.add(btnDong);
        add(footer, BorderLayout.SOUTH);

        loadData();
        setLocationRelativeTo(parent);
    }

    private JPanel createInfoPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new EmptyBorder(22, 24, 18, 24));
        wrapper.setPreferredSize(new Dimension(980, 128));

        JPanel info = new JPanel(new GridBagLayout());
        info.setBackground(Color.WHITE);

        try {
            NhanVien nv = phieuNhap.getNhanVien() == null ? null
                    : nvDAO.getNhanVienTheoMa(phieuNhap.getNhanVien().getMaNV());
            NhaCungCap ncc = phieuNhap.getNhaCungCap() == null ? null
                    : nccDAO.getNhaCungCapTheoMa(phieuNhap.getNhaCungCap().getMaNCC());

            addInfo(info, 0, 0, "Mã phiếu:", phieuNhap.getMaPhieuNhap(), true);
            addInfo(info, 0, 1, "Nhân viên:", nv == null ? "" : nv.getTenNV(), false);
            addInfo(info, 0, 2, "Ngày nhập:", phieuNhap.getNgayNhap() == null ? "" : sdf.format(phieuNhap.getNgayNhap()), false);
            addInfo(info, 1, 0, "Nhà cung cấp:", ncc == null ? "" : ncc.getTenNCC(), false);
            addInfo(info, 1, 1, "Mã NV:", phieuNhap.getNhanVien() == null ? "" : phieuNhap.getNhanVien().getMaNV(), true);
            addInfo(info, 1, 2, "Mã NCC:", phieuNhap.getNhaCungCap() == null ? "" : phieuNhap.getNhaCungCap().getMaNCC(), true);
        } catch (Exception e) {
            addInfo(info, 0, 0, "Mã phiếu:", phieuNhap.getMaPhieuNhap(), true);
        }

        wrapper.add(info, BorderLayout.CENTER);
        return wrapper;
    }

    private void addInfo(JPanel panel, int row, int col, String label, String value, boolean blueValue) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, col == 0 ? 0 : 28, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = col * 2;
        gbc.gridy = row;
        gbc.weightx = 0.10;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Roboto", Font.BOLD, 12));
        panel.add(lbl, gbc);

        gbc.gridx = col * 2 + 1;
        gbc.weightx = 0.23;
        JLabel val = new JLabel(value == null ? "" : value);
        val.setFont(new Font("Roboto", Font.PLAIN, 12));
        if (blueValue) {
            val.setForeground(new Color(0, 102, 204));
        }
        panel.add(val, gbc);
    }

    private void loadData() {
        try {
            ArrayList<ChiTietNhapThuoc> list = pnhDAO.getChiTietPhieuNhap(phieuNhap.getMaPhieuNhap());
            int stt = 1;
            double tongTien = 0;
            for (ChiTietNhapThuoc ct : list) {
                Thuoc thuoc = thuocDAO.getThuocTheoMaThuoc(ct.getMaThuoc());
                double thanhTien = ct.getThanhTien();
                tongTien += thanhTien;
                model.addRow(new Object[] { stt++, ct.getMaLo(), ct.getSoLo(), ct.getMaThuoc(),
                        thuoc == null ? "" : thuoc.getTenThuoc(),
                        ct.getNgaySanXuat() == null ? "" : sdf.format(ct.getNgaySanXuat()),
                        ct.getHanSuDung() == null ? "" : sdf.format(ct.getHanSuDung()),
                        ct.getSoLuong(), String.format("%,.0f", ct.getDonGia()),
                        String.format("%,.0f", thanhTien) });
            }
            lblTongTien.setText(String.format("%,.0f VNĐ  ", tongTien));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void centerTableCells(JTable table) {
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(4).setPreferredWidth(190);
    }
}
