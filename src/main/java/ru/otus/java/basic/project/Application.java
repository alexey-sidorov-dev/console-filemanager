package ru.otus.java.basic.project;

import java.util.Scanner;

public class Application {

  public static void main(String[] args) {

    FileManager fileManager = new FileManager();
    Scanner scanner = new Scanner(System.in);
    String input;
    String[] tokens;

    do {
      System.out.print(fileManager.getCurrent() + ">");
      input = scanner.nextLine();
      fileManager.process(input);
      System.out.println();
    } while (!"exit".equals(input.trim()));
  }
}