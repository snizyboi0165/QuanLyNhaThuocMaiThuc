package gui.form;

import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.ThuocDAO;
import entity.Thuoc;
import gui.dialog.DialogChiTietThuoc;

public class FormTimKiemThuoc extends JPanel 
{
    private JTextField txtMaThuoc, txtTenThuoc, txtDonViTinh, txtGiaBan, txtSoLuong, txtThanhPhan, txtXuatXu;
    private JButton btnTimKiem, btnLamMoi, btnXuatExcel, btnXemChiTiet;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblKetQua;
    private ThuocDAO thuocDAO;
    private DecimalFormat df = new DecimalFormat("#,##0.00");

    private Font labelFont = new Font("Roboto", Font.PLAIN, 14);
    private Font fieldFont = new Font("Roboto", Font.PLAIN, 14);
    private Font titleFont = new Font("Roboto", Font.BOLD, 24);
    private Font headerTableFont = new Font("Roboto", Font.BOLD, 16);

    public FormTimKiemThuoc() 
    {
        thuocDAO = new ThuocDAO();
        initComponents();
        hienThiTatCaThuoc();
    }

    private void initComponents() 
    {
        setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(0, 10));

        // Top
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Mid
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(new Color(230, 245, 245));
        centerPanel.add(createSearchPanel(), BorderLayout.NORTH);
        centerPanel.add(createTablePanel(), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() 
    {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 0, 205));
        headerPanel.setPreferredSize(new Dimension(1130, 70));

        JLabel lblTitle = new JLabel("TÌM KIẾM THUỐC", SwingConstants.CENTER);
        lblTitle.setFont(titleFont);
        lblTitle.setForeground(Color.WHITE);

        JLabel lblIcon = new JLabel(new FlatSVGIcon(getClass().getResource("/img/search.svg")));
        lblIcon.setBorder(new EmptyBorder(0, 20, 0, 0));

        headerPanel.add(lblIcon, BorderLayout.WEST);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createSearchPanel() 
    {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblSearchTitle = new JLabel("Tiêu chí tìm kiếm");
        lblSearchTitle.setFont(new Font("Roboto", Font.BOLD, 18));
        lblSearchTitle.setForeground(new Color(0, 0, 205));
        lblSearchTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblSearchTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Hàng 1: Mã thuốc, Tên thuốc, Đơn vị tính
        gbc.gridy = row++;
        
        gbc.gridx = 0; gbc.weightx = 0.15; 
        formPanel.add(createLabel("Mã thuốc:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.35; 
        txtMaThuoc = createTextField(); 
        formPanel.add(txtMaThuoc, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.15; 
        formPanel.add(createLabel("Tên thuốc:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.35; 
        txtTenThuoc = createTextField(); 
        formPanel.add(txtTenThuoc, gbc);

        // Hàng 2: Đơn vị tính, Giá bán, Số lượng
        gbc.gridy = row++;
        
        gbc.gridx = 0; gbc.weightx = 0.15; 
        formPanel.add(createLabel("Đơn vị tính:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.35; 
        txtDonViTinh = createTextField(); 
        formPanel.add(txtDonViTinh, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.15; 
        formPanel.add(createLabel("Giá bán:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.35; 
        txtGiaBan = createTextField(); 
        formPanel.add(txtGiaBan, gbc);

        // Hàng 3: Số lượng, Thành phần
        gbc.gridy = row++;
        
        gbc.gridx = 0; gbc.weightx = 0.15; 
        formPanel.add(createLabel("Số lượng:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.35; 
        txtSoLuong = createTextField(); 
        formPanel.add(txtSoLuong, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.15; 
        formPanel.add(createLabel("Thành phần:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.35; 
        txtThanhPhan = createTextField(); 
        formPanel.add(txtThanhPhan, gbc);

        // Hàng 4: Xuất xứ
        gbc.gridy = row++;
        
        gbc.gridx = 0; gbc.weightx = 0.15; 
        formPanel.add(createLabel("Xuất xứ:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.35; 
        txtXuatXu = createTextField(); 
        formPanel.add(txtXuatXu, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createButtonPanel() 
    {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        btnPanel.setBackground(Color.WHITE);

        btnTimKiem = taoNut("TÌM KIẾM", new Color(0, 0, 205), Color.WHITE, "/img/search.svg");
        btnTimKiem.addActionListener(e -> xuLyTimKiem());

        btnLamMoi = taoNut("LÀM MỚI", Color.WHITE, new Color(100, 100, 100), "/img/reload.svg");
        btnLamMoi.setBorder(new LineBorder(new Color(200, 200, 200), 2, true));
        btnLamMoi.addActionListener(e -> lamMoi());

        btnXuatExcel = taoNut("XUẤT EXCEL", new Color(46, 125, 50), Color.WHITE, "/img/excel.svg");
        btnXuatExcel.addActionListener(e -> xuatExcel());

        btnPanel.add(btnTimKiem);
        btnPanel.add(btnLamMoi);
        btnPanel.add(btnXuatExcel);

        return btnPanel;
    }

    private JButton taoNut(String text, Color bg, Color fg, String iconPath) 
    {
        JButton btn = new JButton(text, new FlatSVGIcon(getClass().getResource(iconPath)));
        btn.setFont(new Font("Roboto", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setBorder(new LineBorder(bg, 2, true));
        return btn;
    }

    private JPanel createTablePanel() 
    {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(new Color(230, 245, 245));

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));
        resultPanel.setPreferredSize(new Dimension(1130, 50));

        lblKetQua = new JLabel("Tìm thấy 0 kết quả");
        lblKetQua.setFont(new Font("Roboto", Font.BOLD, 16));
        lblKetQua.setForeground(new Color(0, 0, 205));
        lblKetQua.setBorder(new EmptyBorder(10, 20, 10, 10));

        btnXemChiTiet = new JButton("XEM CHI TIẾT");
        btnXemChiTiet.setFont(new Font("Roboto", Font.BOLD, 13));
        btnXemChiTiet.setBackground(new Color(33, 150, 243));
        btnXemChiTiet.setForeground(Color.WHITE);
        btnXemChiTiet.setFocusPainted(false);
        btnXemChiTiet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXemChiTiet.setPreferredSize(new Dimension(140, 35));
        btnXemChiTiet.setBorder(new LineBorder(new Color(33, 150, 243), 2, true));
        btnXemChiTiet.addActionListener(e -> xemChiTiet());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 7));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnXemChiTiet);

        resultPanel.add(lblKetQua, BorderLayout.WEST);
        resultPanel.add(btnPanel, BorderLayout.EAST);

        tablePanel.add(resultPanel, BorderLayout.NORTH);

        // Bảng với các cột cần thiết
        String[] columns = {"Mã thuốc", "Tên thuốc", "Đơn vị tính", "Giá bán", "Số lượng", "Xuất xứ"};
        tableModel = new DefaultTableModel(columns, 0) 
        {
            @Override
            public boolean isCellEditable(int r, int c) 
            { 
                return false; 
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Roboto", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(headerTableFont);
        table.getTableHeader().setBackground(new Color(0, 0, 205));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        // Center align
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for(int i = 0; i < table.getColumnCount(); i++)
        {        	
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
        
        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));
        scroll.getViewport().setBackground(Color.WHITE);
        tablePanel.add(scroll, BorderLayout.CENTER);

        return tablePanel;
    }

    private JLabel createLabel(String text) 
    {
        JLabel lbl = new JLabel(text);
        lbl.setFont(labelFont);
        return lbl;
    }

    private JTextField createTextField() 
    {
        JTextField txt = new JTextField();
        txt.setFont(fieldFont);
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return txt;
    }

    // Xử lý sự kiện
    private void xuLyTimKiem() 
    {
        String ma = txtMaThuoc.getText().trim();
        String ten = txtTenThuoc.getText().trim();
        String dv = txtDonViTinh.getText().trim();
        String giaStr = txtGiaBan.getText().trim();
        String slStr = txtSoLuong.getText().trim();
        String tp = txtThanhPhan.getText().trim();
        String xx = txtXuatXu.getText().trim();

        Double gia = null;
        Integer sl = null;
        
        try 
        {
            if(!giaStr.isEmpty()) 
            {
                gia = Double.parseDouble(giaStr);
            }
            if(!slStr.isEmpty()) 
            {
                sl = Integer.parseInt(slStr);
            }
        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, 
                "Giá bán hoặc số lượng không hợp lệ!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try 
        {
            ArrayList<Thuoc> all = thuocDAO.getDsThuoc();
            ArrayList<Thuoc> kq = new ArrayList<>();
            
            for (Thuoc t : all) 
            {
                // Lọc theo tiêu chí
                if(!ma.isEmpty() && !t.getMaThuoc().toLowerCase().contains(ma.toLowerCase())) 
                    continue;
                    
                if(!ten.isEmpty() && !t.getTenThuoc().toLowerCase().contains(ten.toLowerCase())) 
                    continue;
                    
                if(!dv.isEmpty() && !t.getDonViTinh().toLowerCase().contains(dv.toLowerCase()))
                    continue;
                    
                if(gia != null && t.getGiaBan() != gia) 
                    continue;
                    
                if(sl != null && t.getSoLuongTon() != sl) 
                    continue;
                    
                if(!tp.isEmpty() && !t.getThanhPhan().toLowerCase().contains(tp.toLowerCase())) 
                    continue;
                    
                if(!xx.isEmpty() && !t.getXuatXu().toLowerCase().contains(xx.toLowerCase())) 
                    continue;
                    
                kq.add(t);
            }

            hienThiKetQua(kq);
            
            if (kq.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy thuốc nào phù hợp!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tìm kiếm thuốc: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hienThiKetQua(ArrayList<Thuoc> ds) 
    {
        tableModel.setRowCount(0);
        
        for(Thuoc t : ds) 
        {
            tableModel.addRow(new Object[]{
                t.getMaThuoc(), 
                t.getTenThuoc(), 
                t.getDonViTinh(),
                df.format(t.getGiaBan()) + " đ", 
                t.getSoLuongTon(), 
                t.getXuatXu()
            });
        }
        
        lblKetQua.setText("Tìm thấy " + ds.size() + " kết quả");
    }

    private void lamMoi() 
    {
        txtMaThuoc.setText("");
        txtTenThuoc.setText("");
        txtDonViTinh.setText("");
        txtGiaBan.setText("");
        txtSoLuong.setText("");
        txtThanhPhan.setText("");
        txtXuatXu.setText("");
        
        hienThiTatCaThuoc();
    }

    private void xemChiTiet() 
    {
        int row = table.getSelectedRow();
        
        if(row == -1) 
        {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn thuốc cần xem!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maThuoc = tableModel.getValueAt(row, 0).toString();
        
        try 
        {
            Thuoc thuoc = thuocDAO.getThuocTheoMaThuoc(maThuoc);
            
            if (thuoc != null) 
            {
                // Mở dialog chi tiết
                DialogChiTietThuoc dialog = new DialogChiTietThuoc(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    thuoc
                );
                dialog.setVisible(true);
            } 
            else 
            {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy thông tin thuốc!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi lấy thông tin: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuatExcel() 
    {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "Không có dữ liệu để xuất!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, 
            "Chức năng xuất Excel đang được phát triển!", 
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void hienThiTatCaThuoc() 
    {
        try 
        {
            ArrayList<Thuoc> ds = thuocDAO.getDsThuoc();
            hienThiKetQua(ds);
        } 
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(this, 
                "Không thể tải danh sách thuốc!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
