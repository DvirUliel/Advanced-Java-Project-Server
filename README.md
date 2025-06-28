# Profit Analyzer - Advanced Java Project Server

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![TCP Server](https://img.shields.io/badge/TCP-Server-blue.svg)](https://docs.oracle.com/javase/tutorial/networking/sockets/)
[![Architecture](https://img.shields.io/badge/Architecture-3--Tier-green.svg)](https://en.wikipedia.org/wiki/Multitier_architecture)
[![Protocol](https://img.shields.io/badge/Protocol-TCP%2FJSON-lightblue.svg)](https://www.json.org/)
[![Testing](https://img.shields.io/badge/Tests-29%2F29-brightgreen.svg)](https://junit.org/junit4/)
[![Design Patterns](https://img.shields.io/badge/Patterns-Strategy%2FMVC%2FFactory%2FDecorator%2FCommand%2FRepository-purple.svg)](https://refactoring.guru/design-patterns/catalog)

A comprehensive client-server application for financial subarray analysis using advanced algorithms and enterprise design patterns.

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Quick Start](#quick-start)
- [API Usage](#api-usage)
- [Testing](#testing)
- [Design Patterns](#design-patterns)
- [Performance](#performance--complexity)
- [Project Requirements](#project-requirements-fulfilled)

## 🎯 Project Overview

**TCP-based client-server application** that performs financial analysis using subarray algorithms:

- **Maximum Profit Periods** - Best buy/sell timeframes (Kadane Algorithm)
- **Maximum Loss Periods** - Risk analysis (Kadane with flipped values)
- **Zero Return Periods** - Break-even analysis (PrefixSum Algorithm)

### Key Features
- ✅ **Multi-threaded TCP Server** - Concurrent client handling
- ✅ **Algorithm Module JAR** - Reusable with Strategy pattern
- ✅ **JSON API** - RESTful-style communication
- ✅ **Enterprise Architecture** - Clean layered design
- ✅ **Comprehensive Testing** - 29 tests, 100% pass rate
- ✅ **7 Design Patterns** - Professional software architecture

## 🏗️ Architecture

### **Project Structure**
```
src/
├── main/
│   ├── java/
│   │   ├── controller/     # API Controllers & Factory
│   │   ├── enums/          # AnalysisType, DataMode
│   │   ├── model/          # Domain Models
│   │   ├── network/        # TCP Server & Communication
│   │   ├── repository/     # Data Access Layer
│   │   ├── service/        # Business Logic
│   │   └── utils/          # Helper Classes
│   ├── test/               # Test Classes (Course Requirement)
│   │   ├── controller/     # Controller Tests
│   │   ├── integration/    # End-to-End Tests
│   │   ├── network/        # Communication Tests
│   │   └── service/        # Service Layer Tests
│   └── resources/
│       └── datasource.txt  # Data Storage File
└── lib/
    ├── AlgorithmModule.jar # Custom Algorithm Library
    └── gson-2.8.6.jar     # JSON Processing
```

### **3-Tier Enterprise Architecture**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Client Layer  │    │   Server Layer  │    │   Data Layer    │
│                 │    │                 │    │                 │
│ JSON Requests   │───▶│ TCP Server      │───▶│ File Storage    │
│ JSON Responses  │◀───│ Multi-threaded  │◀───│ Repository      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### **Technology Stack**
- **Core Language**: Java 17+ with advanced features
- **Networking**: TCP Sockets with multi-threading
- **Serialization**: Gson for JSON processing
- **Testing**: JUnit 4 framework
- **Architecture**: Layered design with design patterns
- **Algorithms**: Custom implementations (O(n) complexity)

### **Data Flow**
```
TCP Request → HandleRequest → AnalysisController → AnalysisService → Algorithm + Repository → Response
```

### **Concurrency Model**
- **Server Thread**: Accepts incoming connections
- **Client Threads**: One thread per client request
- **Thread Safety**: Stateless services with immutable data
- **Resource Management**: Auto-cleanup with try-with-resources

## 🚀 Quick Start

### Prerequisites
- **Java 17+**
- **IntelliJ IDEA**
- **Ports 34567 & 34568** available

### Setup (4 Steps)
```bash
1. Clone repository: git clone https://github.com/DvirUliel/Advanced-Java-Project-Server.git
2. Open project in IntelliJ IDEA (File → Open)
3. Add JAR dependencies: F4 → Dependencies → + → JARs → Select lib/*.jar files
4. Verify setup: Right-click src/main/test/ → Run All Tests
```

**Expected: 29/29 tests passed** ✅

**JAR Setup Note**: If IntelliJ doesn't automatically recognize the JARs in `lib/` folder, manually add them:
- **Go to**: File → Project Structure → Modules → Dependencies
- **Add**: lib/AlgorithmModule.jar and lib/gson-2.10.1.jar
- **Scope**: Compile

### Verify Installation
```bash
# Expected output:
✅ Server started successfully!
📡 Listening on port: 34567
🔄 Ready to accept client connections...
```

## 📡 API Usage

### Request Format
```json
{"headers":{"action":"analysis/maxProfit"},"body":{"values":[1.0,-2.0,3.0],"dataMode":"DAILY_CHANGES"}}
```

### Available Actions
| Action | Algorithm | Purpose |
|--------|-----------|---------|
| `analysis/maxProfit` | Kadane | Find maximum profit period |
| `analysis/maxLoss` | Kadane (flipped) | Find maximum loss period |
| `analysis/zeroReturn` | PrefixSum | Find zero-sum periods |
| `analysis/clear` | N/A | Clear all results |
| `analysis/getAll` | N/A | Get all results |

### Action-Based Routing
Your server uses **action-based routing** through JSON headers, not HTTP methods. All communication happens over **TCP sockets** with JSON messages.

### Response Format
```json
{"status":"SUCCESS","message":"Operation completed successfully","data":{"startIndex":2,"endIndex":4,"total":2.5}}
```

### Manual Testing
```bash
# Using Telnet (Recommended)
telnet localhost 34567
# Then paste single-line JSON requests with action headers

# Using Netcat
echo '{"headers":{"action":"analysis/maxProfit"},"body":{"values":[1.0,-2.0,3.0],"dataMode":"DAILY_CHANGES"}}' | nc localhost 34567

# Using Java Socket Client
Socket socket = new Socket("localhost", 34567);
PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
writer.println(jsonRequest); // Send JSON with action header
```

## 🧪 Testing

### **Test Suite Overview**
```
📁 src/main/test/
├── 📁 controller/      AnalysisControllerTest.java      (7 tests) ✅
│                      FactoryControllerTest.java       (5 tests) ✅
├── 📁 integration/     ServerIntegrationTest.java       (5 tests) ✅
├── 📁 network/         RequestResponseTest.java         (6 tests) ✅
└── 📁 service/         AnalysisServiceTest.java         (6 tests) ✅

🎯 Total: 29 tests covering all layers and functionality
```

### **Quick Test Execution**
```bash
# Run complete test suite
Right-click src/main/test/ folder → Run 'All Tests'

# Expected result
✅ 29/29 tests passed (100% success rate)

# Run by category
- Controller: Right-click src/main/test/controller/ → Run All Tests
- Service: Right-click src/main/test/service/ → Run All Tests  
- Network: Right-click src/main/test/network/ → Run All Tests
- Integration: Right-click src/main/test/integration/ → Run All Tests
```

### **What Tests Validate**
- **Algorithm Integration** - Correct algorithm selection and execution
- **Business Logic** - Service layer functionality and data processing
- **API Routing** - Controller request handling and response formatting
- **JSON Processing** - Request/response serialization and deserialization
- **Network Communication** - Full TCP client-server interaction
- **Error Handling** - Invalid requests and exception management
- **Dependency Injection** - Factory pattern and object creation

## 🎨 Design Patterns

Based on [Refactoring.Guru Design Patterns Catalog](https://refactoring.guru/design-patterns/catalog)

### 🏭 Creational Patterns

#### 1. Factory Pattern
```java
/**
 * Centralized dependency injection and object creation
 */
public class FactoryController {
    private static final Map<String, Object> controllers = new HashMap<>();
    
    static {
        // Pre-configured dependency injection
        AnalysisDaoImpl dao = new AnalysisDaoImpl("src/main/resources/datasource.txt");
        KadaneAnalyzer kadaneAnalyzer = new KadaneAnalyzer();
        AnalysisService analysisService = new AnalysisService(kadaneAnalyzer, dao);
        
        controllers.put("analysis", new AnalysisController(analysisService));
    }
    
    public static AnalysisController getAnalysisController() {
        return (AnalysisController) controllers.get("analysis");
    }
}
```

### 🏗️ Structural Patterns

#### 2. Decorator Pattern (Stream Processing)
```java
/**
 * Enhanced stream capabilities through decoration chain
 * As required by course document page 18
 */
// Input Stream Decoration: InputStream → InputStreamReader → Scanner
Scanner reader = new Scanner(new InputStreamReader(socket.getInputStream()));

// Output Stream Decoration: OutputStream → OutputStreamWriter → PrintWriter  
PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
```
**Stream Decoration Benefits:**
- `InputStreamReader`: Adds character encoding conversion
- `Scanner`: Adds structured text parsing capabilities
- `OutputStreamWriter`: Adds character encoding for output
- `PrintWriter`: Adds formatted printing methods

#### 3. Repository Pattern
```java
/**
 * Abstract data access layer for flexible storage implementations
 */
public interface IAnalysisDao {
    void save(AnalysisRequest request, SubarrayResult result);
    List<String> loadAll();
    void clearAll();
}

// File-based implementation with decorator streams
public class AnalysisDaoImpl implements IAnalysisDao {
    private final String filePath;
    
    // Uses decorator pattern for file I/O
    public void save(AnalysisRequest request, SubarrayResult result) {
        try (FileWriter writer = new FileWriter(filePath, true);
             PrintWriter printWriter = new PrintWriter(writer)) {
            // Decorated writing operations
        }
    }
}
```

### ⚡ Behavioral Patterns

#### 4. Strategy Pattern
```java
/**
 * Algorithm family with interchangeable implementations
 */
// Context selects strategy based on analysis type
ISubarrayAnalyzer analyzer = (analysisType == ZERO_RETURN) 
    ? new PrefixSumAnalyzer(0)     // O(n) time, O(n) space
    : new KadaneAnalyzer();        // O(n) time, O(1) space

// Client uses strategy without knowing implementation details
SubarrayResult result = analyzer.findOptimalSubarray(values);
```

#### 4. Command Pattern (Request Handling)
```java
/**
 * Request encapsulates action and parameters as commands
 */
public class Request<T> {
    private RequestHeaders headers;  // Contains action command
    private T body;                  // Command parameters
}

// HandleRequest acts as invoker, Controller as receiver
public class HandleRequest implements Runnable {
    public void run() {
        String action = request.getHeaders().getAction();
        AnalysisController controller = FactoryController.getAnalysisController();
        
        // Execute command through controller
        Response<?> response = controller.handle(action, bodyJson);
    }
}
```

### 🏛️ Architectural Patterns

#### 5. MVC Pattern
```java
/**
 * Model-View-Controller separation of concerns
 */
// Model - Data representation
public class AnalysisRequest { /* Data model */ }
public class SubarrayResult { /* Result model */ }

// View - JSON representation for client communication
{"headers":{"action":"analysis/maxProfit"},"body":{"values":[...]}}

// Controller - Business logic coordination
public class AnalysisController {
    public Response<?> handle(String action, String bodyJson) {
        // Coordinates between model and service layers
    }
}
```

#### 6. Layered Architecture Pattern
```
┌─────────────────┐
│  Controller     │ ← API Layer (AnalysisController)
├─────────────────┤
│  Service        │ ← Business Logic (AnalysisService)  
├─────────────────┤
│  Repository     │ ← Data Access (AnalysisDaoImpl)
├─────────────────┤
│  Network        │ ← Communication (Server, HandleRequest)
└─────────────────┘
```

### 🔗 Pattern Interactions

**Factory + Strategy**: Factory creates pre-configured strategy implementations
```java
// Factory provides strategy-configured services
AnalysisService service = new AnalysisService(kadaneAnalyzer, dao);
```

**Repository + Decorator**: File I/O uses decorated streams for enhanced functionality
```java
// Repository uses decorator pattern for efficient I/O
PrintWriter writer = new PrintWriter(new FileWriter(file));
```

**MVC + Command**: Controllers handle command-pattern requests through action routing
```java
// Controller receives commands and delegates to appropriate handlers
Response<?> response = controller.handle(action, bodyJson);
```

### 📚 Pattern Benefits in Your Server

- **🔧 Extensibility**: Easy to add new algorithms via Strategy pattern
- **🛠️ Maintainability**: Clear separation of concerns with MVC and Repository
- **⚡ Performance**: Decorator pattern optimizes I/O operations
- **🧪 Testability**: Each pattern enables focused unit testing (29/29 tests passing)
- **📈 Scalability**: Layered architecture supports concurrent client handling
- **🎯 Flexibility**: Command pattern allows dynamic request routing

## 📊 Performance & Complexity

- **Kadane Algorithm**: O(n) time, O(1) space
- **PrefixSum Algorithm**: O(n) time, O(n) space
- **Multi-threading**: Thread-per-client model
- **Concurrency**: Thread-safe design
- **I/O Operations**: Optimized with Decorator pattern streams

## 🎯 Project Requirements Fulfilled

### Part 1 - Algorithm Module ✅
- ✅ **Interface design** (ISubarrayAnalyzer) with Strategy pattern
- ✅ **Multiple implementations** (KadaneAnalyzer, PrefixSumAnalyzer)
- ✅ **Abstract class** (AbstractSubarrayAnalyzer) with Template Method pattern
- ✅ **Algorithm packaging** as reusable JAR module
- ✅ **Unit testing** with JUnit framework
- ✅ **Package structure** following lecturer's specifications

### Part 2 - OOP & Design Patterns ✅
- ✅ **Service layer** (AnalysisService) with business logic
- ✅ **Repository pattern** (IAnalysisDao → AnalysisDaoImpl)
- ✅ **Domain models** (AnalysisRequest) with rich data structures
- ✅ **File-based persistence** with structured data storage
- ✅ **Dependency injection** through Factory pattern
- ✅ **Open/Closed principle** - extensible without modification
- ✅ **Algorithm integration** via Strategy pattern from Part 1

### Part 3 - Communication & Controllers ✅
- ✅ **Multi-threaded TCP Server** with concurrent client handling
- ✅ **JSON communication** (Request/Response classes with Gson)
- ✅ **Controller layer** (AnalysisController) for API endpoints
- ✅ **Factory pattern** (FactoryController) for dependency injection
- ✅ **Request routing** via Command pattern with action-based routing
- ✅ **Error handling** with standardized responses
- ✅ **HandleRequest** class for client connection management
- ✅ **Decorator pattern** for stream processing (course requirement)
- ✅ **Integration testing** with full request-response cycle

### Advanced Features ✅
- ✅ **7 Design Patterns** implemented professionally
- ✅ **Enterprise Architecture** with clear layer separation
- ✅ **Comprehensive Testing** (29/29 tests passing)
- ✅ **Performance Optimization** through appropriate algorithm selection
- ✅ **Thread Safety** and concurrent client handling
- ✅ **Extensible Design** following SOLID principles

## 👥 Project Info

- **Course**: Advanced Java Development
- **Institution**: HIT - Holon Institute of Technology
- **Instructor**: Nissim Barami
- **Semester**: Spring 2025

---

**🏆 Ready for submission with 7 Design Patterns, 29/29 tests passing, and enterprise-grade architecture!** ✅
