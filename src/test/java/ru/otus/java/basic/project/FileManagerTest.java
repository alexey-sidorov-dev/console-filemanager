package ru.otus.java.basic.project;

import org.junit.jupiter.api.Test;

class FileManagerTest {

  FileManager fileManager = new FileManager();

  @Test
  void testHelp() {
    fileManager.help();
  }
}

