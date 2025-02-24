package ru.otus.java.basic.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class FileManager {

  private String current;
  private final String root;

  public FileManager() {
    current = String.valueOf(Paths.get(".").toAbsolutePath().normalize());
    root = String.valueOf(Paths.get(".").toAbsolutePath().normalize().getRoot());
  }

  public String getCurrent() {
    return current;
  }

  private String getRoot() {
    return root;
  }

  // TODO: добавить обработку ошибок
  // TODO: добавить в команды обработку абсолютного пути
  public void execute(String[] input) {
    List<String> tokens = new ArrayList<>(Arrays.asList(input));
    List<String> args = tokens.subList(1, tokens.size());
    try {
      String command = tokens.get(0);
      switch (command) {
        case "" -> blank();
        case "help" -> help();
        case "ls" -> list(args);
        case "cd" -> changeDirectory(args);
        case "mkdir" -> makeDirectory(args);
        case "rm" -> remove(args);
        case "mv" -> move(args);
        case "cp" -> copy(args);
        case "finfo" -> info(args);
        case "exit" -> exit();
        default -> {
          System.out.println(
              "Команда не найдена, введите help для получения списка доступных команд");
        }
      }
    } catch (Exception e) {
      System.out.println(
          "Невозможно выполнить команду, проверьте правильность введённой команды и повторите попытку");
    }
  }

  void help() {
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

  private void list(List<String> args) {
    File current = new File(this.current);
    boolean infoFlag = args.contains("-i");
    boolean isDirectory;
    String fileName;
    File[] files = current.listFiles();
    if (files == null) {
      return;
    }

    for (File file : files
    ) {
      isDirectory = file.isDirectory();
      fileName = file.getName() + (isDirectory ? File.separator : "");
      if (infoFlag) {
        System.out.printf(" %-15s%s%15s%n", fileName,
            convertTime(file.lastModified()),
            (isDirectory ? FileUtils.byteCountToDisplaySize(
                FileUtils.sizeOfDirectory(file))
                : FileUtils.byteCountToDisplaySize(file.length())));
      } else {
        System.out.printf(" %-15s%n", fileName);
      }
    }
  }

  private void changeDirectory(List<String> args) {
    String destinationName = args.get(0);
    if ("..".equals(destinationName)) {
      int index = this.current.lastIndexOf(File.separator);
      this.current = this.current.substring(0, index);
    } else {
      this.current += File.separator + destinationName;
    }
  }

  public void makeDirectory(List<String> args) throws Exception {
    String targetName = args.get(0);
    File file = new File(this.current + File.separator + targetName);
    if (!file.mkdir()) {
      throw new Exception();
    }
  }

  public void remove(List<String> args) throws Exception {
    String path = args.get(0);
    File file = new File(path);
    if (!file.delete()) {
      throw new Exception();
    }
  }

  public void move(List<String> args) throws IOException {
    String source = args.get(0);
    String destination = args.get(1);
    File sourceFile = new File(this.current + "\\" + source);
    File destinationFile = new File(this.current + "\\" + destination);
    FileUtils.moveToDirectory(sourceFile, destinationFile, destinationFile.exists());

  }

  public void copy(List<String> args) throws IOException {
    String sourceFile = args.get(0);
    String destinationFile = args.get(1);
    File source = new File(current + File.separator + sourceFile);
    File destination = new File(current + File.separator + destinationFile);
    FileUtils.copyFile(source, destination);
  }

  public void info(List<String> args) {
    String fileName = args.get(0);
    File file = new File(this.current + "\\" + fileName);
    boolean isDirectory = file.isDirectory();
    System.out.printf(" %-15s%s%15s%n",
        file.getName() + (isDirectory ? File.separator : ""),
        convertTime(file.lastModified()),
        (isDirectory ? FileUtils.byteCountToDisplaySize(
            FileUtils.sizeOfDirectory(file))
            : FileUtils.byteCountToDisplaySize(file.length())));
  }

  void blank() {
    System.out.println(
        "Невозможно выполнить пустую команду, введите help для получения списка доступных команд");
  }

  void exit() {
    System.out.println("Завершение работы...");
  }

  public String convertTime(long time) {
    Date date = new Date(time);
    Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return format.format(date);
  }
}



