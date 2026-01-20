# Journal Module - Technical Report

## 5.1.5 Journal Module Implementation

This report provides a comprehensive overview of the Journal module implementation in the PhysioTrack Spring Boot application, covering all architectural layers from the service API to the data initializer.

---

## 5.1.5.1 Service API Layer

The Service API layer defines the contract for journal operations through the `JournalService` interface, which provides the following methods:

```java
public interface JournalService {
    Journal createJournal(Long requestingUserId, Journal journal);
    List<Journal> getJournalsForPatient(Long requestingUserId, Long patientId);
    Journal getJournalById(Long requestingUserId, Long journalId);
    Journal updateJournal(Long requestingUserId, Long journalId, Journal updated);
    void deleteJournal(Long requestingUserId, Long journalId);
    Journal setSharedWithPhysio(Long requestingUserId, Long journalId, boolean shared);
}
```

### Purpose
The `JournalService` interface establishes a clear separation between the API contract and implementation, promoting loose coupling and testability. It ensures that all journal operations are performed with proper authorization checks by requiring a `requestingUserId` parameter in every method.

### Method Signatures
- **createJournal**: Creates a new journal entry with authorization validation
- **getJournalsForPatient**: Retrieves all journals for a specific patient with role-based filtering
- **getJournalById**: Fetches a single journal by ID with access control
- **updateJournal**: Updates an existing journal entry (owner-only operation)
- **deleteJournal**: Removes a journal entry (owner-only operation)
- **setSharedWithPhysio**: Toggles the sharing status of a journal with physiotherapists

---

## 5.1.5.2 Service Implementation Layer

The `JournalServiceImpl` class implements the `JournalService` interface and provides the core business logic for journal management, including authorization, validation, and data persistence operations.

### Dependencies
- **JournalRepository**: Handles database operations for journal entities
- **UserManagementService**: Validates user existence and retrieves user role information for authorization

### Implemented Methods

#### 1. createJournal(Long requestingUserId, Journal journal)
**Purpose**: Creates a new journal entry with authorization checks.

**Implementation Details**:
- Validates that the requesting user exists via UserManagementService
- Enforces authorization: only the patient themselves or a PHYSIO role can create journals
- Sets `createdAt` and `updatedAt` timestamps to current time
- Persists the journal entity through JournalRepository
- Throws RuntimeException if unauthorized

**Transaction Management**: Annotated with `@Transactional` to ensure atomicity

#### 2. getJournalsForPatient(Long requestingUserId, Long patientId)
**Purpose**: Retrieves all journal entries for a specific patient with role-based access control.

**Implementation Details**:
- Validates the requesting user's existence and retrieves their role
- Implements two-tier authorization:
  - Patients can view all their own journals
  - Physiotherapists can only view journals explicitly shared with them
- Returns journals ordered by creation date (descending)
- Filters results based on `sharedWithPhysio` flag for physiotherapist access
- Throws RuntimeException if the user is not authorized

#### 3. getJournalById(Long requestingUserId, Long journalId)
**Purpose**: Retrieves a single journal entry by ID with access control.

**Implementation Details**:
- Fetches the journal from repository, throwing RuntimeException if not found
- Validates user authorization using UserManagementService
- Grants access if:
  - The requesting user is the journal's owner (patientId matches)
  - The requesting user is a PHYSIO and the journal is marked as shared
- Throws RuntimeException for unauthorized access attempts

#### 4. updateJournal(Long requestingUserId, Long journalId, Journal updated)
**Purpose**: Updates an existing journal entry (owner-only operation).

**Implementation Details**:
- Retrieves the existing journal, throwing RuntimeException if not found
- Enforces strict ownership: only the patient who owns the journal can update it
- Performs selective field updates (null-safe):
  - title, weather, feeling, healthCondition, comment, imageUrl
- Updates the `updatedAt` timestamp to current time
- Preserves fields not included in the update request
- Returns the updated and persisted journal entity

**Transaction Management**: Annotated with `@Transactional`

#### 5. deleteJournal(Long requestingUserId, Long journalId)
**Purpose**: Deletes a journal entry (owner-only operation).

**Implementation Details**:
- Retrieves the existing journal to verify it exists
- Validates that only the owner (matching patientId) can delete
- Permanently removes the journal from the database
- Throws RuntimeException for unauthorized deletion attempts

**Transaction Management**: Annotated with `@Transactional`

#### 6. setSharedWithPhysio(Long requestingUserId, Long journalId, boolean shared)
**Purpose**: Toggles whether a journal is shared with physiotherapists.

**Implementation Details**:
- Fetches the journal from repository
- Enforces owner-only access control
- Updates the `sharedWithPhysio` flag to the provided boolean value
- Updates the `updatedAt` timestamp
- Persists and returns the modified journal
- Throws RuntimeException if the requesting user is not the owner

**Transaction Management**: Annotated with `@Transactional`

### Authorization Strategy
The service implements a comprehensive authorization model:
- **Patient Role**: Full CRUD access to their own journals
- **PHYSIO Role**: Read-only access to journals explicitly shared by patients
- **Cross-cutting**: All operations validate user existence and enforce role-based permissions

---

## 5.1.5.3 Repository Layer

The `JournalRepository` interface extends Spring Data JPA's `JpaRepository` to provide database access for Journal entities.

### Implementation
```java
@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
    List<Journal> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
```

### Inherited Methods (from JpaRepository)
By extending `JpaRepository<Journal, Long>`, the repository automatically provides:
- **save(Journal entity)**: Persists or updates a journal entity
- **findById(Long id)**: Retrieves a journal by its primary key, returning Optional<Journal>
- **delete(Journal entity)**: Removes a journal from the database
- **count()**: Returns the total count of journal entries (used in data initialization)
- Standard CRUD operations without requiring explicit implementation

### Custom Query Methods

#### findByPatientIdOrderByCreatedAtDesc(Long patientId)
**Purpose**: Retrieves all journals belonging to a specific patient, sorted by creation date in descending order (newest first).

**Implementation**: Spring Data JPA automatically generates the query based on method naming convention:
- `findBy`: Indicates a query method
- `PatientId`: Filters by the patientId field
- `OrderByCreatedAtDesc`: Sorts results by createdAt in descending order

**Usage**: Used by the service layer to fetch patient journals with proper ordering for chronological display

---

## 5.1.5.4 Model Layer

The `Journal` entity represents the core domain model for journal entries in the application.

### Entity Configuration
- **Entity Annotation**: `@Entity` marks the class as a JPA entity
- **Table Mapping**: `@Table(name = "journal")` maps to the "journal" database table
- **Primary Key**: `@Id` with `@GeneratedValue(strategy = GenerationType.IDENTITY)` for auto-incrementing IDs

### Entity Fields

#### Identity and Ownership
- **id** (Long): Primary key, auto-generated
- **patientId** (Long): Foreign key reference to the patient who owns this journal

#### Content Fields
- **title** (String): Brief title or subject of the journal entry
- **weather** (String): Weather condition when the entry was created
- **feeling** (String): Patient's emotional state or mood
- **healthCondition** (String): Description of the patient's health status
- **comment** (String): Detailed notes or observations (max length: 4000 characters via `@Column(length = 4000)`)
- **imageUrl** (String): Optional URL reference to an associated image

#### Sharing and Timestamps
- **sharedWithPhysio** (boolean): Flag indicating if the journal is visible to physiotherapists (default: false)
- **createdAt** (LocalDateTime): Timestamp of journal creation
- **updatedAt** (LocalDateTime): Timestamp of last modification

### Methods
The entity provides standard getter and setter methods for all fields, following JavaBean conventions. Notable aspects:
- **isSharedWithPhysio()**: Boolean getter following standard naming convention
- All setters enable fluent updates through the service layer
- No business logic in the entity itself (anemic domain model pattern)

### Design Considerations
- The `comment` field uses `@Column(length = 4000)` to accommodate longer text entries
- Default value for `sharedWithPhysio` is `false`, ensuring privacy by default
- Timestamp fields use `LocalDateTime` for modern Java time handling
- No validation annotations are present; validation is handled at the service layer

---

## 5.1.5.5 Initializer Layer

The `JournalDataInitializer` class is responsible for seeding initial test data into the database when the application starts.

### Implementation
The initializer implements Spring Boot's `CommandLineRunner` interface, which triggers the `run()` method after application context initialization.

### Dependencies
- **JournalRepository**: Used for direct repository access to seed test data

### Seeding Logic

#### run(String... args) Method
**Purpose**: Populates the database with sample journal data if the journal table is empty.

**Implementation Details**:
- Checks if the database is empty using `journalRepository.count() == 0`
- Creates a single sample journal entry with the following attributes:
  - **patientId**: 1L (references a pre-existing patient)
  - **title**: "First entry"
  - **weather**: "Sunny"
  - **feeling**: "Good"
  - **healthCondition**: "Stable"
  - **comment**: "Seeded journal entry."
  - **imageUrl**: null (no image)
  - **sharedWithPhysio**: false (not shared)
  - **createdAt/updatedAt**: Set to one day in the past using `LocalDateTime.now().minusDays(1)`
- Persists the entry using `journalRepository.save(j)`

### Direct Repository Access for Testing
The initializer demonstrates **direct repository access** for seeding test data:
- **Pattern**: Uses `@Component` annotation to register as a Spring bean
- **Execution**: Runs automatically on application startup via `CommandLineRunner`
- **Conditional Logic**: Only seeds data when the database is empty, preventing duplicate entries on subsequent runs
- **Purpose**: Provides immediate test data for development and demonstration without manual database setup
- **Dependencies**: Assumes that a user with ID 1 exists (from UserManagement module initialization)

### Benefits of This Approach
- **Automatic Setup**: Developers can start testing immediately after application launch
- **Idempotent**: Safe to run multiple times; only seeds when necessary
- **Simple**: Direct repository access makes the seeding logic straightforward
- **Development-Friendly**: Eliminates manual database population steps

---

## Summary

The Journal module implements a complete, layered architecture following Spring Boot best practices:

1. **Service API Layer**: Defines clear contracts with authorization-aware method signatures
2. **Service Implementation Layer**: Implements robust business logic with role-based access control and transaction management
3. **Repository Layer**: Leverages Spring Data JPA for efficient data access with custom query methods
4. **Model Layer**: Provides a clean entity model with appropriate field constraints
5. **Initializer Layer**: Enables rapid development with automatic test data seeding using direct repository access

The module demonstrates proper separation of concerns, with authorization logic in the service layer, data access abstracted through repositories, and initialization handled separately for testing convenience. The use of direct repository access in the initializer layer specifically supports testing and development workflows by providing ready-to-use sample data.
