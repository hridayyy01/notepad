package com.notepad.ui;

import com.notepad.utils.Constants;

import javax.swing.*;
import java.awt.*;

public class FindReplaceDialog extends JDialog {
    private final EditorPanel editor;
    private final JTextField searchField = new JTextField(20);
    private final JTextField replacementField = new JTextField(20);

    public FindReplaceDialog(JFrame owner, EditorPanel editor) {
        super(owner, Constants.MENU_EDIT_FIND, false);
        this.editor = editor;
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

        JPanel fields = new JPanel(new GridLayout(2, 2, 8, 8));
        fields.add(new JLabel("Find:"));
        fields.add(searchField);
        fields.add(new JLabel("Replace with:"));
        fields.add(replacementField);

        JButton findNext = new JButton("Find Next");
        JButton replace = new JButton("Replace");
        JButton replaceAll = new JButton("Replace All");
        JButton close = new JButton("Close");
        findNext.addActionListener(e -> findNext());
        replace.addActionListener(e -> replace());
        replaceAll.addActionListener(e -> replaceAll());
        close.addActionListener(e -> setVisible(false));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(findNext);
        buttons.add(replace);
        buttons.add(replaceAll);
        buttons.add(close);

        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        content.add(fields, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        setContentPane(content);
        getRootPane().setDefaultButton(findNext);
        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    private boolean hasSearchText() {
        if (!searchField.getText().isEmpty()) return true;
        showMessage("Enter text to find.");
        return false;
    }

    private void findNext() {
        if (hasSearchText() && !editor.findNext(searchField.getText())) {
            showMessage("No match found.");
        }
    }

    private void replace() {
        if (hasSearchText() && !editor.replace(searchField.getText(), replacementField.getText())) {
            showMessage("No match found.");
        }
    }

    private void replaceAll() {
        if (!hasSearchText()) return;
        int count = editor.replaceAll(searchField.getText(), replacementField.getText());
        showMessage("Replacements: " + count);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, getTitle(), JOptionPane.INFORMATION_MESSAGE);
    }
}
