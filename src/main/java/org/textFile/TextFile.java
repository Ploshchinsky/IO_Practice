package org.textFile;


import java.io.*;
import java.util.List;
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

    public void copyTo(String anotherFilePath, boolean isAppend) {
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

    public void copyTo(TextFile anotherFile, boolean isAppend) {
        System.out.println("Copy to [" + anotherFile.getPath() + "] starts...");
        if (file.exists()) {
            try (
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(anotherFile.getPath(), isAppend));
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
        System.out.println("Copy to [" + anotherFile.getPath() + "] ends!");
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
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

    private void delete(String text) {
        System.out.println("Delete text from file starts...");
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file));
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        ) {
            List<String> tempFile = reader.lines().filter(s -> !s.equals(text)).collect(Collectors.toList());
            writer.newLine();
            for (String s : tempFile) {
                writer.write(s);
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Delete text from file ends!");
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
