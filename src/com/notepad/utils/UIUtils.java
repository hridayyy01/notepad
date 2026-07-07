package com.notepad.utils;

import javax.swing.*;
import java.awt.*;

public final class UIUtils {
    private UIUtils() {};
    public static void showError(Component parent,String message) {
        JOptionPane.showMessageDialog(parent,message,"Error",JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(Component parent,String message) {
        JOptionPane.showMessageDialog(parent,message,"Info",JOptionPane.INFORMATION_MESSAGE);
    }

    public static int showYesNoCancel(Component parent,String message) {
        return JOptionPane.showConfirmDialog(parent,message,Constants.APP_NAME,JOptionPane.YES_NO_CANCEL_OPTION);
    }

    public static boolean showConfirm(Component parent,String message) {
        int result = JOptionPane.showConfirmDialog(parent,message,Constants.APP_NAME,JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    public static String showInput(Component parent, String message) {
        String result = JOptionPane.showInputDialog(parent, message);
        return (result != null) ? result.trim() : null;
    }

    public static char[] showPasswordDialog(Component parent, String message) {
        JPasswordField pf = new JPasswordField(24);
        int option = JOptionPane.showConfirmDialog(
                parent,
                new Object[] {message, pf},
                Constants.APP_NAME,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if(option == JOptionPane.OK_OPTION) {
            return pf.getPassword();
        }
        return null;
    }
}
