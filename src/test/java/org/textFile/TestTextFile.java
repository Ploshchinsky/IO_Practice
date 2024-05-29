package org.textFile;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TestTextFile {
    @Test
    public void test1() {
        String textToFile1 = "First string for this file :)";
        String textToFile2 = "It's a second file!";
        TextFile file1 = new TextFile("src/main/resources/file1.txt", 2048);
        TextFile file2 = new TextFile("src/main/resources/file2.txt", 500);

        file1.add(textToFile1);
        file2.add(textToFile2);
        file1.copyFrom(file2, true);
        file1.delete("second");

        String expected = (textToFile1 + textToFile2).replace("second", "");
        String actual = file1.read();
        Assertions.assertEquals(expected, actual);
    }
}


