package gui.dialog;

import java.awt.Frame;

import entity.PhieuNhapThuoc;
import gui.form.FormPhieuNhapThuoc;

public class DialogCapNhatPhieuNhap extends DialogThemPhieuNhap {
    public DialogCapNhatPhieuNhap(Frame parent, FormPhieuNhapThuoc form, PhieuNhapThuoc pnh) {
        super(parent, form);
        setEditMode(pnh);
    }
}
