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
      tokens = input.trim().split("\\s+");
      fileManager.execute(tokens);
      System.out.println();
    } while (!"exit".equals(input));
  }
}