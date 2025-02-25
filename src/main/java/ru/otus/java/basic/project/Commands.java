package ru.otus.java.basic.project;

import java.util.HashMap;
import java.util.Map;

public enum Commands {

  LIST("ls", "ls [-i | --info] - вывести на экран список файлов текущей директории"),
  CHANGE("cd", "cd [path] - перейти в указанную директорию"),
  MAKE("mkdir", "mkdir [name] - создать новую директорию"),
  REMOVE("rm", "rm [filename] [-f | --force] – удалить указанный файл или директорию"),
  MOVE("mv", "mv [source] [destination] [-f | --force] – переименовать или перенести файл или директорию"),
  COPY("cp", "cp [source] [destination] [-f | --force] – скопировать файл или директорию"),
  INFO("finfo", "finfo [filename] – получить подробную информацию о файле"),
  FIND("find","find [filename] [-r | --recursive] - найти файл или директорию с указанным именем"),
  EXIT("exit", "exit – завершить работу"),
  HELP("help", "help – вывести в консоль список всех поддерживаемых команд");


  private final String command;
  private final String description;
  private static final Map<String, String> map = new HashMap<>();

  Commands(String command, String description) {
    this.command = command;
    this.description = description;
  }

  public String getCommand() {
    return command;
  }

  public static Map<String, String> getCommands() {
    for (Commands commands : values()) {
      map.put(commands.command, commands.description);
    }

    return map;
  }
}
