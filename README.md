# Opportunity Dashboard

A REST API that aggregates internships, fellowships, and other educational opportunities in one place, with filtering to help students quickly find programs they're actually eligible for.

Built with Spring Boot and PostgreSQL.

## Why

Opportunity info for students is scattered across mailing lists, Instagram posts, and word of mouth, and most of it doesn't say clearly who can actually apply. This project centralizes opportunities in a queryable database so students can filter by grade level and type instead of digging through a dozen sources.

## Tech Stack

- **Java 17**
- **Spring Boot 3.5** (Web, Data JPA)
- **PostgreSQL**
- **Maven**

## Features

- Full CRUD for opportunities (create, read, update, delete)
- Filter opportunities by grade level, type, or both
- Each opportunity tracks name, type, deadline, eligibility criteria, minimum GPA, year, grade level, and application link

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL running locally (or a connection string to a hosted instance)
- Maven (or use the included `mvnw` wrapper)

### Setup

1. Clone the repo:
   ```bash
   git clone https://github.com/emanmoh25/opportunity-dashboard.git
   cd opportunity-dashboard
   ```

2. Create a local PostgreSQL database:
   ```bash
   createdb opportunity_dashboard
   ```

3. Set your database credentials as environment variables (or fall back to the defaults in `application.properties`):
   ```bash
   export DATABASE_URL=jdbc:postgresql://localhost:5432/opportunity_dashboard
   export DATABASE_USERNAME=your_username
   export DATABASE_PASSWORD=your_password
   ```

4. Run the app:
   ```bash
   ./mvnw spring-boot:run
   ```

The API will be available at `http://localhost:8080`.

## API Reference

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/opportunities` | Get all opportunities |
| GET | `/api/opportunities/{id}` | Get a single opportunity by ID |
| POST | `/api/opportunities` | Create a new opportunity |
| PUT | `/api/opportunities/{id}` | Update an existing opportunity |
| DELETE | `/api/opportunities/{id}` | Delete an opportunity |
| GET | `/api/opportunities/filter?gradeLevel=&type=` | Filter opportunities by grade level and/or type |

### Example: Create an opportunity

```bash
curl -X POST http://localhost:8080/api/opportunities \
  -H "Content-Type: application/json" \
  -d '{
    "name": "ColorStack Summer Fellowship",
    "type": "Fellowship",
    "deadline": "2026-11-01",
    "eligibility": "Underrepresented CS students",
    "minimumGpa": 3.0,
    "year": "Sophomore",
    "gradeLevel": "College Sophomore",
    "link": "https://colorstack.org"
  }'
```

### Example: Filter by grade level

```bash
curl "http://localhost:8080/api/opportunities/filter?gradeLevel=College%20Sophomore"
```

## Data Model

**Opportunity**
| Field | Type | Description |
|---|---|---|
| id | Long | Auto-generated primary key |
| name | String | Opportunity name |
| type | String | e.g. Internship, Fellowship, Scholarship |
| deadline | String | Application deadline |
| eligibility | String | Free-text eligibility description |
| minimumGpa | Double | Minimum GPA requirement, if any |
| year | String | Target class year |
| gradeLevel | String | e.g. Middle School, High School, College Sophomore |
| link | String | Application link |

## Roadmap

- [ ] `Student` entity to represent user profiles (GPA, class year, interests)
- [ ] Eligibility matching algorithm to recommend opportunities per student
- [ ] Unit and integration tests
- [ ] Expanded seed dataset
- [ ] Deployment (Railway or Render)

## Author

Built by [Eman Mohamed](https://github.com/emanmoh25).
