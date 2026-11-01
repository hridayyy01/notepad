package com.notepad.ui;

import com.notepad.core.DocumentState;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class EditorPanel extends JPanel {
    private static final String DEFAULT_FONT_NAME = "Monospaced";
    private static final int DEFAULT_FONT_SIZE = 16;
    private static final int TAB_SIZE = 4;

    private final JTextArea textArea;
    private final JScrollPane scrollPane;
    private final DocumentState documentState;
    private final Runnable onModified;
    private boolean replacingText;

    public EditorPanel(DocumentState documentState, Runnable onModified) {
        this.documentState = documentState;
        this.onModified = onModified;
        setLayout(new BorderLayout());
        textArea = buildTextArea();
        scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        wireDocumentListener();
    }

    private JTextArea buildTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(new Font(DEFAULT_FONT_NAME,Font.PLAIN,DEFAULT_FONT_SIZE));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setTabSize(TAB_SIZE);
        return area;
    }

    private void wireDocumentListener() {
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                markModified();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                markModified();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
    }

    private void markModified() {
        if (!replacingText) {
            documentState.markModified();
            onModified.run();
        }
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        replacingText = true;
        try {
            textArea.setText(text);
            textArea.setCaretPosition(0);
        } finally {
            replacingText = false;
        }
    }

    public void clear() {
        setText("");
    }

    public void setWordWrap(boolean wrap) {
        textArea.setLineWrap(wrap);
        textArea.setWrapStyleWord(wrap);
    }

    public void setEditorFont(Font font) {
        textArea.setFont(font);
    }

    public Font getEditorFont() {
        return textArea.getFont();
    }
}
