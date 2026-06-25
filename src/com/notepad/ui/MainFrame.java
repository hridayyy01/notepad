package com.notepad.ui;

import com.notepad.core.DocumentState;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 900;
    private static final int DEFAULT_HEIGHT = 650;

    private final DocumentState documentState;
    private final EditorPanel editorPanel;

    public MainFrame() {
        applyLookAndFeel();
        documentState = new DocumentState();
        editorPanel = new EditorPanel(documentState);
        initWindow();
    }

    private void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

        }
    }

    private void initWindow() {
        setTitle("Notepad");
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        add(editorPanel,BorderLayout.CENTER);
    }
}
