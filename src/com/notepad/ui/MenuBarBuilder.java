package com.notepad.ui;

import com.notepad.utils.Constants;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MenuBarBuilder {
    private final ActionListener onNew;
    private final ActionListener onOpen;
    private final ActionListener onSave;
    private final ActionListener onSaveAs;
    private final ActionListener onEncrypt;
    private final ActionListener onDecrypt;
    private final ActionListener onExit;

    private final ActionListener onUndo;
    private final ActionListener onRedo;
    private final ActionListener onFindReplace;

    private final ActionListener onFont;
    private final JCheckBoxMenuItem wordWrapItem;

    private final JCheckBoxMenuItem statusBarItem;
    private final ActionListener onZoomIn;
    private final ActionListener onZoomOut;

    private static final int MENU_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

    public MenuBarBuilder(
            ActionListener onNew,
            ActionListener onOpen,
            ActionListener onSave,
            ActionListener onSaveAs,
            ActionListener onEncrypt,
            ActionListener onDecrypt,
            ActionListener onExit,
            ActionListener onUndo,
            ActionListener onRedo,
            ActionListener onFindReplace,
            ActionListener onFont,
            JCheckBoxMenuItem wordWrapItem,
            JCheckBoxMenuItem statusBarItem,
            ActionListener onZoomIn,
            ActionListener onZoomOut
    ) {
        this.onNew = onNew;
        this.onOpen = onOpen;
        this.onSave = onSave;
        this.onSaveAs = onSaveAs;
        this.onEncrypt = onEncrypt;
        this.onDecrypt = onDecrypt;
        this.onExit = onExit;
        this.onUndo = onUndo;
        this.onRedo = onRedo;
        this.onFindReplace = onFindReplace;
        this.onFont = onFont;
        this.wordWrapItem = wordWrapItem;
        this.statusBarItem = statusBarItem;
        this.onZoomIn = onZoomIn;
        this.onZoomOut = onZoomOut;
    }

    public JMenuBar build() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(buildFileMenu());
        menuBar.add(buildEditMenu());
        menuBar.add(buildFormatMenu());
        return menuBar;
    }

    private JMenu buildFileMenu() {
        JMenu menu = new JMenu(Constants.MENU_FILE);
        menu.setMnemonic(KeyEvent.VK_F);

        menu.add(item(Constants.MENU_FILE_NEW,onNew,KeyEvent.VK_N,0));
        menu.add(item(Constants.MENU_FILE_OPEN,onOpen,KeyEvent.VK_O,0));
        menu.add(item(Constants.MENU_FILE_SAVE,onSave,KeyEvent.VK_S,0));
        menu.add(item(Constants.MENU_FILE_SAVE_AS,onSaveAs,KeyEvent.VK_S,KeyEvent.SHIFT_DOWN_MASK));
        menu.addSeparator();
        menu.add(item(Constants.MENU_FILE_ENCRYPT,onEncrypt,KeyEvent.VK_E,InputEvent.SHIFT_DOWN_MASK));
        menu.add(item(Constants.MENU_FILE_DECRYPT,onDecrypt,KeyEvent.VK_D,InputEvent.SHIFT_DOWN_MASK));
        menu.addSeparator();
        menu.add(item(Constants.MENU_FILE_EXIT,onExit,KeyEvent.VK_Q,0));

        return menu;
    }

    private JMenu buildFormatMenu() {
        JMenu menu = new JMenu(Constants.MENU_FORMAT);
        menu.setMnemonic(KeyEvent.VK_O);
        menu.add(item(Constants.MENU_FORMAT_FONT,onFont,0,0));
        menu.add(wordWrapItem);
        return menu;
    }

    private JMenu buildEditMenu() {
        JMenu menu = new JMenu(Constants.MENU_EDIT);
        menu.setMnemonic(KeyEvent.VK_E);

        menu.add(item(Constants.MENU_EDIT_UNDO,onUndo,KeyEvent.VK_Z,0));
        menu.add(item(Constants.MENU_EDIT_REDO,onRedo,KeyEvent.VK_Y,0));
        menu.addSeparator();

        menu.add(actionItem(Constants.MENU_EDIT_CUT,"cut",KeyEvent.VK_X));
        menu.add(actionItem(Constants.MENU_EDIT_COPY,"copy",KeyEvent.VK_C));
        menu.add(actionItem(Constants.MENU_EDIT_PASTE,"paste",KeyEvent.VK_V));
        menu.add(actionItem(Constants.MENU_EDIT_SELECT_ALL,"select-all",KeyEvent.VK_A));
        menu.addSeparator();

        menu.add(item(Constants.MENU_EDIT_FIND, onFindReplace,KeyEvent.VK_H,0));
        return menu;
    }

    private JMenu buildViewMenu() {
        JMenu menu = new JMenu(Constants.MENU_VIEW);
        menu.setMnemonic(KeyEvent.VK_V);

        menu.add(statusBarItem);
        menu.addSeparator();
        menu.add(item(Constants.MENU_VIEW_ZOOM_IN,onZoomIn,KeyEvent.VK_PLUS,InputEvent.CTRL_DOWN_MASK));
        menu.add(item(Constants.MENU_VIEW_ZOOM_OUT,onZoomOut,KeyEvent.VK_MINUS,InputEvent.CTRL_DOWN_MASK));

        return menu;
    }

    private JMenuItem item(String label,ActionListener listener,int keyCode,int extraMod) {
        JMenuItem mi = new JMenuItem(label);
        mi.addActionListener(listener);
        if(keyCode != 0) {
            mi.setAccelerator(KeyStroke.getKeyStroke(keyCode,MENU_MASK | extraMod));
        }
        return mi;
    }

    private JMenuItem actionItem(String label,String actionName,int keyCode) {
        JMenuItem mi = new JMenuItem(label);
        String finalActionName = actionName;
        mi.addActionListener(e -> {
            Action action = null;
            Component focused = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if(focused instanceof JTextArea) {
                JTextArea ta = (JTextArea) focused;
                action = ta.getActionMap().get(finalActionName);
            }
            if(action != null) action.actionPerformed(e);
        });
        mi.setAccelerator(KeyStroke.getKeyStroke(keyCode, MENU_MASK));
        return mi;
    }
}
