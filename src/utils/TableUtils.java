package utils;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ContainerEvent;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

public final class TableUtils {
    private static boolean installed;

    private TableUtils() {
    }

    public static synchronized void installGlobalTableLock() {
        if (installed) {
            return;
        }
        installed = true;

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event instanceof ContainerEvent) {
                    ContainerEvent containerEvent = (ContainerEvent) event;
                    if (containerEvent.getID() == ContainerEvent.COMPONENT_ADDED) {
                        lockTablesIn(containerEvent.getChild());
                    }
                }
            }
        }, AWTEvent.CONTAINER_EVENT_MASK);
    }

    public static void lockTablesIn(Component component) {
        if (component instanceof JTable) {
            lockTable((JTable) component);
        }

        if (component instanceof Container) {
            Component[] children = ((Container) component).getComponents();
            for (Component child : children) {
                lockTablesIn(child);
            }
        }
    }

    public static void lockTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setReorderingAllowed(false);
            header.setResizingAllowed(false);
        }
    }
}
