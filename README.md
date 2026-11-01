## Student information

- **Name:** RUREBWAYIRE Ampoze Ariella
- **Student ID:** 27640
- **Course:** INSY 8322 — Web Technology and Internet
- **Project:** UtilityWatch Outage Reporting System (Mid-Term Project Examination)
- **Academic Year:** 2026–2027

---

# UtilityWatch Outage Reporting System — Backend

A Spring Boot REST API for reporting and tracking water and electricity outages. Residents (Subscribers) file outage reports that are handled by Technicians assigned to Service Zones. Each report moves through a workflow — `REPORTED → DISPATCHED → RESOLVED` or `DISMISSED` — and once resolved, the technician records Repair Actions taken on site.

---

## Table of contents

1. [Overview](#overview)
2. [Tech stack](#tech-stack)
3. [Prerequisites](#prerequisites)
4. [Database setup](#database-setup)
5. [Configuration](#configuration)
6. [Running the application](#running-the-application)
7. [Project structure](#project-structure)
8. [Domain model](#domain-model)
9. [API reference](#api-reference)
   - [Part A — CRUD](#part-a--crud)
   - [Part B — Derived queries](#part-b--derived-queries)
   - [Part C — JPQL queries](#part-c--jpql-queries)
   - [Part D — Business rules](#part-d--business-rules)
   - [Part E — Repair actions](#part-e--repair-actions)
   - [Part F — Reports & bulk updates](#part-f--reports--bulk-updates)
   - [Bonus endpoints](#bonus-endpoints)
10. [Seed data](#seed-data)
11. [Testing with Postman](#testing-with-postman)
12. [Query type summary](#query-type-summary)
13. [Troubleshooting](#troubleshooting)
14. [License](#license)

---

## Overview

UtilityWatch lets residents report water and electricity outages and track their resolution. The backend exposes a REST API built with Spring Boot and Spring Data JPA, backed by PostgreSQL.

The six core entities play complementary roles:

- **Subscriber** — the resident the work is done for
- **Technician** — the person handling the report
- **TradeSkill** — a classification a technician can hold several of
- **ServiceZone** — the geographic area a technician is assigned to
- **OutageReport** — the dated transaction with a status
- **RepairAction** — a record written only after an outage report is RESOLVED

---

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1.1 |
| Persistence | Spring Data JPA / Hibernate |
| Database | PostgreSQL |
| Build | Maven (wrapper included) |
| API testing | Postman |

---

## Database setup

1. Open **pgAdmin** (or `psql`) connected to your local PostgreSQL server.
2. Create the database:

   ```sql
   CREATE DATABASE utility_watch_midterm;
   ```

   Tables will be created automatically by Hibernate on first run (`ddl-auto=update`).

---

## Configuration

The file `src/main/resources/application.properties` should contain:

```properties
spring.application.name=utility-watch-outage-reporting-system

spring.datasource.url=jdbc:postgresql://localhost:5432/utility_watch_midterm
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.error.include-message=always
```

The database password is **never committed** — it is read from the `DB_PASSWORD` environment variable, which you set in your terminal for the current session only.

---

## Running the application

### Windows (PowerShell)

```powershell
# Set the password for this terminal session only
$env:DB_PASSWORD="your-postgres-password"

# Run the app
.\mvnw.cmd spring-boot:run
```

The API is available at **`http://localhost:8080`**.

To stop the app, press `Ctrl+C` in the terminal.

---

## Project structure

```
utility-watch-outage-reporting-system/
├── README.md
├── pom.xml
├── mvnw / mvnw.cmd
├── utility-watch-outage-reporting-system-midterm.postman_collection.json
└── src/
    └── main/
        ├── java/rw/ac/auca/utility_watch_outage_reporting_system/
        │   ├── UtilityWatchOutageReportingSystemApplication.java
        │   ├── domain/            # Entities and OutageStatus enum
        │   │   ├── Subscriber.java
        │   │   ├── Technician.java
        │   │   ├── TradeSkill.java
        │   │   ├── ServiceZone.java
        │   │   ├── OutageReport.java
        │   │   ├── RepairAction.java
        │   │   └── OutageStatus.java
        │   ├── repository/        # Spring Data JPA repositories
        │   ├── service/           # Business logic
        │   └── controller/        # REST endpoints
        └── resources/
            └── application.properties
```

---

## Domain model

### Entities and relationships

| Entity | Table | Key fields | Relationships |
|---|---|---|---|
| **Subscriber** | `subscriber` | `id`, `firstName`, `lastName`, `meterNumber` (unique), `phone`, `address` | none |
| **Technician** | `technician` | `id`, `firstName`, `lastName`, `phone` | `@ManyToOne ServiceZone`; `@ManyToMany TradeSkill` |
| **TradeSkill** | `trade_skill` | `id`, `name` | `@ManyToMany(mappedBy="tradeSkills") Technician` (JsonIgnore) |
| **ServiceZone** | `service_zone` | `id`, `zoneNumber`, `name` | none |
| **OutageReport** | `outage_report` | `id`, `reportDate`, `status`, `utilityType`, `description` | `@ManyToOne Subscriber`; `@ManyToOne Technician` |
| **RepairAction** | `repair_action` | `id`, `actionName`, `notes`, `hoursTaken`, `actionDate` | `@ManyToOne OutageReport` |

### Enum — `OutageStatus`

```
REPORTED, DISPATCHED, RESOLVED, DISMISSED
```

Stored as `@Enumerated(EnumType.STRING)` on `OutageReport.status`.

### Status workflow

```
REPORTED ──► DISPATCHED ──► RESOLVED
   │             │
   └──► DISMISSED└──► DISMISSED

RESOLVED and DISMISSED are terminal.
```

All status changes must go through `PATCH /api/outage-reports/{id}/status` (see D2).

---

## API reference

Base URL: `http://localhost:8080`

### Part A — CRUD

For `{path}` = `trade-skills`, `service-zones`, `technicians`, `subscribers`, `outage-reports`.

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/{path}/save` | Create a new record — **201** on success |
| `GET` | `/api/{path}/all` | List all records |
| `GET` | `/api/{path}/{id}` | Get one record by UUID — **404** if not found |
| `PUT` | `/api/{path}/update/{id}` | Update a record — **404** if not found |
| `DELETE` | `/api/{path}/delete/{id}` | Delete a record — **404** if not found |

> **Note:** `PUT /api/outage-reports/update/{id}` does **not** change the status — that is reserved for D2.

### Part B — Derived queries

| Method | Path | Behaviour | Query type |
|---|---|---|---|
| `GET` | `/api/subscribers/by-last-name?lastName=ampoze` | Case-insensitive match, sorted by firstName A–Z | **DERIVED** (`IgnoreCase`, `OrderBy`) |
| `GET` | `/api/outage-reports/by-status?status=REPORTED` | Reports in that status, earliest first | **DERIVED** (enum parameter, `OrderBy`) |
| `GET` | `/api/outage-reports/between?start=2026-10-01&end=2026-10-31` | Inclusive date range, sorted by date | **DERIVED** (`Between`) |

### Part C — JPQL queries

| Method | Path | Behaviour | Query type |
|---|---|---|---|
| `GET` | `/api/technicians/by-trade-skill?name=water` | Technicians holding that skill (case-insensitive) | **JPQL** (`JOIN`, `LOWER`) |
| `GET` | `/api/service-zones/free` | Zones no technician occupies | **JPQL** (`NOT EXISTS`) |
| `GET` | `/api/trade-skills/unused` | Skills no technician holds | **JPQL** (`IS EMPTY`) |
| `GET` | `/api/subscribers/of-technician/{technicianId}` | Unique subscribers who filed reports with that technician | **JPQL** (`DISTINCT`, path navigation) |

`C4` returns **404** with `"The technician with that id does not exist"` when the technician is missing.

### Part D — Business rules

#### D1 — Safe save
`POST /api/outage-reports/save`

Rejects:
- **400** — `"Outage report date cannot be in the past"`
- **409** — `"Technician is already dispatched on that date"`

Otherwise saves with status forced to `REPORTED` (**201**).

#### D2 — Status flow
`PATCH /api/outage-reports/{id}/status?status=DISPATCHED`

Allowed moves only:
- `REPORTED → DISPATCHED` or `DISMISSED`
- `DISPATCHED → RESOLVED` or `DISMISSED`
- `RESOLVED` and `DISMISSED` are terminal

Responses:
- **200** — `"Outage report status updated successfully"`
- **400** — `"Cannot change status from X to Y"`
- **404** — `"The outage report with that id does not exist"`

#### D3 — Add trade skill to technician
`PUT /api/technicians/{technicianId}/trade-skills/{tradeSkillId}`

- **200** — `"Trade skill added to the technician"`
- **404** — `"The technician with that id does not exist"` / `"That trade skill does not exist"`
- **409** — `"The technician already has that trade skill"`

#### D4 — Move technician to another zone
`PATCH /api/technicians/{technicianId}/service-zone?zoneNumber=4`

- **200** — `"Service zone changed successfully"`
- **404** — missing technician or zone number
- **409** — `"That service zone is taken by another technician"`

### Part E — Repair actions

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/repair-actions/save?outageReportId=...` | Save a repair action — report must be RESOLVED |
| `GET` | `/api/repair-actions/subscriber/{meterNumber}` | All repair actions for that subscriber, newest first |
| `DELETE` | `/api/repair-actions/delete/{id}` | Delete a repair action |

- **E1:** **201** `"Repair action saved successfully"`; **400** `"Repair actions are only for RESOLVED outage reports"`; **404** missing report
- **E2:** **JPQL** with path navigation (`a.outageReport.subscriber.meterNumber`) ordered by `actionDate DESC`
- **E3:** An outage report with repair actions cannot be deleted — `DELETE /api/outage-reports/delete/{id}` returns **409** `"That outage report has repair actions, delete them first"`

### Part F — Reports & bulk updates

#### F1 — Reports per status
`GET /api/outage-reports/stats/by-status`

Returns `[[status, count], ...]` as `List<Object[]>`. **JPQL** with `GROUP BY` + `COUNT`.

#### F2 — Technician absent
`PATCH /api/outage-reports/cancel-day?technicianId=...&date=2026-10-20`

Sets every non-RESOLVED report of that technician on that date to `DISMISSED`. Returns `"N outage reports cancelled"`.

**JPQL** UPDATE with `@Modifying` and `@Transactional`.

### Bonus endpoints

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/dashboard/summary` | 6 named counts as `Map<String, Long>` (LinkedHashMap) |
| `GET` | `/api/outage-reports/page?page=0&size=5&sort=reportDate,desc` | Returns `Page<OutageReport>` with metadata |

---

## Seed data

Import the Postman collection and run the requests in the **0 Seed** folder in this order:

1. **4 Trade Skills** — Water Network, Power Lines, Metering, Transformers
2. **4 Service Zones** — numbers 1, 2, 3, 4
3. **3 Technicians** — one holding two skills, one holding exactly one, one holding none
4. **5 Subscribers** — two sharing the lastName "Ampoze"; one with `meterNumber = MTR-04481927`
5. **10 Outage Reports** — spread across October and November 2026, using all four statuses; one subscriber has at least 3 reports; one technician has two reports on 2026-10-20 (one DISMISSED); at least two reports are RESOLVED

Run all the seed requests **before** implementing or testing D1 rules, otherwise the D1 date rule will reject past-dated reports.

---

## Testing with Postman

1. Import `utility-watch-outage-reporting-system-midterm.postman_collection.json` into Postman.
2. Create an environment with:
   - `baseUrl` = `http://localhost:8080`
   - UUID variables for each seed record (subscriber IDs, technician IDs, etc.)
3. Run the **0 Seed** folder first.
4. Then test each requirement individually. Each requirement has one success case and at least one error case in the collection.

---

## Query type summary

The exam requires each query to use a specific implementation type:

| Requirement | Type | Method |
|---|---|---|
| B1 | DERIVED | `findByLastNameIgnoreCaseOrderByFirstNameAsc` |
| B2 | DERIVED | `findByStatusOrderByReportDateAsc` |
| B3 | DERIVED | `findByReportDateBetweenOrderByReportDateAsc` |
| C1 | JPQL | `JOIN t.tradeSkills s WHERE LOWER(s.name) LIKE LOWER(...)` |
| C2 | JPQL | `NOT EXISTS (SELECT t FROM Technician t WHERE t.serviceZone = z)` |
| C3 | JPQL | `WHERE t.technicians IS EMPTY` |
| C4 | JPQL | `SELECT DISTINCT r.subscriber FROM OutageReport r WHERE r.technician.id = :id` |
| D1 | Business rule | `LocalDate.now()` + `existsByTechnicianIdAndReportDateAndStatusNot` |
| D2 | Business rule | Enum comparison with `==` |
| D3 | Business rule | Many-to-many list `add` + `contains` |
| D4 | Business rule + DERIVED | `findByZoneNumber` + `existsByServiceZone` |
| E1 | Business rule | Enum check `!= OutageStatus.RESOLVED` |
| E2 | JPQL | `a.outageReport.subscriber.meterNumber` + `ORDER BY actionDate DESC` |
| E3 | CRUD + DERIVED | `existsByOutageReport` guard |
| F1 | JPQL | `GROUP BY r.status`, `COUNT(r)` |
| F2 | JPQL | `@Modifying` + `@Transactional` UPDATE |
| Bonus 1 | Combined | `count()`, `countByReportDate`, `countByStatus`, reuse C2 |
| Bonus 2 | Spring Data | `Pageable` + `Page` |

---

## Troubleshooting

| Problem | Fix |
|---|---|
| `mvnw.cmd` not found | Run from the project root; use `./mvnw` on macOS/Linux |
| Port 8080 already in use | Stop the other Spring Boot instance: `Get-Process -Name java \| Stop-Process` |
| PostgreSQL connection refused | Confirm PostgreSQL is running on port 5432 |
| `password authentication failed` | Check `DB_PASSWORD` env var; the value must match your local Postgres password |
| Startup error: `PropertyReferenceException` | Derived query method name does not match entity field — check spelling/case |
| Startup error: `Expecting a SELECT query : UPDATE` | Missing `@Modifying` on a repository method |
| Runtime: `TransactionRequiredException` | Missing `@Transactional` on the service method |
| `Invalid UUID string: <path>` | Postman is using GET instead of the correct verb, or the app was not recompiled |
| Infinite JSON loop | `TradeSkill.technicians` must be annotated `@JsonIgnore` |
| Lazy-loading error on `/api/technicians/all` | Keep `spring.jpa.open-in-view` at its default (`true`) |

---

© 2026 RUREBWAYIRE Ampoze Ariella (Student ID: 27640)