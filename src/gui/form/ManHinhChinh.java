package gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.RenderingHints;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;

import dao.HoaDonDAO;
import dao.NhanVienDAO;
import dao.ThuocDAO;
import entity.TaiKhoan;
import entity.Thuoc;
import utils.TableUtils;

public class ManHinhChinh extends JFrame implements ActionListener{
	private JButton btnHoaDon;
	private JButton btnNhanVien;
	private JButton btnKhachHang;
	private JButton btnTaiKhoan;
	private JButton btnThuoc;
	private JPanel pnCenter; 
	private JPanel backgroundPanel;
	private JButton btnNhaCungCap;
	private JButton btnHeThong;
	private TaiKhoan taiKhoan;
	private NhanVienDAO nvDAO;
	private JPanel pnUserInfo;
	private JPanel pnNorth;
	private JPanel pnMenu;
	private ThuocDAO thuocDAO;
	private HoaDonDAO hoaDonDAO;
	
	// Biến lưu kích thước ban đầu
	private int baseButtonWidth = 200;
	private int baseButtonHeight = 50;
	private int baseFontSize = 20;
	private int baseUserPanelWidth = 400;
	private int baseUserPanelHeight = 60;
	
	public ManHinhChinh(TaiKhoan tk) throws SQLException {
		TableUtils.installGlobalTableLock();
		nvDAO = new NhanVienDAO();
		thuocDAO = new ThuocDAO();
		hoaDonDAO = new HoaDonDAO();
		taiKhoan = tk;
		this.setTitle("Hiệu Thuốc Tây Mai Thức");
		this.setSize(1280,1000);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		
		// Panel North - Menu Bar
		pnNorth = new JPanel(new BorderLayout());
		pnNorth.setOpaque(false);
		pnNorth.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		// Panel bên trái chứa các nút menu
		pnMenu = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
		pnMenu.setOpaque(false);
		
		// Khởi tạo các nút menu
		initializeMenuButtons();
		
		// Panel thông tin người vào
		pnUserInfo = createUserInfoPanel(tk);
		
		// Thêm vào pnNorth
		pnNorth.add(pnMenu, BorderLayout.WEST);
		pnNorth.add(pnUserInfo, BorderLayout.EAST);
		
		backgroundPanel = new JPanel() {
			URL imageUrl = getClass().getResource("/img/anhHieuThuoc.jpg");
			private Image backgroundImage = new ImageIcon(imageUrl).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        
        // Form chức năng
        pnCenter = new JPanel(new BorderLayout());
        pnCenter.setOpaque(false);
        
        backgroundPanel.add(pnNorth, BorderLayout.NORTH);
        backgroundPanel.add(pnCenter, BorderLayout.CENTER);
        setContentPane(backgroundPanel);
        
        // Auto Resize
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustSizeBasedOnScreenSize();
            }
        });
        

        adjustSizeBasedOnScreenSize();
        hienThiTrangChu();
	}
	
	private void initializeMenuButtons() {
		String vaiTro = taiKhoan.getVaiTro();
		// Hệ thống
		btnHeThong = taoButtonDep("Hệ thống", "src/img/hethong.svg", baseButtonWidth, baseButtonHeight);
		JPopupMenu dropMenuHeThong = new JPopupMenu();
		String[] chucNangHeThong = {"Trang chủ", "Đăng xuất", "Thoát"};
		themMenuItem(dropMenuHeThong, chucNangHeThong, this);
		btnHeThong.addActionListener(e -> {
			dropMenuHeThong.show(btnHeThong, 0, btnHeThong.getHeight());
		});
		
		// Hoá Đơn
		btnHoaDon = taoButtonDep("Hoá Đơn", "src/img/bill.svg", baseButtonWidth, baseButtonHeight);
		JPopupMenu dropMenuHoaDon = new JPopupMenu();
		String[] chucNangHoaDon = {"Lập hoá đơn","Danh sách hoá đơn", "Tìm kiếm hoá đơn", "Quản lý phiếu đặt thuốc", "Quản lý phiếu nhập thuốc"};
		themMenuItem(dropMenuHoaDon, chucNangHoaDon, this);
		btnHoaDon.addActionListener(e -> {
			dropMenuHoaDon.show(btnHoaDon, 0, btnHoaDon.getHeight());
		});
		
		// Nhân Viên
		
		btnNhanVien = taoButtonDep("Nhân Viên", "src/img/employee.svg", baseButtonWidth, baseButtonHeight);
		JPopupMenu dropMenuNhanVien = new JPopupMenu();
		if (vaiTro.equals("Nhân viên quản lý")) {
			String[] chucNangNhanVien = {"Quản lý nhân viên","Tìm kiếm nhân viên", "Khuyến mãi", "Thuế", "Thống kê"};
			themMenuItem(dropMenuNhanVien, chucNangNhanVien, this);
		} else {
			String[] chucNangNhanVien = {"Khuyến mãi", "Thuế"};
			themMenuItem(dropMenuNhanVien, chucNangNhanVien, this);
		}
		
		btnNhanVien.addActionListener(e -> {
			dropMenuNhanVien.show(btnNhanVien,0,btnNhanVien.getHeight());
		});
		
		// Khách Hàng
		btnKhachHang = taoButtonDep("Khách Hàng", "src/img/customer_52.svg", baseButtonWidth, baseButtonHeight);
		JPopupMenu dropMenuKhachHang = new JPopupMenu();
		String[] chucNangKhachHang = {"Quản lý khách hàng", "Tìm kiếm khách hàng"};
		themMenuItem(dropMenuKhachHang, chucNangKhachHang, this);
		btnKhachHang.addActionListener(e -> {
			dropMenuKhachHang.show(btnKhachHang,0,btnKhachHang.getHeight());
		});
		
		// Tài Khoản
		if (vaiTro.equals("Nhân viên quản lý")) {
			btnTaiKhoan = taoButtonDep("Tài Khoản", "src/img/security.svg", baseButtonWidth, baseButtonHeight);
			JPopupMenu dropMenuTaiKhoan = new JPopupMenu();
			String[] chucNangTaiKhoan = {"Quản lý tài khoản"};
			themMenuItem(dropMenuTaiKhoan, chucNangTaiKhoan, this);
			btnTaiKhoan.addActionListener(e -> {
				dropMenuTaiKhoan.show(btnTaiKhoan,0,btnTaiKhoan.getHeight());
			});
		}
		
		// Thuốc
		btnThuoc = taoButtonDep("Thuốc", "src/img/medicine.svg", baseButtonWidth, baseButtonHeight);
		JPopupMenu dropMenuThuoc = new JPopupMenu();
		String[] chucNangThuoc = {"Quản lý thuốc", "Tìm kiếm thuốc", "Danh Mục Thuốc"};
		themMenuItem(dropMenuThuoc, chucNangThuoc, this);
		btnThuoc.addActionListener(e -> {
			dropMenuThuoc.show(btnThuoc,0,btnThuoc.getHeight());
		});
		
		if (vaiTro.equals("Nhân viên quản lý")) {
			btnNhaCungCap = taoButtonDep("Nhà Cung Cấp", "src/img/trucks.svg", baseButtonWidth, baseButtonHeight);
			JPopupMenu dropMenuNhaCungCap = new JPopupMenu();
			String[] chucNangNhaCungCap = {"Quản lý nhà cung cấp", "Tìm kiếm nhà cung cấp"};
			themMenuItem(dropMenuNhaCungCap, chucNangNhaCungCap, this);
			btnNhaCungCap.addActionListener(e -> {
				dropMenuNhaCungCap.show(btnNhaCungCap,0,btnNhaCungCap.getHeight());
			});
		}
		
		pnMenu.add(btnHeThong);
		pnMenu.add(btnHoaDon);
		pnMenu.add(btnThuoc);
		pnMenu.add(btnNhanVien);
		pnMenu.add(btnKhachHang);
		if (vaiTro.equals("Nhân viên quản lý")) {
			pnMenu.add(btnNhaCungCap);
			pnMenu.add(btnTaiKhoan);
		}
	}
	
	private JPanel createUserInfoPanel(TaiKhoan tk) {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 5));
		panel.setOpaque(true);
		panel.setBackground(new Color(41, 128, 185, 200));
		panel.setBorder(new LineBorder(Color.WHITE, 2, true));
		panel.setPreferredSize(new Dimension(baseUserPanelWidth, baseUserPanelHeight));
		
		// Icon user
		JLabel lblUserIcon = new JLabel("👤");
		lblUserIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
		lblUserIcon.setForeground(Color.WHITE);
		lblUserIcon.setName("userIcon"); 
		
		// Panel chứa thông tin text
		JPanel pnTextInfo = new JPanel();
		pnTextInfo.setLayout(new BoxLayout(pnTextInfo, BoxLayout.Y_AXIS));
		pnTextInfo.setOpaque(false);
		
		// Tên nhân viên
		String tenNV = "";
		try {
			tenNV = nvDAO.getNhanVienTheoMa(tk.getNhanVien().getMaNV()).getTenNV();
		} catch (SQLException e) {
			tenNV = "N/A";
			e.printStackTrace();
		}
		
		JLabel lblTenNV = new JLabel(tenNV);
		lblTenNV.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTenNV.setForeground(Color.WHITE);
		lblTenNV.setAlignmentX(LEFT_ALIGNMENT);
		lblTenNV.setName("tenNV"); // Đặt tên
		
		// Vai trò
		JPanel pnVaiTro = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		pnVaiTro.setOpaque(false);
		
		JLabel lblStatus = new JLabel("●");
		lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblStatus.setForeground(new Color(46, 204, 113));
		
		JLabel lblVaiTro = new JLabel(tk.getVaiTro());
		lblVaiTro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblVaiTro.setForeground(new Color(236, 240, 241));
		lblVaiTro.setName("vaiTro"); 
		
		pnVaiTro.add(lblStatus);
		pnVaiTro.add(lblVaiTro);
		
		pnTextInfo.add(lblTenNV);
		pnTextInfo.add(Box.createVerticalStrut(3));
		pnTextInfo.add(pnVaiTro);
		
		panel.add(lblUserIcon);
		panel.add(pnTextInfo);
		
		return panel;
	}
	
	
	private void adjustSizeBasedOnScreenSize() {
	    int screenWidth = getWidth();
	    int screenHeight = getHeight();
	    

	    double widthScale = screenWidth / 1920.0;
	    double heightScale = screenHeight / 1080.0;
	    double scale = Math.min(widthScale, heightScale);
	    

	    scale = Math.max(0.7, Math.min(scale, 1.5));
	    

	    int newButtonWidth = (int)(baseButtonWidth * scale);
	    int newButtonHeight = (int)(baseButtonHeight * scale);
	    int newFontSize = (int)(baseFontSize * scale);
	    

	    updateButtonSize(btnHeThong, newButtonWidth, newButtonHeight, newFontSize);
	    updateButtonSize(btnHoaDon, newButtonWidth, newButtonHeight, newFontSize);
	    updateButtonSize(btnNhanVien, newButtonWidth, newButtonHeight, newFontSize);
	    updateButtonSize(btnKhachHang, newButtonWidth, newButtonHeight, newFontSize);
	    updateButtonSize(btnThuoc, newButtonWidth, newButtonHeight, newFontSize);
	    

	    if (btnTaiKhoan != null) {
	        updateButtonSize(btnTaiKhoan, newButtonWidth, newButtonHeight, newFontSize);
	    }
	    if (btnNhaCungCap != null) {
	        updateButtonSize(btnNhaCungCap, newButtonWidth, newButtonHeight, newFontSize);
	    }
	    

	    int newUserPanelWidth = (int)(baseUserPanelWidth * scale);
	    int newUserPanelHeight = (int)(baseUserPanelHeight * scale);
	    pnUserInfo.setPreferredSize(new Dimension(newUserPanelWidth, newUserPanelHeight));
	    

	    updateUserInfoFontSize((int)(28 * scale), (int)(16 * scale), (int)(13 * scale));
	    

	    int padding = (int)(10 * scale);
	    pnNorth.setBorder(new EmptyBorder(padding, padding, padding, padding));
	    

	    FlowLayout layout = (FlowLayout) pnMenu.getLayout();
	    layout.setHgap((int)(15 * scale));
	    

	    pnNorth.revalidate();
	    pnNorth.repaint();
	}
	

		private void updateButtonSize(JButton btn, int width, int height, int fontSize) {
		    if (btn != null) {
		        Font currentFont = btn.getFont();
		        btn.setFont(new Font(currentFont.getName(), currentFont.getStyle(), fontSize));
		        
		        // Cập nhật icon size với tỷ lệ tốt hơn
		        if (btn.getIcon() != null) {
		            // Tính icon size dựa trên chiều cao button
		            int iconSize = Math.max(16, Math.min(32, (int)(height * 0.5)));
		            
		            // Lấy icon gốc và scale lại
		            ImageIcon currentIcon = (ImageIcon) btn.getIcon();
		            if (currentIcon. getImage() != null) {
		                Image scaledImage = currentIcon.getImage().getScaledInstance(
		                    iconSize, iconSize, Image.SCALE_SMOOTH
		                );
		                btn.setIcon(new ImageIcon(scaledImage));
		            }
		            
		            // Điều chỉnh khoảng cách giữa icon và text
		            int iconTextGap = (int)(8 * (fontSize / 20.0));
		            btn.setIconTextGap(iconTextGap);
		        }
		        
		        // Điều chỉnh border/padding
		        int paddingV = (int)(10 * (height / 50.0));
		        int paddingH = (int)(20 * (width / 200.0));
		        btn.setBorder(new EmptyBorder(paddingV, paddingH, paddingV, paddingH));
		        int adjustedWidth = calculateButtonWidth(btn, width);
		        Dimension buttonSize = new Dimension(adjustedWidth, height);
		        btn.setPreferredSize(buttonSize);
		        btn.setMinimumSize(buttonSize);
		    }
		}

	private int calculateButtonWidth(JButton btn, int targetWidth) {
		FontMetrics metrics = btn.getFontMetrics(btn.getFont());
		int textWidth = metrics.stringWidth(btn.getText());
		int iconWidth = btn.getIcon() != null ? btn.getIcon().getIconWidth() + btn.getIconTextGap() : 0;
		java.awt.Insets insets = btn.getInsets();
		int horizontalPadding = insets.left + insets.right + 24;
		int contentWidth = textWidth + iconWidth + horizontalPadding;
		return Math.max(targetWidth, contentWidth);
	}
	
	/**
	 * Cập nhật font size trong user info panel
	 */
	private void updateUserInfoFontSize(int iconSize, int nameSize, int roleSize) {
		for (java.awt.Component comp : pnUserInfo.getComponents()) {
			if (comp instanceof JLabel) {
				JLabel label = (JLabel) comp;
				if ("userIcon".equals(label.getName())) {
					label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, iconSize));
				}
			} else if (comp instanceof JPanel) {
				JPanel textPanel = (JPanel) comp;
				for (java.awt.Component innerComp : textPanel.getComponents()) {
					if (innerComp instanceof JLabel) {
						JLabel label = (JLabel) innerComp;
						if ("tenNV".equals(label.getName())) {
							label.setFont(new Font("Segoe UI", Font.BOLD, nameSize));
						}
					} else if (innerComp instanceof JPanel) {
						JPanel rolePanel = (JPanel) innerComp;
						for (java.awt.Component roleComp : rolePanel.getComponents()) {
							if (roleComp instanceof JLabel) {
								JLabel label = (JLabel) roleComp;
								if ("vaiTro".equals(label.getName())) {
									label.setFont(new Font("Segoe UI", Font.PLAIN, roleSize));
								}
							}
						}
					}
				}
			}
		}
	}
	
	// Phương thức để thêm MenuItem với ActionListener
	private void themMenuItem(JPopupMenu menu, String[] names, ActionListener listener) {
	    Font menuFont = new Font("Segoe UI", Font.BOLD, 20);
	    JLabel measure = new JLabel();
	    measure.setFont(menuFont);
	    FontMetrics fm = measure.getFontMetrics(menuFont);
	    int maxTextPx = 0;
	    for (String name : names) {
	        maxTextPx = Math.max(maxTextPx, fm.stringWidth(name));
	    }
	    int padSides = 36;
	    int itemWidth = Math.max(maxTextPx + padSides * 2, 380);
	    int itemHeight = 50;

	    for (String name : names) {
	        JMenuItem item = new JMenuItem(name);
	        item.setActionCommand(name);
	        item.setFont(menuFont);
	        item.setForeground(new Color(41, 128, 185)); 
	        Color baseColor = new Color(236, 240, 241);  
	        Color hoverColor = new Color(52, 152, 219);  
	        Color hoverTextColor = Color.WHITE;
	        item.setBackground(baseColor);
	        Dimension itemDim = new Dimension(itemWidth, itemHeight);
	        item.setPreferredSize(itemDim);
	        item.setMinimumSize(itemDim);
	        
	        item.setBorderPainted(false);   
	        item.setFocusPainted(false);    
	        item.setContentAreaFilled(true);
	        
	        
	        EmptyBorder originalBorder = new EmptyBorder(10, padSides, 10, padSides);
	        item.setBorder(originalBorder);
	        
	        item.addActionListener(e -> {
	            
	            item.setBackground(baseColor);
	            item.setForeground(new Color(41, 128, 185));
	            item.setBorder(originalBorder);
	            listener.actionPerformed(e);
	        });

	        item.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseEntered(MouseEvent e) {
	                item.setBackground(hoverColor);
	                item.setForeground(hoverTextColor);
	            }

	            @Override
	            public void mouseExited(MouseEvent e) {
	                item.setBackground(baseColor);
	                item.setForeground(new Color(41, 128, 185));
	            }
	        });
	        menu.add(item);
	    }
	}
	
	public static JButton taoButtonDep(String text, String iconPath, int width, int height) {
        JButton btn = new JButton(text);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setForeground(new Color(41, 128, 185));
        Color baseColor = new Color(236, 240, 241);
        Color hoverColor = new Color(52, 152, 219);
        Color hoverTextColor = Color.WHITE;
        btn.setBackground(baseColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setBorder(new LineBorder(new Color(189, 195, 199), 2, true));
        btn.setContentAreaFilled(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));

       
        if (iconPath != null && !iconPath.isEmpty()) {
            try {
                ImageIcon icon = null;
                String resourcePath = iconPath;
                if (resourcePath.startsWith("src/")) {
                    resourcePath = resourcePath.substring(4); 
                }
                if (!resourcePath.startsWith("/")) {
                    if (!resourcePath.contains("/")) {
                        resourcePath = "/img/" + resourcePath;
                    } else {
                        resourcePath = "/" + resourcePath;
                    }
                }

                URL resUrl = ManHinhChinh.class.getResource(resourcePath);
                if (resUrl != null) {
                    if (iconPath.toLowerCase().endsWith(".svg")) {
                        icon = renderSVG(resUrl, 24, 24);
                    } else {
                        icon = new ImageIcon(resUrl);
                        Image scaled = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                        icon = new ImageIcon(scaled);
                    }
                } else {
                    File f = new File(iconPath);
                    if (f.exists()) {
                        if (iconPath.toLowerCase().endsWith(".svg")) {
                            icon = renderSVG(f.toURI().toURL(), 24, 24);
                        } else {
                            ImageIcon raw = new ImageIcon(iconPath);
                            Image scaled = raw.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(scaled);
                        }
                    } else {
                        System.err.println("Không tìm thấy icon: (resource) " + resourcePath + " hoặc (file) " + iconPath);
                    }
                }

                if (icon != null) {
                    btn.setIcon(icon);
                    btn.setHorizontalTextPosition(SwingConstants.RIGHT);
                    btn.setIconTextGap(8);
                }
            } catch (Exception e) {
                System.err.println("⚠️ Không thể tải icon: " + iconPath);
                e.printStackTrace();
            }
        }

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor);
                btn.setForeground(hoverTextColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(baseColor);
                btn.setForeground(new Color(41, 128, 185));
            }
        });

        FontMetrics metrics = btn.getFontMetrics(btn.getFont());
        int textWidth = metrics.stringWidth(text);
        int iconWidth = btn.getIcon() != null ? btn.getIcon().getIconWidth() + btn.getIconTextGap() : 0;
        java.awt.Insets insets = btn.getInsets();
        int minWidthForText = textWidth + iconWidth + insets.left + insets.right + 24;
        int buttonWidth = Math.max(width, minWidthForText);
        Dimension buttonSize = new Dimension(buttonWidth, height);
        btn.setPreferredSize(buttonSize);
        btn.setMinimumSize(buttonSize);

        return btn;
    }

    private static ImageIcon renderSVG(URL url, int w, int h) throws Exception {
        SVGUniverse universe = new SVGUniverse();
        URI handle = universe.loadSVG(url);
        SVGDiagram diagram = universe.getDiagram(handle);
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double scaleX = w / diagram.getWidth();
        double scaleY = h / diagram.getHeight();
        g2.scale(scaleX, scaleY);
        diagram.render(g2);
        g2.dispose();
        return new ImageIcon(img);
    }
	
	public void hienThiForm(JPanel formMoi) {
		pnCenter.removeAll(); 
		pnCenter.add(formMoi, BorderLayout.CENTER); 
		pnCenter.revalidate(); 
		pnCenter.repaint(); 
	}

	private void hienThiTrangChu() {
		pnCenter.removeAll();
		pnCenter.add(taoPanelTrangChu(), BorderLayout.CENTER);
		pnCenter.revalidate();
		pnCenter.repaint();
	}

	private JPanel taoPanelTrangChu() {
		JPanel wrapper = new JPanel(new BorderLayout(16, 16));
		wrapper.setOpaque(false);
		wrapper.setBorder(new EmptyBorder(24, 24, 24, 24));

		JPanel topCards = new JPanel(new GridLayout(1, 3, 16, 0));
		topCards.setOpaque(false);

		try {
			ArrayList<Thuoc> thuocSapHetHan = thuocDAO.getThuocSapHetHan(30, 5);
			ArrayList<Thuoc> thuocSapHetHang = thuocDAO.getThuocSapHetHang(10, 5);
			double doanhThuHomNay = hoaDonDAO.getDoanhThuHomNay();
			NumberFormat tienVietNam = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

			topCards.add(taoTheTongQuan("Thuốc sắp hết hạn", String.valueOf(thuocSapHetHan.size()),
					"Trong 30 ngày tới"));
			topCards.add(taoTheTongQuan("Thuốc sắp hết hàng", String.valueOf(thuocSapHetHang.size()),
					"Tồn kho dưới hoặc bằng 10"));
			topCards.add(taoTheTongQuan("Doanh thu hôm nay", tienVietNam.format(doanhThuHomNay),
					new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())));

			JPanel content = new JPanel(new GridLayout(1, 2, 16, 0));
			content.setOpaque(false);
			content.add(taoPanelDanhSach("Thuốc sắp hết hạn", thuocSapHetHan, true));
			content.add(taoPanelDanhSach("Thuốc sắp hết hàng", thuocSapHetHang, false));

			wrapper.add(topCards, BorderLayout.NORTH);
			wrapper.add(content, BorderLayout.CENTER);
		} catch (SQLException e) {
			JPanel errorPanel = new JPanel(new BorderLayout());
			errorPanel.setBackground(new Color(255, 255, 255, 235));
			errorPanel.setBorder(new LineBorder(new Color(220, 220, 220), 2, true));
			JLabel lblError = new JLabel("Không thể tải dữ liệu trang chủ: " + e.getMessage());
			lblError.setBorder(new EmptyBorder(20, 20, 20, 20));
			errorPanel.add(lblError, BorderLayout.CENTER);
			wrapper.add(errorPanel, BorderLayout.CENTER);
		}

		return wrapper;
	}

	private JPanel taoTheTongQuan(String tieuDe, String giaTri, String moTa) {
		JPanel card = new JPanel(new BorderLayout(0, 8));
		card.setBackground(new Color(255, 255, 255, 235));
		card.setBorder(new EmptyBorder(20, 20, 20, 20));

		JLabel lblTitle = new JLabel(tieuDe);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitle.setForeground(new Color(35, 55, 86));

		JLabel lblValue = new JLabel(giaTri);
		lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
		lblValue.setForeground(new Color(0, 102, 204));

		JLabel lblDesc = new JLabel(moTa);
		lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblDesc.setForeground(new Color(90, 90, 90));

		card.add(lblTitle, BorderLayout.NORTH);
		card.add(lblValue, BorderLayout.CENTER);
		card.add(lblDesc, BorderLayout.SOUTH);
		return card;
	}

	private JPanel taoPanelDanhSach(String tieuDe, ArrayList<Thuoc> dsThuoc, boolean hienHanSuDung) {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBackground(new Color(255, 255, 255, 235));
		panel.setBorder(new EmptyBorder(18, 18, 18, 18));
		Color contentBackground = Color.WHITE;

		JLabel lblTitle = new JLabel(tieuDe);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitle.setForeground(new Color(35, 55, 86));
		panel.add(lblTitle, BorderLayout.NORTH);

		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFocusable(false);
		textArea.setOpaque(true);
		textArea.setBackground(contentBackground);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		textArea.setBorder(new EmptyBorder(6, 6, 6, 6));

		if (dsThuoc.isEmpty()) {
			textArea.setText("Không có dữ liệu.");
		} else {
			StringBuilder builder = new StringBuilder();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			for (Thuoc thuoc : dsThuoc) {
				builder.append("- ").append(thuoc.getTenThuoc())
						.append(" (").append(thuoc.getMaThuoc()).append(")");
				if (hienHanSuDung && thuoc.getHanSuDung() != null) {
					builder.append(" - HSD: ").append(sdf.format(thuoc.getHanSuDung()));
				} else {
					builder.append(" - Tồn: ").append(thuoc.getSoLuongTon());
				}
				builder.append("\n");
			}
			textArea.setText(builder.toString());
		}

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(null);
		scrollPane.setOpaque(true);
		scrollPane.setBackground(contentBackground);
		scrollPane.getViewport().setOpaque(true);
		scrollPane.getViewport().setBackground(contentBackground);
		panel.add(scrollPane, BorderLayout.CENTER);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		// Xử lý cho các chức năng Hoá Đơn (chuỗi phải khớp nhãn trong menu)
		if (command.equals("Danh sách hoá đơn") || command.equals("Quản lý hoá đơn")) {
			hienThiForm(new FormQuanLyHoaDon(this,taiKhoan));
		}
		else if(command.equals("Tìm kiếm hoá đơn")) {
			System.out.println("Hiển thị form tìm kiếm hóa đơn");
			hienThiForm(new FormTimKiemHoaDon());
		}
		else if (command.equals("Lập hoá đơn")) {
		    System.out.println("Hiển thị form lập hóa đơn");
		    try {
				hienThiForm(new FormLapHoaDon(taiKhoan));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		// Xử lý cho các chức năng Nhân Viên
		else if(command.equals("Quản lý nhân viên")) {
			hienThiForm(new FormQuanLyNhanVien());
		}
		else if(command.equals("Tìm kiếm nhân viên")) {
			hienThiForm(new FormTimKiemNV());
		}
		else if(command.equals("Khuyến mãi")) {
			hienThiForm(new FormKhuyenMai(taiKhoan));
		}
		else if(command.equals("Thuế")) {
			hienThiForm(new FormThue(taiKhoan));
		}
		else if (command.equals("Quản lý phiếu đặt thuốc") || command.equals("Phiếu Đặt Thuốc")) {
			hienThiForm(new FormPhieuDatThuoc(taiKhoan));
		}
		else if (command.equals("Quản lý phiếu nhập thuốc") || command.equals("Phiếu Nhập Thuốc")) {
			hienThiForm(new FormPhieuNhapThuoc(taiKhoan));
		}
		else if(command.equals("Thống kê")) {
			if (laNhanVienBanThuoc()) {
				thongBaoKhongCoQuyen();
				return;
			}
			hienThiForm(new FormThongKeLoiNhuan());
		}
		
		// Xử lý cho các chức năng Khách Hàng
		else if(command.equals("Quản lý khách hàng")) {
			hienThiForm(new FormQuanLyKhachHang(taiKhoan));
		}
		else if(command.equals("Tìm kiếm khách hàng")) {
			hienThiForm(new FormTimKiemKH());
		}
		
		// Xử lý cho các chức năng Tài Khoản
		else if(command.equals("Quản lý tài khoản")) {
			hienThiForm(new FormQuanLyTK());
		}
		
		// Xử lý cho các chức năng Thuốc
		else if(command.equals("Quản lý thuốc")) {
			hienThiForm(new FormQuanLyThuoc(taiKhoan));
		}
		else if(command.equals("Tìm kiếm thuốc")) {
			hienThiForm(new FormTimKiemThuoc());
		}
		else if(command.equals("Danh Mục Thuốc")) {
			try {
				hienThiForm(new FormDanhMucThuoc(taiKhoan));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
		else if(command.equals("Quản lý đổi/trả thuốc")) {
			hienThiForm(new FormQuanLyDoiTraThuoc());
		}
		
		// Xử lý cho các chức năng nhà cung cấp
		else if(command.equals("Quản lý nhà cung cấp")) {
			hienThiForm(new FormQuanLyNhaCungCap());
		} 
		else if (command.equals("Tìm kiếm nhà cung cấp")) {
			hienThiForm(new TimKiemNhaCungCap());
		}
		
		// Xử lý cho các chức năng hệ thống
		else if (command.equals("Trang chủ")) {
			hienThiTrangChu();
		}
		else if (command.equals("Đăng xuất")) {
			new Login().setVisible(true);
			this.dispose();
		}
		else if (command.equals("Thoát")) {
			System.exit(0);
		}
	}

	private boolean laNhanVienBanThuoc() {
		return taiKhoan != null && !"Nhân viên quản lý".equals(taiKhoan.getVaiTro());
	}

	private void thongBaoKhongCoQuyen() {
		JOptionPane.showMessageDialog(this,
				"Nhân viên không được phép truy cập mục này",
				"Không có quyền truy cập",
				JOptionPane.WARNING_MESSAGE);
	}
}
