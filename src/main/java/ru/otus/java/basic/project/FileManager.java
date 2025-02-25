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

  // TODO: добавить -f для mv и cp
  // TODO: добавить find
  // TODO: добавить обработку ошибок
  public void process(String input) {
    List<String> tokens = new ArrayList<>(Arrays.asList(input.trim().split("\\s+")));
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

  private void help() {
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
        System.out.printf(" %-25s%s%10s%n", fileName,
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
    String path = args.get(0);
    if ("..".equals(path)) {
      int index = this.current.lastIndexOf(File.separator);
      this.current = this.current.substring(0, index);
    } else {
      this.current = (isAbsolutePath(path)) ? path : this.current + File.separator + path;
    }
  }

  public void makeDirectory(List<String> args) throws Exception {
    String path = args.get(0);
    File file = new File((isAbsolutePath(path)) ? path : this.current + File.separator + path);
    if (!file.mkdir()) {
      throw new Exception();
    }
  }

  public void remove(List<String> args) throws Exception {
    String path = args.get(0);
    File file = new File((isAbsolutePath(path)) ? path : this.current + File.separator + path);
    if (!file.delete()) {
      throw new Exception();
    }
  }

  public void move(List<String> args) throws IOException {
    String sourcePath = args.get(0);
    String destinationPath = args.get(1);
    File sourceFile = new File(
        (isAbsolutePath(sourcePath)) ? sourcePath : this.current + "\\" + sourcePath);
    File destinationFile = new File((isAbsolutePath(destinationPath)) ? destinationPath
        : this.current + "\\" + destinationPath);
    FileUtils.moveToDirectory(sourceFile, destinationFile, destinationFile.exists());

  }

  public void copy(List<String> args) throws IOException {
    String sourcePath = args.get(0);
    String destinationPath = args.get(1);
    File sourceFile = new File(
        (isAbsolutePath(sourcePath)) ? sourcePath : current + File.separator + sourcePath);
    File destinationFile = new File((isAbsolutePath(destinationPath)) ? destinationPath
        : current + File.separator + destinationPath);
    FileUtils.copyFile(sourceFile, destinationFile);
  }

  public void info(List<String> args) {
    String path = args.get(0);
    File file = new File((isAbsolutePath(path)) ? path : this.current + "\\" + path);
    boolean isDirectory = file.isDirectory();
    System.out.printf(" %-25s%s%10s%n",
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

  private String convertTime(long time) {
    Date date = new Date(time);
    Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return format.format(date);
  }

  private boolean isAbsolutePath(String path) {
    return path.contains(this.root);
  }
}



