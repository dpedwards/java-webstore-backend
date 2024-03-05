[![Java](https://img.shields.io/badge/java-%2017-brown.svg)](https://www.java.com/de/download/faq/java17.xml)
[![JUnit](https://img.shields.io/badge/JUnit-%204-orange.svg)](https://junit.org/junit5/)

# SOFTWARE ENGINEERING ASSIGNMENT (ITB63-AS)

## Java Webstore Backend Application

This repository contains the source code for a Java webstore backend application, designed as part of the ITB63-AS software engineering assignment. The application provides a suite of RESTful services to manage products, warehouses, and orders for an online store.

### Credits

#### Creator

- **Davain Pablo Edwards**

### Environment Requirements

To run this application, you will need:

- [Java 17](https://www.java.com/de/download/)
- [Visual Studio Code](https://code.visualstudio.com/download) (Recommended IDE)

### Getting Started

To get started with the Java Webstore Backend Application:

1. **Clone the repository:**
   ```
   git clone <repository-url>
   ```

2. Navigate to the project directory:
   ```
   cd java-webstore-backend/rest-api
   ```

3. Build the project (optional):
   ```
   ./gradlew build  # For Unix/Linux/Mac
   gradlew.bat build  # For Windows
   ```
4. Run the application (optional):
   ```
   ./gradlew bootRun  # For Unix/Linux/Mac
   gradlew.bat bootRun  # For Windows
   ```
5. Run the REST-API with Visual Studio Code [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) and [Gradle for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-gradle) Plugins
   and execute `java-webstore-backend\rest-api\src\main\java\de\webstore\backend\Application.java` 

6. Access Swagger docs:

   http://localhost:8080/swagger-ui/index.html

Project Structure
The project is structured as follows:

```
java-webstore-backend
├─ .git
├─ .gitignore
├─ .vscode
│  └─ settings.json
├─ assets
│  ├─ warehouse_database.db
│  ├─ warehouse_database_de.sql
│  ├─ warehouse_database_en.sql
│  ├─ warehouse_database_test.sql
│  └─ warehouse_database_uuid_de.sql
├─ README.md
└─ rest-api
   ├─ .gitignore
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
   │  │     │           ├─ config
   │  │     │           │  └─ DatabaseConnection.class
   │  │     │           ├─ controller
   │  │     │           │  ├─ OrderController.class
   │  │     │           │  ├─ ProductController.class
   │  │     │           │  └─ WarehouseController.class
   │  │     │           ├─ dto
   │  │     │           │  ├─ OrderDTO.class
   │  │     │           │  ├─ PositionDTO.class
   │  │     │           │  ├─ ProductDTO.class
   │  │     │           │  ├─ ProductUpdateDTO.class
   │  │     │           │  └─ WarehouseDTO.class
   │  │     │           ├─ exception
   │  │     │           │  ├─ OrderClosedException.class
   │  │     │           │  ├─ OrderNotFoundException.class
   │  │     │           │  └─ ProductNotFoundException.class
   │  │     │           └─ service
   │  │     │              ├─ OrderService.class
   │  │     │              ├─ ProductService.class
   │  │     │              └─ WarehouseService.class
   │  │     └─ test
   │  │        └─ de
   │  │           └─ webstore
   │  │              └─ backend
   │  │                 └─ WarehouseServiceTest.class
   │  ├─ generated
   │  │  └─ sources
   │  │     ├─ annotationProcessor
   │  │     │  └─ java
   │  │     │     ├─ main
   │  │     │     └─ test
   │  │     └─ headers
   │  │        └─ java
   │  │           ├─ main
   │  │           └─ test
   │  ├─ resources
   │  │  └─ main
   │  │     └─ application.properties
   │  └─ tmp
   │     ├─ compileJava
   │     │  ├─ compileTransaction
   │     │  │  ├─ backup-dir
   │     │  │  └─ stash-dir
   │     │  │     ├─ WarehouseController.class.uniqueId0
   │     │  │     └─ WarehouseService.class.uniqueId1
   │     │  └─ previous-compilation-data.bin
   │     └─ compileTestJava
   │        ├─ compileTransaction
   │        │  ├─ backup-dir
   │        │  └─ stash-dir
   │        │     └─ WarehouseServiceTest.class.uniqueId0
   │        └─ previous-compilation-data.bin
   ├─ build.gradle
   ├─ gradle
   │  └─ wrapper
   │     ├─ gradle-wrapper.jar
   │     └─ gradle-wrapper.properties
   ├─ gradlew
   ├─ gradlew.bat
   ├─ HELP.md
   ├─ settings.gradle
   └─ src
      ├─ main
      │  ├─ java
      │  │  └─ de
      │  │     └─ webstore
      │  │        └─ backend
      │  │           ├─ Application.java
      │  │           ├─ config
      │  │           │  └─ DatabaseConnection.java
      │  │           ├─ controller
      │  │           │  ├─ OrderController.java
      │  │           │  ├─ ProductController.java
      │  │           │  └─ WarehouseController.java
      │  │           ├─ dto
      │  │           │  ├─ OrderDTO.java
      │  │           │  ├─ PositionDTO.java
      │  │           │  ├─ ProductDTO.java
      │  │           │  ├─ ProductUpdateDTO.java
      │  │           │  └─ WarehouseDTO.java
      │  │           ├─ exception
      │  │           │  ├─ OrderClosedException.java
      │  │           │  ├─ OrderNotFoundException.java
      │  │           │  └─ ProductNotFoundException.java
      │  │           └─ service
      │  │              ├─ OrderService.java
      │  │              ├─ ProductService.java
      │  │              └─ WarehouseService.java
      │  └─ resources
      └─ test
         └─ java
            └─ de
               └─ webstore
                  └─ backend
                     └─ WarehouseServiceTest.java

```


License
This project is licensed under the GNU General Public License Version 3.

Copyright (c) 2024 Davain Pablo Edwards

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.