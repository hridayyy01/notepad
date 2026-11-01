package com.notepad.ui;

import com.notepad.core.DocumentState;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;
import java.awt.*;

public class EditorPanel extends JPanel {
    private static final String DEFAULT_FONT_NAME = "Monospaced";
    private static final int DEFAULT_FONT_SIZE = 16;
    private static final int TAB_SIZE = 4;

    private final JTextArea textArea;
    private final JScrollPane scrollPane;
    private final DocumentState documentState;
    private final Runnable onModified;
    private final UndoManager undoManager = new UndoManager();
    private boolean replacingText;

    public EditorPanel(DocumentState documentState, Runnable onModified) {
        this.documentState = documentState;
        this.onModified = onModified;
        setLayout(new BorderLayout());
        textArea = buildTextArea();
        scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        wireDocumentListener();
        textArea.getDocument().addUndoableEditListener(e -> {
            if (!replacingText) {
                undoManager.addEdit(e.getEdit());
            }
        });
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
                markModified();
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
            undoManager.discardAllEdits();
        }
    }

    public void clear() {
        setText("");
    }

    public void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    public void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }

    public boolean findNext(String search) {
        if (search.isEmpty()) return false;

        String content = getText();
        int index = content.indexOf(search, textArea.getSelectionEnd());
        if (index == -1) {
            index = content.indexOf(search);
        }
        if (index == -1) return false;

        textArea.select(index, index + search.length());
        // Keep the match visible while the Find & Replace dialog has focus.
        textArea.getCaret().setSelectionVisible(true);
        return true;
    }

    public boolean replace(String search, String replacement) {
        if (search.isEmpty()) return false;

        if (search.equals(textArea.getSelectedText())) {
            textArea.replaceSelection(replacement);
        }
        return findNext(search);
    }

    public int replaceAll(String search, String replacement) {
        if (search.isEmpty()) return 0;

        String content = getText();
        int count = 0;
        int index = content.indexOf(search);
        while (index != -1) {
            count++;
            index = content.indexOf(search, index + search.length());
        }
        if (count > 0) {
            textArea.replaceRange(content.replace(search, replacement), 0, content.length());
        }
        return count;
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
