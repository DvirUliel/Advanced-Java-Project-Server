# Java Advanced Project – Subarray Analysis System

## 📦 Part A: Algorithm Library (`AlgorithmModule`)

This module provides a reusable Java library for analyzing numeric sequences (e.g., stock price changes) using the **Subarray Sum** family of algorithms.

### ✅ Features:

* Interface `ISubarrayAnalyzer` defines the strategy for subarray analysis.
* Abstract class `AbstractSubarrayAnalyzer` implements shared logic.
* Two concrete algorithms:

  * `KadaneAnalyzer`: finds subarray with **maximum sum** (profit).
  * `PrefixSumAnalyzer`: finds the **longest subarray** with a given **target sum** (e.g., 0 for zero return).
* Output model: `SubarrayResult` (start index, end index, total sum).

### 🧪 Includes:

* Unit tests (JUnit 4) verifying correctness of each algorithm under edge cases and realistic input.

### 📁 Folder structure:

```
src/
├── AlgorithmModule/
│   ├── AbstractSubarrayAnalyzer.java
│   ├── ISubarrayAnalyzer.java
│   ├── KadaneAnalyzer.java
│   ├── PrefixSumAnalyzer.java
│   └── SubarrayResult.java
└── test/
    └── ISubarrayAnalyzerTest.java
```

---

## 💼 Part B: Client Application (`algoClient`)

This module builds on the algorithm library to implement a command-line system that analyzes stock trading performance.

### 🧠 Functionality:

Given a list of stock prices or daily changes, the system allows:

* 🟢 Max profit period (using Kadane’s algorithm)
* 🔴 Max loss period (flipping sign and applying Kadane)
* ⚖️ All zero-return periods (Prefix Sum with target = 0)

### 🧩 Architecture:

* Strategy pattern: algorithm injected into the `SubarrayService`
* DAO layer: saves analysis requests/results to `datasource.txt`
* Models:

  * `AnalysisType` (enum)
  * `SubarrayRequest`: wraps input, analysis type, and UUID
  * `SubarrayResult`: output structure (from Part A)

### 🧪 Tests:

* Unit tests for `SubarrayService`
* Validates real scenarios: profit, loss, and zero return with daily/closing input

### 🖥 Usage:

```
Choose action:
[1] Run analysis
[2] View saved results
[3] Clear saved results

Enter stock data type: [1] Daily changes  [2] Closing prices
Enter values: 100, 102, 101, 104
Choose analysis type: [1] Max profit / [2] Max loss / [3] Zero return
```

### 📁 Folder structure:

```
src/
└── algoClient/
    ├── model/
    │   ├── analysis/
    │   │   ├── AnalysisRequest.java
    │   │   └── AnalysisType.java
    │   └── subarray/
    │       └── SubarrayRequest.java
    ├── repository/
    │   ├── ISubarrayDao.java
    │   └── SubarrayFileDaoImpl.java
    ├── service/
    │   └── SubarrayService.java
    ├── test/
    │   └── SubarrayServiceTest.java
    └── Main.java
```

### 🗃 Output:

All results saved to `datasource.txt`:

```
Request ID: ...
Analysis Type: MAX_PROFIT
Input: [...]
Result: StartIndex: ..., EndIndex: ..., Total: ...
```

---

## ✅ Build & Run

* Java 17+
* JUnit 4
* Run `Main.java` from Part B
* Part A is compiled into `.jar` and imported by Part B

---

© Developed by Dvir Uliel and Nicole Davidov as part of Java Advanced Course – Final Project
