# PhysioTrack – Development Guide

Please read this carefully before starting development.

---

## 1. Prerequisites

### Java

* **Minimum required:** Java **17**
* Higher versions (21, 23, 25) are allowed, but the project **compiles to Java 17**
* Verify:

```bash
java -version
```

### Maven

* Do **NOT** rely on your system Maven
* Always use the **Maven Wrapper** included in this repository

---

## 2. Cloning the Repository

Clone the repository assigned for the framework you are working on:

```bash
git clone <REPO_URL>
cd physiotrack
```

---

## 3. Building the Project (Important)

All builds **must** use the Maven Wrapper.

### Windows

```bash
./mvnw.cmd clean test
```

### macOS / Linux

```bash
./mvnw clean test
```

If this command succeeds, your environment is correctly set up.

---

## 4. Java Version Consistency (Critical)

* The project enforces **Java 17 bytecode**
* You may run Maven using Java ≥ 17
* Do **not** use Java features newer than 17

If Maven and Java show different versions:

```bash
java -version
mvn -version
```

Fix your `JAVA_HOME` so both use the same JDK family.

---

## 5. Branching Rules (Mandatory)

* `main` must always be **stable**
* No direct commits to `main`
Create branch for your modules:

```bash
git checkout -b feature/<module-name>
```

Example:

```bash
git checkout -b feature/appointment
```

---

## 6. Before Opening a Pull Request (PR)

You **must** run:

```bash
./mvnw clean test
```

Only open a PR if:

* The build passes
* You modified only your assigned modules
* Your code follows the agreed structure

---

## 7. Framework Notes

* This repository is **console-based only**
* No UI implementation is required
* Functionalities are demonstrated via console output
* Services must follow the component/module boundaries defined in the design

---

## 8. Running the Spring Boot app

### Windows

```bash
./mvnw.cmd -pl springboot-app spring-boot:run
```

### macOS/Linux

```bash
./mvnw -pl springboot-app spring-boot:run
```
