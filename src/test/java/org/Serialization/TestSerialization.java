package org.Serialization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestSerialization {
    @Test
    public void test1() {
        String pathFile1 = "src/main/resources/object1.bin";
        String pathFile2 = "src/main/resources/object2.bin";

        Car car = new Car("Nissan", "350Z", 210);
        Employee employee1 = new Employee("Ivan Ivanov", 29, 190_000, car);
        Employee employee2 = new Employee("Petr Petrov", 31, 235_000, null);
        Employee employee3 = new Employee("Maria Sidorova", 22, 186_000, null);

        List<Employee> employeeList = List.of(employee1, employee2, employee3);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(pathFile2))) {
            for (Employee e : employeeList) {
                outputStream.writeObject(e);
            }
            System.out.println("All objects from List has been write!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Employee> readFromFile = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(Paths.get(pathFile2)))) {
            Employee temp;
            while (true) {
                try {
                    temp = (Employee) inputStream.readObject();
                } catch (EOFException ex) {
                    break;
                }
                readFromFile.add(temp);
            }
            System.out.println("All objects from file has been read!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("-----");
        readFromFile.forEach(System.out::println);
        List<Employee> expected = employeeList;
        List<Employee> actual = readFromFile;
        Assertions.assertEquals(expected, actual);
    }
}
