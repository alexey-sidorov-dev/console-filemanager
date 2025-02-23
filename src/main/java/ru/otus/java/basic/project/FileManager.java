package ru.otus.java.basic.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;

public class FileManager {

  private Path current;
  private final Path root;

  public FileManager() {
    current = Paths.get(".").toAbsolutePath().normalize();
    root = current.getRoot();
  }

  public Path getCurrent() {
    return current;
  }

  public void setCurrent(Path current) {
    this.current = current;
  }

  public Path getRoot() {
    return root;
  }

  public void execute(String... tokens) {
    String command = tokens[0];
    switch (command) {
      case "help" -> help();
      case "cp" -> copy(tokens);
      default -> {
        System.out.println(
            "Команда не найдена, help для вывода списка доступных команд.");
      }
    }
  }

  public void help() {
    String help = """
        ls [-i] - вывести на экран списка файлов текущего каталога
        cd [path] - перейти в указанную поддиректорию
        mkdir [name] - создать новую директорию в текущем каталоге
        rm [filename] – удалить указанный файл или директорию
        mv [source] [destination] – переименовать или перенести файл или директорию
        cp [source] [destination] – скопировать файл или директорию
        finfo [filename] – получить подробную информацию о файле
        help – вывести в консоль всех поддерживаемых команд
        exit – завершить работу
        """;
    System.out.print(help);
  }

  public void copy(String... tokens) {
    String sourceFile = tokens[1];
    String destinationFile = tokens[2];
    File source = new File(current + File.separator + sourceFile);
    File destination = new File(current + File.separator + destinationFile);
    try {
      FileUtils.copyFile(source, destination);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(
          "Невозможно скопировать файл. Проверьте правильность введённой команды и повторите попытку.");
    }
  }
}



