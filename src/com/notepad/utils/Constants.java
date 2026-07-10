package com.notepad.utils;

public class Constants {
    private Constants() {}

    // Application
    public static final String APP_NAME = "Notepad";
    public static final String UNTITLED = "Untitled";
    public static final String FILE_EXTENSION = ".txt";
    public static final String FILE_FILTER_DESC = "Text Files (*.txt)";
    public static final String DEFAULT_ENCODING = "UTF-8";

    // Menu - File
    public static final String MENU_FILE = "File";
    public static final String MENU_FILE_NEW = "New";
    public static final String MENU_FILE_OPEN = "Open..";
    public static final String MENU_FILE_SAVE = "Save";
    public static final String MENU_FILE_SAVE_AS = "Save as..";
    public static final String MENU_FILE_ENCRYPT = "Encrypt & Save";
    public static final String MENU_FILE_DECRYPT = "Open & Decrypt";
    public static final String MENU_FILE_EXIT = "Exit";

    // Menu - Edit
    public static final String MENU_EDIT = "Edit";
    public static final String MENU_EDIT_UNDO = "Undo";
    public static final String MENU_EDIT_REDO = "Redo";
    public static final String MENU_EDIT_CUT = "Cut";
    public static final String MENU_EDIT_COPY = "Copy";
    public static final String MENU_EDIT_PASTE = "Paste";
    public static final String MENU_EDIT_SELECT_ALL = "Select All";
    public static final String MENU_EDIT_FIND = "Find & Replace";

    // Menu - Format
    public static final String MENU_FORMAT = "Format";
    public static final String MENU_FORMAT_FONT = "Font..";
    public static final String MENU_FORMAT_WRAP = "Word Wrap";

    // Menu - View
    public static final String MENU_VIEW = "View";
    public static final String MENU_VIEW_STATUSBAR = "Status Bar";
    public static final String MENU_VIEW_ZOOM_IN = "Zoom In";
    public static final String MENU_VIEW_ZOOM_OUT = "Zoom Out";

    // Dialog - messages
    public static final String MSG_UNSAVED_CHANGES = "You have unsaved changes. do you want to save before Continuing?";
    public static final String MSG_SAVE_ERROR = "Failed to save the file";
    public static final String MSG_OPEN_ERROR = "Failed to open the file";
    public static final String MSG_ENCRYPT_ERROR = "Encryption Failed";
    public static final String MSG_DECRYPT_ERROR = "Decryption Failed.Wrong Password?";
    public static final String MSG_PASSWORD_PROMPT = "Enter Password";
    public static final String MSG_CONFIRM_EXIT = "Exit without saving?";

    // Status Bar labels
    public static final String STATUS_LINE = "Ln";
    public static final String STATUS_COL = "Col";
    public static final String STATUS_WORDS = "Words";
    public static final String STATUS_ENC = "Encoding";

}
