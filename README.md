# Проектная работа курса [OTUS Java Developer. Basic](https://otus.ru/lessons/java-basic/)

## Консольный файловый менеджер

---
[![Actions Status](https://github.com/alexey-sidorov-dev/console-filemanager/workflows/Build/badge.svg)](https://github.com/alexey-sidorov-dev/console-filemanager/actions)
[![Actions Status](https://github.com/alexey-sidorov-dev/console-filemanager/workflows/Check/badge.svg)](https://github.com/alexey-sidorov-dev/console-filemanager/actions)
![Language](https://img.shields.io/github/languages/top/alexey-sidorov-dev/console-filemanager)
[![License](https://img.shields.io/github/license/alexey-sidorov-dev/console-filemanager)](https://github.com/alexey-sidorov-dev/console-filemanager/blob/master/LICENSE)

### Цель

Применить полученные на курсе знания.

### Задания

Реализовать консольный файловый менеджер, который имеет следующие возможности:

- ls – распечатать список файлов текущего каталога. Если добавлен ключ -i, то должна быть более
  подробная информация о файлах: имя – размер – дата последнего изменения;
- cd [path] – переход в указанную поддиректорию, cd .. – переход в родительский каталог;
- mkdir [name] – создание новой директории с указанным именем;
- rm [filename] – удаление указанного файла или директории (*возможность удаления не пустого
  каталога);
- mv [source] [destination] – переименовать/перенести файл или директорию;
- cp [source] [destination] – скопировать файл;
-
    * для mv, cp выдавать предупреждение, что указанный файл в точке назначения уже существует.
      Добавить ключ -f, чтобы принудительно переписывать файл в точке назначения;
- finfo [filename] – получить подробную информацию о файле;
- help – вывод в консоль всех поддерживаемых команд;
-
    * find [filename] – найти файл с указанным именем в текущем каталоге или любом его подкаталоге;
- exit – завершить работу.
