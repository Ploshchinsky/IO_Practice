package org.Serialization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestSerialization {
    public static List<Employee> startList;
    public static List<Employee> endList;
    public static String pathFile1;
    public static String pathFile2;

    @BeforeEach
    public void beforeEach() {
        pathFile1 = "src/main/resources/object1.bin";
        pathFile2 = "src/main/resources/object2.bin";
        Car car = new Car("Nissan", "350Z", 210);
        Employee employee1 = new Employee("Ivan Ivanov", 29, 190_000, car);
        Employee employee2 = new Employee("Petr Petrov", 31, 235_000, null);
        Employee employee3 = new Employee("Maria Sidorova", 22, 186_000, null);

        startList = List.of(employee1, employee2, employee3);
        endList = new ArrayList<>();
    }

    @Test
    public void test1() {
        //write all objects from list to file
        System.out.println("Write to the file starts...");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(pathFile2))) {
            for (Employee e : startList) {
                outputStream.writeObject(e);
            }
            System.out.println("All objects from List has been write!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //read all objects from file to list
        System.out.println("Read from the file starts...");
        try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(Paths.get(pathFile2)))) {
            Employee temp;
            while (true) {
                try {
                    temp = (Employee) inputStream.readObject();
                } catch (EOFException ex) {
                    break;
                }
                endList.add(temp);
            }
            System.out.println("All objects from the file has been read!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("-----");
        endList.forEach(System.out::println);
        List<Employee> expected = startList;
        List<Employee> actual = endList;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test2() {
        //write from list to file2 (only Employee with higher salary)
        System.out.println("Write to the file2 starts...");
        Employee salaryMax;
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(pathFile2))) {
            salaryMax = startList.stream().max(Comparator.comparing(Employee::getSalary)).get();
            outputStream.writeObject(salaryMax);
            System.out.println("Write is done!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //read from the file2
        System.out.println("Read from the file2 starts...");
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(pathFile2))) {
            while (true) {
                try {
                    endList.add((Employee) inputStream.readObject());
                } catch (EOFException e) {
                    System.out.println("Read is done!");
                    break;
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Employee expected = startList.stream().max(Comparator.comparing(Employee::getSalary)).get();
        Employee actual = endList.get(0);
        Assertions.assertEquals(expected, actual);
    }
}
