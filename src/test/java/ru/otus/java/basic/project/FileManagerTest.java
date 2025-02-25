package ru.otus.java.basic.project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FileManagerTest {

  FileManager fileManager = new FileManager();

  @ParameterizedTest
  @ValueSource(strings = {"ls", "ls -i", "ls -p", "help", "help -p", "cd", "mkdir", "rm", "mv",
      "cp", "", "exit"})
  void testProcess(String input) {
    Assertions.assertDoesNotThrow(() -> fileManager.process(input));
  }
}
