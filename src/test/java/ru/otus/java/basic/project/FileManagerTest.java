package ru.otus.java.basic.project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileManagerTest {

  FileManager fileManager = new FileManager();

  @Test
  void testHelp() {
    Assertions.assertDoesNotThrow(() -> fileManager.help());
    Assertions.assertDoesNotThrow(() -> fileManager.execute(new String[]{"help"}));
  }
}
