package ru.otus.java.basic.project;

import java.util.Scanner;

public class Application {

  public static void main(String[] args) {

    try {
      FileManager fileManager = new FileManager();
      Scanner scanner = new Scanner(System.in);
      String input;
      do {
        System.out.print(fileManager.getCurrent() + ">");
        input = scanner.nextLine();
        fileManager.process(input);
        System.out.println();
      } while (!"exit".equals(input.trim()));
    } catch (Exception e) {
      System.out.println("Во время работы программы произошла ошибка: " + e.getMessage());
    }
  }
}
