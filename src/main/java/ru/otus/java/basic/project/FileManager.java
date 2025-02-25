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
import java.util.Map;
import org.apache.commons.io.FileUtils;

public class FileManager {

  private String current;
  private final String root;
  private final Map<String, String> commands;

  public FileManager() {
    Path path = Paths.get(".");
    current = String.valueOf(path.toAbsolutePath().normalize());
    root = String.valueOf(path.toAbsolutePath().normalize().getRoot());
    commands = Commands.getCommands();
  }

  public String getCurrent() {
    return current;
  }

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
          System.out.printf(" %s%n",
              "Команда не найдена, введите help для получения списка доступных команд");
        }
      }
    } catch (Exception e) {
      System.out.printf(" %s%n", e.getMessage());
    }
  }

  private void help() {
    for (String description : commands.values()) {
      System.out.printf(" %s%n", description);
    }
  }

  // FIXME: отображает только файлы
  private void list(List<String> args) {
    try {
      boolean infoFlag = args.contains("-i") || args.contains("--info");
      Collection<File> files = FileUtils.listFiles(new File(this.current), null, false);
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
      throw new RuntimeException(e.getMessage());
    }
  }

  private void changeDirectory(List<String> args) {
    try {
      if (args.isEmpty()) {
        System.out.printf(" %s%s%n", "Использование: ", commands.get("cd"));
        return;
      }

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
          e.getMessage());
    }
  }

  private void makeDirectory(List<String> args) throws Exception {
    try {
      if (args.isEmpty()) {
        System.out.printf(" %s%s%n", "Использование: ", commands.get("mkdir"));
        return;
      }

      String path = args.get(0);
      File file = new File((isAbsolutePath(path)) ? path : this.current + File.separator + path);
      if (file.exists()) {
        throw new Exception("Такая папка уже существует");
      }
      if (!file.mkdir()) {
        throw new Exception("Не удалось создать папку");
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private void remove(List<String> args) throws Exception {
    try {
      if (args.isEmpty()) {
        System.out.printf(" %s%s%n", "Использование: ", commands.get("rm"));
        return;
      }

      String path = args.get(0);
      boolean forceFlag = args.contains("-f") || args.contains("--force");
      File file = new File((isAbsolutePath(path)) ? path : this.current + File.separator + path);
      if (!file.exists()) {
        throw new Exception("не найдены папка или файл для удаления");
      }
      if (forceFlag) {
        FileUtils.forceDelete(file);
        return;
      }
      if (!file.delete()) {
        throw new Exception("Не удалось удалить папку или файл");
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
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
      if (args.isEmpty()) {
        System.out.printf(" %s%s%n", "Использование: ", commands.get("finfo"));
        return;
      }

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
      throw new RuntimeException(e.getMessage());
    }
  }

  private void find(List<String> args) throws Exception {
    try {
      if (args.isEmpty()) {
        System.out.printf(" %s%s%n", "Использование: ", commands.get("find"));
        return;
      }

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
      throw new RuntimeException(e.getMessage());
    }

    System.out.printf(" %s%n", "Файл не найден");
  }

  private void blank() {
    System.out.printf(" %s%n",
        "Невозможно выполнить пустую команду, введите help для получения списка доступных команд");
  }

  private void exit() {
    System.out.printf(" %s%n","Завершение работы...");
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
