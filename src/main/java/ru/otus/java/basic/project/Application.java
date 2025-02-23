package ru.otus.java.basic.project;

import java.util.Scanner;

public class Application {

  public static void main(String[] args) {

    FileManager fileManager = new FileManager();
    Scanner scanner = new Scanner(System.in);
    System.out.print(fileManager.getCurrent() + ">");
    String input = scanner.nextLine();

    while (!"exit".equals(input)) {
      String[] tokens = input.split(" ");
      fileManager.execute(tokens);
      System.out.println();
      System.out.print(fileManager.getCurrent() + ">");
      input = scanner.nextLine();
    }
  }
}