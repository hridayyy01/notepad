# Notepad

A small desktop plain-text editor built with Java Swing. It supports editing UTF-8 text files, tracks unsaved changes, and separates editor behavior from file handling and window controls.

## Implemented features

- New, Open, Save, Save As, and Exit with unsaved-change prompts
- Document title showing the filename and a `*` for unsaved edits
- Undo, Redo, Cut, Copy, Paste, and Select All
- Case-sensitive Find Next, Replace, and Replace All
- Word Wrap and a font family/size chooser
- Zoom In/Out within an 8–72 point font size range
- Show/hide status bar with line, column, word count, and UTF-8 encoding

## Project structure

Sources are under `src/com/notepad/`:

- `Main.java`: starts the application.
- `ui/`: the main window, editor, menus, and Find & Replace dialog.
- `core/`: document state and file operations.
- `utils/`: shared constants and message dialogs.

## Technologies

Java 21, Java Swing, and Java NIO. No external dependencies or build tools are required.

## How to run

Install JDK 21. From the repository root on macOS/Linux:

```sh
mkdir -p out
javac --release 21 -d out $(find src -name '*.java')
java -cp out com.notepad.Main
```

Alternatively, open the project in IntelliJ IDEA, select JDK 21, mark `src` as the Sources Root if needed, and run `com.notepad.Main`.

## What I learned

Event-driven Swing programming, file I/O with Java NIO, application state management, and separation of responsibilities between UI components and file operations.
