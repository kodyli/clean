# PlantUML Patterns for ICONIX Process

> **Rule**: All diagrams produced during the ICONIX process **must** use PlantUML syntax, embedded in a fenced code block tagged `` ```plantuml ``.

---

## 1. Domain Model (Class Diagram)
*Used in Phase 1 — Domain Modeling and refined throughout Phases 2–3.*

Focus on real-world objects and their relationships (Aggregation, Generalization).

```plantuml
@startuml
skinparam classAttributeIconSize 0

class Customer {
  +name: String
  +dateOfBirth: Date
}

class Account {
  +accountNumber: String
  +balance: Double
}

class Address {
  +street: String
  +city: String
}

Customer "1" *-- "1..*" Account : holds
Customer "1" o-- "1" Address : lives at
@enduml
```

---

## 2. Use Case Diagram
*Used in Phase 1 — Use Case Modeling.*

Identify actors and their use cases; group into packages when needed.

```plantuml
@startuml
left to right direction

actor "Reporter" as reporter
actor "Line of Business" as lob

package "Deceased Record System" {
  usecase "Report Decedent" as UC1
  usecase "Notify Line of Business" as UC2
  usecase "Correct Erroneous Notification" as UC3
  usecase "Process Notification" as UC4
}

reporter --> UC1
UC1 ..> UC2 : <<invokes>>
reporter --> UC3
lob --> UC4
@enduml
```

---

## 3. Robustness Diagram (BCE)
*Used in Phase 2 — Robustness Analysis.*

Bridge the gap between requirements and design. Use Boundary, Controller, and Entity stereotypes.

```plantuml
@startuml
skinparam backgroundColor white

actor Reporter

boundary "Report Decedent UI" as UI
control "Report Decedent Handler" as Handler
entity "Customer" as Customer
entity "DeceasedRecord" as Record

Reporter -> UI : submits decedent info
UI -> Handler : validate and process
Handler -> Customer : lookup by ID
Handler -> Record : create record
Handler --> UI : confirmation
@enduml
```

---

## 4. Sequence Diagram
*Used in Phase 3 — Behavior Allocation.*

**Layout rule**: use case text as notes on the **left**, design decisions on the **right**.

```plantuml
@startuml
skinparam backgroundColor white

participant "Report Decedent UI" as UI
participant "ReportDecedentHandler" as Handler
participant "CustomerRepository" as Repo
participant "DeceasedRecordRepository" as RecordRepo

note left of UI
  Use Case: Report Decedent
  Basic Course:
  1. Reporter submits decedent info
end note

UI -> Handler : reportDecedent(command)
Handler -> Repo : findById(customerId)
Repo --> Handler : Customer

note left of Handler
  2. System validates customer exists
end note

Handler -> RecordRepo : save(DeceasedRecord)
RecordRepo --> Handler : saved record
Handler --> UI : success response

note left of UI
  3. System confirms submission
end note
@enduml
```

---

## 5. Class Diagram (Detailed Design)
*Used in Phase 3 — Static Modeling.*

Full class diagram with visibility, types, and multiplicity.

```plantuml
@startuml
skinparam classAttributeIconSize 0

interface ReportDecedentUseCase {
  +execute(command: ReportDecedentCommand): DeceasedRecord
}

class ReportDecedentService {
  -customerRepo: CustomerRepository
  -recordRepo: DeceasedRecordRepository
  +execute(command: ReportDecedentCommand): DeceasedRecord
}

interface CustomerRepository {
  +findById(id: CustomerId): Optional<Customer>
}

interface DeceasedRecordRepository {
  +save(record: DeceasedRecord): DeceasedRecord
}

class DeceasedRecord {
  -id: RecordId
  -customerId: CustomerId
  -reportedAt: LocalDateTime
  -status: RecordStatus
}

ReportDecedentUseCase <|.. ReportDecedentService
ReportDecedentService --> CustomerRepository
ReportDecedentService --> DeceasedRecordRepository
ReportDecedentService ..> DeceasedRecord : creates
@enduml
```
