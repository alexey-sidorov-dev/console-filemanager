package ru.otus.java.basic.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class FileManager {

  private String current;
  private final String root;

  public FileManager() {
    Path path = Paths.get(".");
    current = String.valueOf(path.toAbsolutePath().normalize());
    root = String.valueOf(path.toAbsolutePath().normalize().getRoot());
  }

  public String getCurrent() {
    return current;
  }

  // TODO: добавить обработку ошибок
  // TODO: заменить ошибки на информационные сообщения, где штатная работа приложения
  // TODO: проверить все аргументы, где они требуются
  // TODO: добавить -f для mv и cp
  public void process(String input) {
    try {
      List<String> tokens = new ArrayList<>(Arrays.asList(input.trim().split("\\s+")));
      List<String> args = tokens.subList(1, tokens.size());
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
        case "find" -> find(args);
        case "exit" -> exit();
        default -> {
          System.out.println(
              "Команда не найдена, введите help для получения списка доступных команд");
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private void help() {
    String text = """
        ls [-i | --info] - вывести на экран списка файлов текущей директории
        cd [path] - перейти в указанную директорию
        mkdir [name] - создать новую директорию
        rm [filename] [-f | --force] – удалить указанный файл или директорию
        mv [source] [destination] [-f | --force] – переименовать или перенести файл или директорию
        cp [source] [destination] [-f | --force] – скопировать файл или директорию
        finfo [filename] – получить подробную информацию о файле
        find [filename] [-r | --recursive] - найти файл или директорию с указанным именем
        help – вывести в консоль всех поддерживаемых команд
        exit – завершить работу
        """;
    System.out.print(text);
  }

  // TODO: возможно, что стоит переделать на FileUtils
  private void list(List<String> args) {
    try {
      File current = new File(this.current);
      boolean infoFlag = args.contains("-i") || args.contains("--info");
      File[] files = current.listFiles();
      if (files == null) {
        throw new Exception("Ошибка получения списка файлов текущей директории");
      }
      for (File file : files
      ) {
        boolean isDirectory = file.isDirectory();
        String fileName = file.getName() + (isDirectory ? File.separator : "");

        if (infoFlag) {
          System.out.printf(" %-25s%s%20s%n", fileName,
              convertTime(file.lastModified()),
              (isDirectory ? FileUtils.byteCountToDisplaySize(
                  FileUtils.sizeOfDirectory(file))
                  : FileUtils.byteCountToDisplaySize(file.length())));
        } else {
          System.out.printf(" %s%n", fileName);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(
          "При отображении файлов каталога произошла ошибка: " + e.getMessage());
    }
  }

  private void changeDirectory(List<String> args) {
    try {
      String path = args.get(0);
      if ("/".equals(path)) {
        this.current = this.root;
      } else if ("..".equals(path)) {
        int index = this.current.lastIndexOf(File.separator);
        this.current = this.current.substring(0, index);
      } else {
        File directory = new File(
            (isAbsolutePath(path)) ? path : this.current + File.separator + path);
        if (!directory.exists()) {
          throw new Exception("Такой директории не существует");
        }
        this.current = (isAbsolutePath(path)) ? path : this.current + File.separator + path;
      }
    } catch (Exception e) {
      throw new RuntimeException(
          "При переходе в директорию произошла ошибка: " + e.getMessage());
    }
  }

  private void makeDirectory(List<String> args) throws Exception {
    try {
      String path = args.get(0);
      File file = new File((isAbsolutePath(path)) ? path : this.current + File.separator + path);
      if (file.exists()) {
        throw new Exception("Такая папка уже существует");
      }
      if (!file.mkdir()) {
        throw new Exception("Не удалось создать папку");
      }
    } catch (Exception e) {
      throw new RuntimeException("При создании папки произошла ошибка: " + e.getMessage());
    }
  }

  private void remove(List<String> args) throws Exception {
    try {
      String path = args.get(0);
      boolean forceFlag = args.contains("-f") || args.contains("--force");
      File file = new File((isAbsolutePath(path)) ? path : this.current + File.separator + path);
      if (!file.exists()) {
        throw new Exception("Указанные папка или файл не существуют");
      }
      if (forceFlag) {
        FileUtils.forceDelete(file);
        return;
      }
      if (!file.delete()) {
        throw new Exception("Не удалось удалить папку или файл");
      }
    } catch (Exception e) {
      throw new RuntimeException(
          "При удалении папки или файла произошла ошибка: " + e.getMessage());
    }
  }

  // FIXME: сообщение об ошибке и флаг
  private void move(List<String> args) throws IOException {
    String sourcePath = args.get(0);
    boolean forceFlag = args.contains("-f") || args.contains("--force");
    String destinationPath = args.get(1);
    File sourceFile = new File(
        (isAbsolutePath(sourcePath)) ? sourcePath : this.current + File.separator + sourcePath);
    File destinationFile = new File((isAbsolutePath(destinationPath)) ? destinationPath
        : this.current + "\\" + destinationPath);
    // FIXME:
    FileUtils.moveToDirectory(sourceFile, destinationFile, destinationFile.exists());

  }

  // FIXME: сообщение об ошибке и флаг
  private void copy(List<String> args) throws Exception {
    String sourcePath = args.get(0);
    String destinationPath = args.get(1);
    boolean forceFlag = args.contains("-f") || args.contains("--force");
    File sourceFile = new File(
        (isAbsolutePath(sourcePath)) ? sourcePath : this.current + File.separator + sourcePath);
    File destinationFile = new File((isAbsolutePath(destinationPath)) ? destinationPath
        : this.current + File.separator + destinationPath);
    if (!sourceFile.exists()) {
      throw new Exception("Неверный путь для исходных файла или папки");
    }

    if (sourceFile.isDirectory()) {
      FileUtils.copyDirectory(sourceFile, destinationFile);
    } else {
      FileUtils.copyFile(sourceFile, destinationFile);
    }
  }

  private void info(List<String> args) throws Exception {
    try {
      String path = args.get(0);
      File file = new File((isAbsolutePath(path)) ? path : this.current + File.separator + path);
      if (!file.exists()) {
        throw new Exception("Не найдены папка или файл");
      }
      boolean isDirectory = file.isDirectory();
      System.out.printf(" %-25s%s%20s%n",
          file.getName() + (isDirectory ? File.separator : ""),
          convertTime(file.lastModified()),
          (isDirectory ? FileUtils.byteCountToDisplaySize(
              FileUtils.sizeOfDirectory(file))
              : FileUtils.byteCountToDisplaySize(file.length())));
    } catch (Exception e) {
      throw new RuntimeException(
          "При получении информации о папке или файле произошла ошибка: " + e.getMessage());
    }
  }

  private void find(List<String> args) throws Exception {
    try {
      String name = args.get(0);
      boolean recursiveFlag = args.contains("-r") || args.contains("--recursive");
      Collection<File> files = FileUtils.listFiles(new File(this.current), null, recursiveFlag);
      for (File file : files) {
        if (name.equals(file.getName())) {
          System.out.printf(" %-25s%s%20s%n", file.getName(), convertTime(file.lastModified()),
              FileUtils.byteCountToDisplaySize(file.length()));
          return;
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("При поиске файла произошла ошибка: " + e.getMessage());
    }

    System.out.println("Файл не найден");
  }

  private void blank() {
    System.out.println(
        "Невозможно выполнить пустую команду, введите help для получения списка доступных команд");
  }

  private void exit() {
    System.out.println("Завершение работы...");
  }

  private String convertTime(long time) {
    try {
      Date date = new Date(time);
      Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return format.format(date);
    } catch (Exception e) {
      throw new RuntimeException("Ошибка во время преобразования даты" + e);
    }
  }

  private boolean isAbsolutePath(String path) {
    return path.contains(this.root);
  }
}
