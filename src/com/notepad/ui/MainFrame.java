package com.notepad.ui;

import com.notepad.core.DocumentState;
import com.notepad.core.FileManager;
import com.notepad.utils.Constants;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;

public class MainFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 900;
    private static final int DEFAULT_HEIGHT = 650;
    private static final int MIN_FONT_SIZE = 8;
    private static final int MAX_FONT_SIZE = 72;
    private static final int ZOOM_STEP = 2;

    private final DocumentState documentState;
    private final EditorPanel editorPanel;
    private final FileManager fileManager;
    private FindReplaceDialog findReplaceDialog;

    private final JCheckBoxMenuItem wordWrapItem;
    private final JCheckBoxMenuItem statusBarItem;
    private final JLabel statusBar = new JLabel();

    public MainFrame() {
        applyLookAndFeel();

        documentState = new DocumentState();
        editorPanel = new EditorPanel(documentState, this::updateTitle);
        wordWrapItem = new JCheckBoxMenuItem(Constants.MENU_FORMAT_WRAP,true);
        statusBarItem = new JCheckBoxMenuItem(Constants.MENU_VIEW_STATUSBAR,true);

        initWindow();
        fileManager = new FileManager(this, documentState);
        setJMenuBar(buildMenuBar());

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });
    }

    private void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Keep Swing's default look and feel if the system look and feel is unavailable.
        }
    }

    private void initWindow() {
        updateTitle();
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        add(editorPanel,BorderLayout.CENTER);
        initStatusBar();
    }

    private void initStatusBar() {
        statusBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        add(statusBar, BorderLayout.SOUTH);
        JTextArea textArea = editorPanel.getTextArea();
        textArea.addCaretListener(e -> updateStatusBar());
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateStatusBar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateStatusBar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateStatusBar();
            }
        });
        updateStatusBar();
    }

    private void updateStatusBar() {
        JTextArea textArea = editorPanel.getTextArea();
        // Document listeners can run before Swing moves the caret after a deletion.
        int caret = Math.min(textArea.getCaretPosition(), textArea.getDocument().getLength());
        try {
            int line = textArea.getLineOfOffset(caret);
            int column = caret - textArea.getLineStartOffset(line) + 1;
            String text = textArea.getText().trim();
            int words = text.isEmpty() ? 0 : text.split("\\s+").length;
            statusBar.setText(Constants.STATUS_LINE + " " + (line + 1)
                    + ", " + Constants.STATUS_COL + " " + column
                    + " | " + Constants.STATUS_WORDS + " " + words
                    + " | " + Constants.DEFAULT_ENCODING);
        } catch (BadLocationException ex) {
            throw new IllegalStateException("Invalid caret position", ex);
        }
    }

    private JMenuBar buildMenuBar() {
        wordWrapItem.addActionListener(e -> editorPanel.setWordWrap(wordWrapItem.isSelected()));
        statusBarItem.addActionListener(e -> {
            statusBar.setVisible(statusBarItem.isSelected());
            revalidate();
            repaint();
        });

        return new MenuBarBuilder(
                e -> onNew(),
                e -> onOpen(),
                e -> onSave(),
                e -> onSaveAs(),
                e -> onExit(),
                e -> onUndo(),
                e -> onRedo(),
                e -> onFindReplace(),
                e -> onFont(),
                wordWrapItem,
                statusBarItem,
                e -> onZoomIn(),
                e -> onZoomOut()
        ).build();
    }

    private boolean confirmUnsavedChanges() {
        boolean proceed = fileManager.promptIfUnsaved(editorPanel.getText());
        updateTitle();
        return proceed;
    }

    private void onNew() {
        if(!confirmUnsavedChanges()) return;
        editorPanel.clear();
        documentState.reset();
        updateTitle();
    }

    private void onOpen() {
        if(!confirmUnsavedChanges()) return;
        String content = fileManager.openFile();
        if(content != null) {
            editorPanel.setText(content);
            updateTitle();
        }
    }

    private void onSave() {
        fileManager.save(editorPanel.getText());
        updateTitle();
    }

    private void onSaveAs() {
        fileManager.saveAs(editorPanel.getText());
        updateTitle();
    }

    private void onExit() {
        if(!confirmUnsavedChanges()) return;
        System.exit(0);
    }

    private void onUndo() {
        editorPanel.undo();
    }

    private void onRedo() {
        editorPanel.redo();
    }

    private void onFindReplace() {
        if (findReplaceDialog == null) {
            findReplaceDialog = new FindReplaceDialog(this, editorPanel);
        }
        findReplaceDialog.setVisible(true);
    }

    private void onFont() {
        Font currentFont = editorPanel.getEditorFont();
        String[] families = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        JComboBox<String> family = new JComboBox<>(families);
        family.setSelectedItem(currentFont.getFamily());
        JSpinner size = new JSpinner(new SpinnerNumberModel(
                currentFont.getSize(), MIN_FONT_SIZE, MAX_FONT_SIZE, 1));

        JPanel fields = new JPanel(new GridLayout(2, 2, 8, 8));
        fields.add(new JLabel("Font family:"));
        fields.add(family);
        fields.add(new JLabel("Font size:"));
        fields.add(size);
        int result = JOptionPane.showConfirmDialog(this, fields, "Font",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            size.commitEdit();
            editorPanel.setEditorFont(new Font((String) family.getSelectedItem(),
                    Font.PLAIN, (Integer) size.getValue()));
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Enter a font size between " + MIN_FONT_SIZE + " and " + MAX_FONT_SIZE + ".",
                    "Invalid font size", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onZoomIn() {
        zoom(ZOOM_STEP);
    }

    private void onZoomOut() {
        zoom(-ZOOM_STEP);
    }

    private void zoom(int step) {
        Font font = editorPanel.getEditorFont();
        int size = Math.max(MIN_FONT_SIZE, Math.min(MAX_FONT_SIZE, font.getSize() + step));
        editorPanel.setEditorFont(font.deriveFont((float) size));
    }

    private void updateTitle() {
        String name = (documentState.getCurrentFilePath() != null) ? new java.io.File(documentState.getCurrentFilePath()).getName() : Constants.UNTITLED;
        String modified = documentState.isModified() ? "*" : "";
        setTitle(Constants.APP_NAME + " - " + name + modified);
    }
}
