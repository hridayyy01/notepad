package com.notepad.utils;

import javax.swing.*;
import java.awt.*;

public final class UIUtils {
    private UIUtils() {}
    public static void showError(Component parent,String message) {
        JOptionPane.showMessageDialog(parent,message,"Error",JOptionPane.ERROR_MESSAGE);
    }

    public static int showYesNoCancel(Component parent,String message) {
        return JOptionPane.showConfirmDialog(parent,message,Constants.APP_NAME,JOptionPane.YES_NO_CANCEL_OPTION);
    }

}
