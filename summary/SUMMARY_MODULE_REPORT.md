# Summary Module - Technical Report

## 5.1.5 Summary Module

The Summary Module provides functionality to generate and retrieve patient summary reports for the PhysioTrack system. It aggregates patient data by month and year, allowing both patients and physiotherapists to view summarized information about progress and achievements.

---

## 5.1.5.1 Service API Layer

The Service API Layer defines the contract for summary-related operations through the `SummaryService` interface.

**Package:** `com.physiotrack.summary.api`

**Interface:** `SummaryService`

```java
public interface SummaryService {
    SummaryReport getMonthlySummary(Long requestingUserId, Long patientId, int month, int year);
    List<SummaryReport> getRecentSummaries(Long requestingUserId, Long patientId);
}
```

### API Methods

1. **`getMonthlySummary(Long requestingUserId, Long patientId, int month, int year)`**
   - **Purpose:** Retrieves a specific summary report for a patient for a given month and year
   - **Parameters:**
     - `requestingUserId`: ID of the user requesting the summary (for authorization)
     - `patientId`: ID of the patient whose summary is being requested
     - `month`: Month number (1-12)
     - `year`: Year number
   - **Returns:** `SummaryReport` object containing the monthly summary data
   - **Authorization:** Accessible by the patient themselves or any physiotherapist

2. **`getRecentSummaries(Long requestingUserId, Long patientId)`**
   - **Purpose:** Retrieves a list of recent summary reports for a patient, ordered by date descending
   - **Parameters:**
     - `requestingUserId`: ID of the user requesting the summaries (for authorization)
     - `patientId`: ID of the patient whose summaries are being requested
   - **Returns:** List of `SummaryReport` objects sorted by year and month in descending order
   - **Authorization:** Accessible by the patient themselves or any physiotherapist

---

## 5.1.5.2 Service Implementation Layer

The Service Implementation Layer provides the concrete implementation of the `SummaryService` interface through the `SummaryServiceImpl` class.

**Package:** `com.physiotrack.summary.service`

**Class:** `SummaryServiceImpl`

**Annotations:** `@Service`

### Dependencies

The service implementation has the following dependencies injected via constructor:

- `SummaryRepository`: For accessing summary data from the database
- `UserManagementService`: For user authentication and role verification

### Implemented Methods

#### 1. `getMonthlySummary(Long requestingUserId, Long patientId, int month, int year)`

**Description:** This method retrieves a specific monthly summary report for a patient while enforcing access control.

**Implementation Logic:**
1. Retrieves the requesting user's information using `userManagementService.getUserById(requestingUserId)`
2. Performs authorization check:
   - Allows access if the requesting user is the patient themselves (`requestingUserId.equals(patientId)`)
   - Allows access if the requesting user has the "PHYSIO" role
   - Throws `RuntimeException` with message "Not authorized to view this summary" if neither condition is met
3. If authorized, queries the repository using `summaryRepository.findByPatientIdAndMonthAndYear(patientId, month, year)`
4. If no summary is found, throws `RuntimeException` with message "Summary not found"
5. Returns the found `SummaryReport` object

**Error Handling:**
- Throws `RuntimeException` for authorization failures
- Throws `RuntimeException` when requested summary does not exist

#### 2. `getRecentSummaries(Long requestingUserId, Long patientId)`

**Description:** This method retrieves all summary reports for a patient in descending chronological order (most recent first).

**Implementation Logic:**
1. Retrieves the requesting user's information using `userManagementService.getUserById(requestingUserId)`
2. Performs authorization check:
   - Allows access if the requesting user is the patient themselves (`requestingUserId.equals(patientId)`)
   - Allows access if the requesting user has the "PHYSIO" role
   - Throws `RuntimeException` with message "Not authorized to view summaries" if neither condition is met
3. If authorized, queries the repository using `summaryRepository.findByPatientIdOrderByYearDescMonthDesc(patientId)`
4. Returns the list of `SummaryReport` objects sorted by year and month in descending order

**Error Handling:**
- Throws `RuntimeException` for authorization failures

### Authorization Model

Both methods implement a consistent authorization model:
- **Self-access:** Patients can view their own summaries
- **Physiotherapist access:** Users with role "PHYSIO" can view any patient's summaries
- All other access attempts are denied with appropriate error messages

---

## 5.1.5.3 Repository Layer

The Repository Layer provides data access functionality for summary reports using Spring Data JPA.

**Package:** `com.physiotrack.summary.repository`

**Interface:** `SummaryRepository`

**Annotations:** `@Repository`

**Extends:** `JpaRepository<SummaryReport, Long>`

### Repository Methods

The `SummaryRepository` interface extends `JpaRepository`, inheriting standard CRUD operations, and defines two custom query methods:

#### 1. `findByPatientIdOrderByYearDescMonthDesc(Long patientId)`

**Description:** Retrieves all summary reports for a specific patient, ordered by year and month in descending order (most recent first).

**Method Type:** Spring Data JPA derived query method

**Parameters:**
- `patientId`: The ID of the patient whose summaries are being retrieved

**Returns:** `List<SummaryReport>` - Collection of all summaries for the patient, sorted chronologically in descending order

**Query Derivation:** Spring Data JPA automatically generates the query based on the method name:
- `findBy`: Indicates a SELECT query
- `PatientId`: Filters by the `patientId` field
- `OrderBy`: Specifies sorting criteria
- `YearDesc`: Sorts by `year` field in descending order (primary sort)
- `MonthDesc`: Sorts by `month` field in descending order (secondary sort)

#### 2. `findByPatientIdAndMonthAndYear(Long patientId, int month, int year)`

**Description:** Retrieves a single summary report for a specific patient for a particular month and year.

**Method Type:** Spring Data JPA derived query method

**Parameters:**
- `patientId`: The ID of the patient
- `month`: The month number (1-12)
- `year`: The year number

**Returns:** `SummaryReport` - The summary report matching the criteria, or `null` if not found

**Query Derivation:** Spring Data JPA automatically generates the query:
- `findBy`: Indicates a SELECT query
- `PatientId`: Filters by `patientId` field
- `And`: Combines multiple conditions with logical AND
- `Month`: Filters by `month` field
- `And`: Another logical AND
- `Year`: Filters by `year` field

### Inherited Methods

Through `JpaRepository<SummaryReport, Long>`, the repository also provides:
- `save(SummaryReport entity)`: Save or update a summary report
- `findById(Long id)`: Find a summary by ID
- `findAll()`: Retrieve all summaries
- `count()`: Count total number of summaries
- `delete(SummaryReport entity)`: Delete a summary report
- And other standard JPA operations

---

## 5.1.5.4 Model Layer

The Model Layer defines the entity structure for summary reports in the database.

**Package:** `com.physiotrack.summary.model`

**Class:** `SummaryReport`

**Annotations:** 
- `@Entity`: Marks the class as a JPA entity
- `@Table(name = "summary_report")`: Specifies the database table name

### Entity Fields

#### 1. `id` (Long)
- **Annotations:** `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- **Description:** Primary key for the summary report, auto-generated using database identity column
- **Access:** Read-only (no setter provided)

#### 2. `patientId` (Long)
- **Description:** Foreign key reference to the patient this summary belongs to
- **Database Column:** `patientId` (default)
- **Constraints:** None explicitly defined at entity level
- **Access:** Getter and setter provided

#### 3. `month` (int)
- **Annotations:** `@Column(name = "month_num")`
- **Description:** The month number (1-12) for this summary report
- **Database Column:** `month_num`
- **Valid Range:** 1-12 (representing January through December)
- **Access:** Getter and setter provided

#### 4. `year` (int)
- **Annotations:** `@Column(name = "year_num")`
- **Description:** The year number for this summary report
- **Database Column:** `year_num`
- **Access:** Getter and setter provided

#### 5. `summaryData` (String)
- **Annotations:** `@Column(length = 4000)`
- **Description:** Contains the actual summary data, stored as JSON or simple text format
- **Database Column:** `summaryData` (default)
- **Max Length:** 4000 characters
- **Format:** Flexible - can store JSON objects or plain text
- **Example Content:** `{"progress": 75, "badges": ["streak-7"]}`
- **Access:** Getter and setter provided

### Accessor Methods

The entity provides standard JavaBean getter and setter methods for all fields except `id`:

- `Long getId()`: Returns the unique identifier
- `Long getPatientId()` / `setPatientId(Long patientId)`: Patient ID access
- `int getMonth()` / `setMonth(int month)`: Month access
- `int getYear()` / `setYear(int year)`: Year access
- `String getSummaryData()` / `setSummaryData(String summaryData)`: Summary data access

### Data Model Design

The `SummaryReport` entity uses a simple, flexible design:
- **Composite Business Key:** Combination of `patientId`, `month`, and `year` uniquely identifies a summary
- **Flexible Data Storage:** The `summaryData` field allows storing various summary formats without requiring schema changes
- **Time-based Organization:** Month and year fields enable easy chronological queries and ordering

---

## 5.1.5.5 Initializer Layer

The Initializer Layer is responsible for seeding initial test data for the summary module when the application starts.

**Package:** `com.physiotrack.summary.init`

**Class:** `SummaryDataInitializer`

**Annotations:**
- `@Component`: Marks the class as a Spring-managed component
- `@Order(1)`: Specifies the execution order (runs with priority 1)

**Implements:** `CommandLineRunner`

### Dependencies

- `SummaryRepository`: Injected via constructor for direct database access

### Initialization Logic

The `run(String... args)` method implements the data seeding logic and executes automatically when the application starts.

#### Method: `run(String... args)`

**Description:** Seeds the database with initial summary report data for testing and demonstration purposes.

**Implementation Steps:**

1. **Get Current Date:**
   - Retrieves the current date using `LocalDate.now()` to populate month and year fields for seeded data

2. **Legacy Patient Seed (Patient ID = 1):**
   - Checks if the repository is completely empty using `summaryRepository.count() == 0`
   - If empty, creates a summary for patient ID 1:
     - Sets `patientId` to 1L
     - Sets `month` to current month value
     - Sets `year` to current year
     - Sets `summaryData` to JSON: `{"progress": 75, "badges": ["streak-7"]}`
   - Saves the record to the database using `summaryRepository.save(r)`

3. **Demo Patients Seed (Patient IDs 4 and 5):**
   - Iterates through demo patient IDs: [4L, 5L] (representing patientX and patientY)
   - For each patient ID:
     - Checks if a summary already exists for the current month/year using `summaryRepository.findByPatientIdAndMonthAndYear(pid, now.getMonthValue(), now.getYear())`
     - If no existing summary found:
       - Creates a new `SummaryReport` object
       - Sets `patientId` to the current demo patient ID
       - Sets `month` to current month value
       - Sets `year` to current year
       - Sets `summaryData` to JSON: `{"progress": 40, "notes": "Demo summary for patient id={pid}"}`
       - Saves to database and captures the saved entity
       - Logs success message: `"[SUMMARY-SEED] Inserted summary id={saved.getId()} for patientId={pid}"`
     - If existing summary found:
       - Logs info message: `"[SUMMARY-SEED] Existing summary found for patientId={pid}, id={existing.getId()}"`

### Direct Repository Access for Seeding

**The module uses direct repository access for seeding test data:**

- **Method:** Direct invocation of `SummaryRepository` methods within the `CommandLineRunner`
- **Purpose:** Pre-populate the database with test data for development and demonstration
- **Seeding Approach:**
  - Uses `count()` to check if initial seeding is needed
  - Uses `findByPatientIdAndMonthAndYear()` to avoid duplicate entries
  - Uses `save()` to persist new summary records
- **Idempotent Design:** The initializer is designed to be idempotent - it checks for existing data before inserting, preventing duplicate records on application restarts
- **Logging:** Provides console output to track what data was seeded and what already existed

### Execution Context

- **Execution Timing:** Runs automatically during application startup due to `CommandLineRunner` implementation
- **Order:** Executes with order priority 1 (`@Order(1)`), allowing it to run early in the initialization sequence
- **Environment:** Suitable for development and testing environments; may need to be disabled or modified for production deployments

---

## Summary

The Summary Module follows a clean, layered architecture pattern:

1. **Service API Layer:** Defines clear contracts with two main operations for retrieving summaries
2. **Service Implementation Layer:** Implements business logic with robust authorization checks using role-based access control
3. **Repository Layer:** Leverages Spring Data JPA for efficient database access with custom query methods
4. **Model Layer:** Uses a flexible entity design with JSON storage for extensible summary data
5. **Initializer Layer:** Provides automatic test data seeding with idempotent design for reliable development environment setup

The module demonstrates best practices including:
- Separation of concerns across layers
- Authorization integrated at the service layer
- Spring Data JPA derived queries for maintainability
- Flexible data storage with JSON in the summaryData field
- Automated test data initialization for development
- Proper use of Spring annotations and dependency injection

**Direct Repository Access:** The module uses direct repository access in the `SummaryDataInitializer` class for seeding test data. This approach uses standard JPA repository methods (`count()`, `findByPatientIdAndMonthAndYear()`, `save()`) to check for existing data and insert new records in an idempotent manner.
