package com.notepad.core;

import com.notepad.utils.Constants;
import com.notepad.utils.UIUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileManager {
    private final Component parent;
    private final DocumentState state;

    public FileManager(Component parent, DocumentState state) {
        this.parent = parent;
        this.state = state;
    }

    // Open
    public String openFile() {
        JFileChooser chooser = buildChooser();
        int result = chooser.showOpenDialog(parent);

        if(result != JFileChooser.APPROVE_OPTION) return null;
        File file = chooser.getSelectedFile();
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            String content = new String(bytes, StandardCharsets.UTF_8);

            state.setCurrentFilePath(file.getAbsolutePath());
            state.markSaved();

            return content;
        } catch (IOException ex) {
            UIUtils.showError(parent,Constants.MSG_OPEN_ERROR + "\n" + ex.getMessage());
            return null;
        }
    }

    public boolean save(String content) {
        if(state.getCurrentFilePath() == null) {
            return saveAs(content);
        }
        return writeToPath(content,state.getCurrentFilePath());
    }

    public boolean saveAs(String content) {
        JFileChooser chooser = buildChooser();
        int result = chooser.showSaveDialog(parent);
        if(result != JFileChooser.APPROVE_OPTION) {
            return false;
        }

        File file = chooser.getSelectedFile();
        if(!file.getName().endsWith(Constants.FILE_EXTENSION)) {
            file = new File(file.getAbsolutePath() + Constants.FILE_EXTENSION);
        }

        boolean saved = writeToPath(content, file.getAbsolutePath());
        if(saved) {
            state.setCurrentFilePath(file.getAbsolutePath());
        }
        return saved;
    }

    private JFileChooser buildChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(Constants.FILE_FILTER_DESC,"txt"));
        chooser.setAcceptAllFileFilterUsed(true);
        return chooser;
    }

    private boolean writeToPath(String content, String filePath) {
        try {
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            Files.write(Paths.get(filePath),bytes, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
            state.markSaved();
            return true;
        } catch (IOException ex) {
            UIUtils.showError(parent,Constants.MSG_SAVE_ERROR + "\n" + ex.getMessage());
            return false;
        }
    }

    public boolean promptIfUnsaved(String content) {
        if(!state.isModified()) {
            return true;
        }
        int choice = UIUtils.showYesNoCancel(parent,Constants.MSG_UNSAVED_CHANGES);
        if(choice == JOptionPane.YES_OPTION) {
            return save(content);
        } else if (choice == JOptionPane.NO_OPTION) {
            return true;
        } else {
            return false;
        }
    }
}
