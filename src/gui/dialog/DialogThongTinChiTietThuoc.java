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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.LoThuocDAO;
import dao.ThuocDAO;
import entity.LoThuoc;
import entity.Thuoc;

public class DialogThongTinChiTietThuoc extends JDialog {
    private static final Color PRIMARY = new Color(0, 0, 205);
    private static final Color FOOTER_BG = new Color(230, 245, 245);
    private static final Color DANGER = new Color(220, 53, 69);

    private final ThuocDAO thuocDAO = new ThuocDAO();
    private final LoThuocDAO loThuocDAO = new LoThuocDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public DialogThongTinChiTietThuoc(Frame parent, String maThuoc) {
        super(parent, "Chi tiết thông tin thuốc", true);
        setSize(820, 720);
        setResizable(false);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(Color.WHITE);
        loadData(maThuoc);
        setLocationRelativeTo(parent);
    }

    private void loadData(String maThuoc) {
        try {
            Thuoc thuoc = thuocDAO.getThuocTheoMaThuoc(maThuoc);
            if (thuoc == null) {
                add(new JLabel("Không tìm thấy thuốc: " + maThuoc), BorderLayout.CENTER);
                return;
            }

            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
            titlePanel.setBackground(PRIMARY);
            titlePanel.setPreferredSize(new Dimension(820, 60));
            JLabel title = new JLabel("ℹ  CHI TIẾT THÔNG TIN THUỐC");
            title.setFont(new Font("Roboto", Font.BOLD, 22));
            title.setForeground(Color.WHITE);
            titlePanel.add(title);
            add(titlePanel, BorderLayout.NORTH);

            JPanel content = new JPanel(new BorderLayout(0, 12));
            content.setBackground(Color.WHITE);
            content.setBorder(new EmptyBorder(16, 18, 12, 18));

            JPanel top = new JPanel(new BorderLayout(0, 12));
            top.setBackground(Color.WHITE);
            top.add(createBasicInfoPanel(thuoc), BorderLayout.NORTH);
            top.add(createTextBox("Thành phần", thuoc.getThanhPhan()), BorderLayout.CENTER);
            top.add(createTextBox("Mô tả", thuoc.getMoTa()), BorderLayout.SOUTH);
            content.add(top, BorderLayout.NORTH);
            content.add(createBatchPanel(thuoc.getMaThuoc()), BorderLayout.CENTER);

            add(content, BorderLayout.CENTER);

            JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 14));
            footer.setBackground(FOOTER_BG);
            footer.setPreferredSize(new Dimension(820, 72));
            JButton btnDong = new JButton("ĐÓNG");
            btnDong.setIcon(null);
            btnDong.setFont(new Font("Roboto", Font.BOLD, 13));
            btnDong.setPreferredSize(new Dimension(110, 38));
            btnDong.setBackground(DANGER);
            btnDong.setForeground(Color.WHITE);
            btnDong.setFocusPainted(false);
            btnDong.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDong.setBorder(new LineBorder(DANGER, 2, true));
            btnDong.addActionListener(e -> dispose());
            footer.add(btnDong);
            add(footer, BorderLayout.SOUTH);
        } catch (Exception e) {
            e.printStackTrace();
            add(new JLabel("Lỗi tải dữ liệu: " + e.getMessage()), BorderLayout.CENTER);
        }
    }

    private JPanel createBasicInfoPanel(Thuoc thuoc) {
        JPanel panel = sectionPanel("Thông tin cơ bản");
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(Color.WHITE);
        grid.setBorder(new EmptyBorder(8, 10, 8, 10));

        addInfo(grid, 0, 0, "Mã thuốc:", thuoc.getMaThuoc(), false);
        addInfo(grid, 0, 1, "Tên thuốc:", thuoc.getTenThuoc(), false);
        addInfo(grid, 1, 0, "Danh mục:", thuoc.getDanhMucThuoc() == null ? "" : thuoc.getDanhMucThuoc().getTenDanhMuc(), false);
        addInfo(grid, 1, 1, "Đơn vị tính:", thuoc.getDonViTinh(), false);
        addInfo(grid, 2, 0, "Xuất xứ:", thuoc.getXuatXu(), false);
        addInfo(grid, 2, 1, "Giá bán:", String.format("%,.0f VNĐ", thuoc.getGiaBan()), true);
        addInfo(grid, 3, 0, "Số lượng tồn:", String.valueOf(thuoc.getSoLuongTon()), false);
        addInfo(grid, 3, 1, "HSD gần nhất:", thuoc.getHanSuDung() == null ? "" : sdf.format(thuoc.getHanSuDung()), false);

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private void addInfo(JPanel panel, int row, int col, String label, String value, boolean redValue) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, col == 0 ? 0 : 28, 4, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = col * 2;
        gbc.gridy = row;
        gbc.weightx = 0.16;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Roboto", Font.BOLD, 12));
        panel.add(lbl, gbc);

        gbc.gridx = col * 2 + 1;
        gbc.weightx = 0.34;
        JLabel val = new JLabel(value == null ? "" : value);
        val.setFont(new Font("Roboto", Font.PLAIN, 12));
        if (redValue) {
            val.setForeground(Color.RED);
            val.setFont(new Font("Roboto", Font.BOLD, 12));
        }
        panel.add(val, gbc);
    }

    private JPanel createTextBox(String title, String content) {
        JPanel panel = sectionPanel(title);
        JTextArea area = new JTextArea(content == null ? "" : content);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Roboto", Font.PLAIN, 13));
        area.setBorder(new EmptyBorder(8, 10, 8, 10));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(760, 74));
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBatchPanel(String maThuoc) throws Exception {
        JPanel panel = sectionPanel("Danh sách lô thuốc");
        String[] cols = { "Mã lô", "Số lô", "NSX", "HSD", "SL nhập", "SL còn", "Giá nhập", "Trạng thái" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (LoThuoc lo : loThuocDAO.getLoThuocTheoMaThuoc(maThuoc)) {
            model.addRow(new Object[] { lo.getMaLo(), lo.getSoLo(),
                    lo.getNgaySanXuat() == null ? "" : sdf.format(lo.getNgaySanXuat()),
                    lo.getHanSuDung() == null ? "" : sdf.format(lo.getHanSuDung()), lo.getSoLuongNhap(),
                    lo.getSoLuongConLai(), String.format("%,.0f", lo.getDonGiaNhap()), lo.getTrangThai() });
        }
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Roboto", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 12));
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel sectionPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(PRIMARY, 1));

        JLabel lbl = new JLabel(" " + title + " ");
        lbl.setFont(new Font("Roboto", Font.BOLD, 12));
        lbl.setForeground(PRIMARY);
        panel.add(lbl, BorderLayout.NORTH);
        return panel;
    }
}
