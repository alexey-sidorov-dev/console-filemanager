package ru.otus.java.basic.project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FileManagerTest {

  FileManager fileManager = new FileManager();

  @ParameterizedTest
  @ValueSource(strings = {"ls", "ls -i", "ls -p", "help", "help -p", "", "exit"})
  void testProcess1(String input) {
    Assertions.assertDoesNotThrow(() -> fileManager.process(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"cd", "mkdir", "rm", "mv", "cp"})
  void testProcess2(String input) {
    Assertions.assertDoesNotThrow( () -> fileManager.process(input));
  }
}
