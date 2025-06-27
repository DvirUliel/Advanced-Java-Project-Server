# Profit Analyzer - Advanced Java Development Project

A comprehensive client-server application for financial subarray analysis using advanced algorithms and enterprise design patterns.

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

### Setup (3 Steps)
```bash
1. Clone repository: git clone <your-repo-url>
2. Open project in IntelliJ IDEA (File → Open)
3. JARs are already included! Verify setup: Right-click test/ → Run All Tests
```

**Expected: 29/29 tests passed** ✅

**Note**: All dependencies (AlgorithmModule.jar, gson-2.8.6.jar) are included in the `lib/` folder - no manual downloads needed!

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

### 1. Strategy Pattern
```java
// Algorithm selection based on analysis type
ISubarrayAnalyzer analyzer = (type == ZERO_RETURN)
                ? new PrefixSumAnalyzer(0)
                : new KadaneAnalyzer();
```

### 2. Factory Pattern
```java
// Centralized dependency injection
AnalysisController controller = FactoryController.getAnalysisController();
```

### 3. Repository Pattern
```java
// Abstract data access
public interface IAnalysisDao {
    void save(AnalysisRequest request, SubarrayResult result);
    List<String> loadAll();
}
```

### 4. MVC Pattern
- **Model**: AnalysisRequest, SubarrayResult
- **View**: JSON Request/Response
- **Controller**: AnalysisController

## 📊 Performance & Complexity

- **Kadane Algorithm**: O(n) time, O(1) space
- **PrefixSum Algorithm**: O(n) time, O(n) space
- **Multi-threading**: Thread-per-client model
- **Concurrency**: Thread-safe design

## 🎯 Project Requirements Fulfilled

### Part 1 - Algorithm Module ✅
- ✅ **Interface design** (ISubarrayAnalyzer) with Strategy pattern
- ✅ **Multiple implementations** (KadaneAnalyzer, PrefixSumAnalyzer)
- ✅ **Abstract class** (AbstractSubarrayAnalyzer) for common functionality
- ✅ **Algorithm packaging** as reusable JAR module
- ✅ **Unit testing** with JUnit framework
- ✅ **Package structure** following lecturer's specifications

### Part 2 - OOP & Design Patterns ✅
- ✅ **Service layer** (AnalysisService) with business logic
- ✅ **Repository pattern** (IAnalysisDao → AnalysisDaoImpl)
- ✅ **Domain models** (AnalysisRequest) with rich data structures
- ✅ **File-based persistence** with structured data storage
- ✅ **Dependency injection** through constructor parameters
- ✅ **Open/Closed principle** - extensible without modification
- ✅ **Algorithm integration** via Strategy pattern from Part 1

### Part 3 - Communication & Controllers ✅
- ✅ **Multi-threaded TCP Server** with concurrent client handling
- ✅ **JSON communication** (Request/Response classes with Gson)
- ✅ **Controller layer** (AnalysisController) for API endpoints
- ✅ **Factory pattern** (FactoryController) for dependency injection
- ✅ **Request routing** to appropriate business logic
- ✅ **Error handling** with standardized responses
- ✅ **HandleRequest** class for client connection management
- ✅ **Integration testing** with full request-response cycle

## 👥 Project Info

- **Course**: Advanced Java Development
- **Institution**: HIT - Holon Institute of Technology
- **Instructor**: Nissim Barami
- **Semester**: Spring 2025

---

**🎯 Ready for submission with 29/29 tests passing!** ✅