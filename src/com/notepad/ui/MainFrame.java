package com.notepad.ui;

import com.notepad.core.DocumentState;
import com.notepad.core.FileManager;
import com.notepad.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 900;
    private static final int DEFAULT_HEIGHT = 650;

    private final DocumentState documentState;
    private final EditorPanel editorPanel;
    private final FileManager fileManager;
    private FindReplaceDialog findReplaceDialog;

    private final JCheckBoxMenuItem wordWrapItem;
    private final JCheckBoxMenuItem statusBarItem;

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

        }
    }

    private void initWindow() {
        updateTitle();
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        add(editorPanel,BorderLayout.CENTER);
    }

    private JMenuBar buildMenuBar() {
        wordWrapItem.addActionListener(e -> editorPanel.setWordWrap(wordWrapItem.isSelected()));
        statusBarItem.addActionListener(e -> {});

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

    private void onFont() {}
    private void onZoomIn() {}
    private void onZoomOut() {}

    private void updateTitle() {
        String name = (documentState.getCurrentFilePath() != null) ? new java.io.File(documentState.getCurrentFilePath()).getName() : Constants.UNTITLED;
        String modified = documentState.isModified() ? "*" : "";
        setTitle(Constants.APP_NAME + " - " + name + modified);
    }
}
