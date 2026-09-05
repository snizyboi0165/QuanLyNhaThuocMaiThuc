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
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.toedter.calendar.JDateChooser;

import dao.DanhMucThuocDAO;
import entity.DanhMucThuoc;
import entity.KhuyenMai;
import gui.form.FormDanhMucThuoc;

public class DialogThemDanhMucThuoc extends JDialog {
	private FormDanhMucThuoc parentForm;
	private DanhMucThuocDAO dmtDAO;
	private JTextField txtTenDanhMuc;
	private JTextField txtMaDanhMuc;
	public DialogThemDanhMucThuoc(Frame parent, FormDanhMucThuoc form) {
		super(parent,"Thêm Danh Mục Thuốc", true);
		this.parentForm = form;
		this.dmtDAO = new DanhMucThuocDAO();
		initComponents();
		this.setLocationRelativeTo(parent);
	}
	private void initComponents() {
		setSize(550, 450);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(500, 60));
        JLabel lblTitle = new JLabel("THÊM DANH MỤC THUỐC");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);

        Font fieldFont = new Font("Roboto", Font.PLAIN, 14);
        
        // Mã danh mục thuốc
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Mã: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        try {
			txtMaDanhMuc = new JTextField(dmtDAO.generateMaDanhMucThuoc());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        txtMaDanhMuc.setEditable(false);
        txtMaDanhMuc.setFont(fieldFont);
        txtMaDanhMuc.setPreferredSize(new Dimension(300, 35));
        formPanel.add(txtMaDanhMuc, gbc);
        
        // Tên danh mục thuốc
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên danh mục thuốc: *"), gbc);
        gbc.gridx = 1;
        txtTenDanhMuc = new JTextField();
        txtTenDanhMuc.setFont(fieldFont);
        txtTenDanhMuc.setPreferredSize(new Dimension(300, 35));
        formPanel.add(txtTenDanhMuc, gbc);

        
        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(230, 245, 245));
        JButton btnXacNhan = createStyledButton("XÁC NHẬN", new Color(0, 0, 205), Color.WHITE);
        btnXacNhan.addActionListener(e -> xuLyThem());
        JButton btnHuy = createStyledButton("HỦY", Color.WHITE, Color.DARK_GRAY);
        btnHuy.addActionListener(e -> dispose());
        buttonPanel.add(btnXacNhan);
        buttonPanel.add(btnHuy);
        add(buttonPanel, BorderLayout.SOUTH);
	}
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(140, 40));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new LineBorder(bgColor.darker(), 2, true));
        return button;
    }
    
    private void xuLyThem() {
        try {
        	String maDanhMuc = dmtDAO.generateMaDanhMucThuoc();
        	String tenDanhMuc = txtTenDanhMuc.getText().trim();
        	DanhMucThuoc dmt = new DanhMucThuoc(maDanhMuc, tenDanhMuc);
        	if (dmtDAO.themDanhMucThuoc(dmt)) {
        		JOptionPane.showMessageDialog(this, "Thêm danh mục thành công");
        		parentForm.reloadTable();
        		this.dispose();
        	} else {
        		JOptionPane.showMessageDialog(this, "Thêm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        	}
        } catch(Exception e ) {
        	e.printStackTrace();
        }
    }
}

