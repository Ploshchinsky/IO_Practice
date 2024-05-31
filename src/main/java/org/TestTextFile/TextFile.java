package org.TestTextFile;


import java.io.*;
import java.util.stream.Collectors;

public class TextFile {
    private File file;
    private StringBuilder buffer;
    private int bufferLength;
    private int bufferActualSize;

    public TextFile(String filePath) {
        file = new File(filePath);
        bufferLength = 1024;
    }

    public TextFile(String filePath, int bufferLength) {
        file = new File(filePath);
        this.bufferLength = bufferLength;
        buffer = new StringBuilder();
    }

    public void copyFrom(String anotherFilePath, boolean isAppend) {
        System.out.println("Copy to [" + anotherFilePath + "] starts...");
        if (file.exists()) {
            try (
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(anotherFilePath, isAppend));
            ) {
                writer.newLine();
                for (String s : reader.lines().collect(Collectors.toList())) {
                    writer.write(s + "\n");
                }
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Copy to [" + anotherFilePath + "] ends!");
    }

    public void copyFrom(TextFile anotherFile, boolean isAppend) {
        System.out.println("Copy from [" + anotherFile.getPath() + "] starts...");
        if (file.exists()) {
            try (
                    BufferedReader reader = new BufferedReader(new FileReader(anotherFile.getPath()));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file, isAppend));
            ) {
                writer.newLine();
                for (String s : reader.lines().collect(Collectors.toList())) {
                    writer.write(s + "\n");
                }
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Copy from [" + anotherFile.getPath() + "] ends!");
    }

    public void add(String text) {
        System.out.println("Add new text to file starts...");
        if (bufferActualSize < bufferLength) {
            addToBuffer(text);
        }
        addFromBufferToFile();
        System.out.println("Add new text to file ends!");
    }

    private void addToBuffer(String text) {
        bufferActualSize = text.length();
        if (bufferActualSize > bufferLength) {
            addFromBufferToFile();
        } else {
            buffer.append(text);
        }
    }

    private void addFromBufferToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (new BufferedReader(new FileReader(file)).readLine() != null) {
                writer.newLine();
            }
            writer.write(buffer.toString());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            buffer = new StringBuilder();
        }
    }

    public void delete(String text) {
        System.out.println("Delete text from file starts...");
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file));
        ) {
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line.replace(text, ""));
                line = reader.readLine();
            }
            replaceAll(buffer.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            buffer = new StringBuilder();
        }
        System.out.println("Delete text from file ends!");
    }

    private void replaceAll(String string) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(string);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(int bufferLength) {
        this.bufferLength = bufferLength;
    }

    public String getPath() {
        return file.getPath();
    }

    public String read() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.lines().forEach(buffer::append);
            return buffer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            buffer = new StringBuilder();
        }
    }

    public int getBufferActualSize() {
        return bufferActualSize;
    }
}
