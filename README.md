[![Java](https://img.shields.io/badge/java-%2019-brown.svg)](https://www.java.com/de/download/faq/java19.xml)
[![JUnit](https://img.shields.io/badge/JUnit-%204-orange.svg)](https://junit.org/junit5/)

#  SOFTWARE ENGINEERING ASSIGNMENT (ITB63-AS)

## Java Webstore backend application.
-------------------------------------------------------

:wip:

---

## Credits

### Creator

**Davain Pablo Edwards**


### Environment requirements

- [Java](https://www.java.com/de/download/)
- [Visual Studio Code](https://code.visualstudio.com/download)

---
### Project Tree

```
java-webstore-backend
├─ .vscode
│  └─ settings.json
├─ README.md
└─ rest-api
   ├─ .gradle
   │  ├─ 8.5
   │  │  ├─ checksums
   │  │  │  ├─ checksums.lock
   │  │  │  ├─ md5-checksums.bin
   │  │  │  └─ sha1-checksums.bin
   │  │  ├─ dependencies-accessors
   │  │  │  ├─ dependencies-accessors.lock
   │  │  │  └─ gc.properties
   │  │  ├─ executionHistory
   │  │  │  ├─ executionHistory.bin
   │  │  │  └─ executionHistory.lock
   │  │  ├─ fileChanges
   │  │  │  └─ last-build.bin
   │  │  ├─ fileHashes
   │  │  │  ├─ fileHashes.bin
   │  │  │  ├─ fileHashes.lock
   │  │  │  └─ resourceHashesCache.bin
   │  │  ├─ gc.properties
   │  │  └─ vcsMetadata
   │  ├─ buildOutputCleanup
   │  │  ├─ buildOutputCleanup.lock
   │  │  ├─ cache.properties
   │  │  └─ outputFiles.bin
   │  ├─ file-system.probe
   │  └─ vcs-1
   │     └─ gc.properties
   ├─ .vscode
   │  └─ settings.json
   ├─ build
   │  ├─ classes
   │  │  └─ java
   │  │     ├─ main
   │  │     │  └─ de
   │  │     │     └─ webstore
   │  │     │        └─ backend
   │  │     │           ├─ Application.class
   │  │     │           ├─ exception
   │  │     │           │  ├─ BusinessException.class
   │  │     │           │  ├─ GlobalExceptionHandler.class
   │  │     │           │  └─ NotFoundException.class
   │  │     │           └─ model
   │  │     │              ├─ Auftrag.class
   │  │     │              ├─ Lager.class
   │  │     │              ├─ Position.class
   │  │     │              └─ Produkt.class
   │  │     └─ test
   │  ├─ generated
   │  │  └─ sources
   │  │     ├─ annotationProcessor
   │  │     │  └─ java
   │  │     │     └─ main
   │  │     └─ headers
   │  │        └─ java
   │  │           └─ main
   │  ├─ resources
   │  │  ├─ main
   │  │  │  └─ application.properties
   │  │  └─ test
   │  └─ tmp
   │     ├─ compileJava
   │     │  ├─ compileTransaction
   │     │  │  ├─ backup-dir
   │     │  │  └─ stash-dir
   │     │  │     └─ Application.class.uniqueId0
   │     │  └─ previous-compilation-data.bin
   │     └─ compileTestJava
   ├─ build.gradle
   ├─ gradle
   │  └─ wrapper
   │     ├─ gradle-wrapper.jar
   │     └─ gradle-wrapper.properties
   ├─ gradlew
   ├─ gradlew.bat
   ├─ HELP.md
   ├─ README.md
   ├─ settings.gradle
   └─ src
      ├─ main
      │  ├─ java
      │  │  └─ de
      │  │     └─ webstore
      │  │        └─ backend
      │  │           ├─ Application.java
      │  │           ├─ config
      │  │           ├─ controller
      │  │           ├─ exception
      │  │           │  ├─ BusinessException.java
      │  │           │  ├─ GlobalExceptionHandler.java
      │  │           │  └─ NotFoundException.java
      │  │           ├─ model
      │  │           └─ service
      │  └─ resources
      │     └─ application.properties
      └─ test
         └─ java
            └─ de
               └─ webstore
                  └─ backend

```

## License

GNU General Public License Version 3

Copyright (c) 2024 Davain Pablo Edwards


This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

For further information regarding the license, please see: <http://www.gnu.org/licenses/>.