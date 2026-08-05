# Opportunity Dashboard

A REST API that aggregates scholarships, fellowships, and pipeline programs, spanning high school through college, and matches them to individual students based on GPA, grade level, and interests.

Built with Spring Boot and PostgreSQL.

## Why

The programs that change a student's trajectory the most, Thrive Scholars, the Gates Scholarship, QuestBridge, sophomore-specific tech fellowships, are usually the hardest to find. They're scattered across mailing lists, Instagram posts, and word of mouth, and most students don't hear about them until the deadline has passed. This isn't another internship board duplicating what's already on LinkedIn. It centralizes access and pipeline opportunities in a queryable database, then scores and ranks them for each student so the best-fit programs surface first, instead of a student having to guess whether they even qualify.

## Tech Stack

- **Java 17**
- **Spring Boot 3.5** (Web, Data JPA)
- **PostgreSQL**
- **Maven**

## Features

- Full CRUD for opportunities and students
- Filter opportunities by grade level, type, or both
- Each opportunity tracks name, type, deadline, eligibility criteria, minimum GPA, year, grade level, and application link
- **Weighted matching algorithm**: scores every opportunity for a given student (0-100) based on GPA fit, grade level fit, and interest fit, then returns them ranked best-match first. GPA requirements are treated holistically, a student below the stated minimum still shows up, just scored lower, since many programs review holistically rather than enforcing a hard cutoff.
- Every match includes plain-language reasons behind its score, so results aren't a black box

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
| GET | `/api/students` | Get all students |
| GET | `/api/students/{id}` | Get a single student by ID |
| POST | `/api/students` | Create a new student |
| PUT | `/api/students/{id}` | Update an existing student |
| DELETE | `/api/students/{id}` | Delete a student |
| GET | `/api/students/{id}/matches` | Get every opportunity scored and ranked for this student, best match first |

### Example: Create an opportunity

```bash
curl -X POST http://localhost:8080/api/opportunities \
  -H "Content-Type: application/json" \
  -d '{
    "name": "The Gates Scholarship",
    "type": "Scholarship",
    "deadline": "2026-09-15",
    "eligibility": "High school senior, Pell-eligible, one of several specified minority backgrounds",
    "minimumGpa": 3.3,
    "year": "Senior",
    "gradeLevel": "High School Senior",
    "link": "https://www.thegatesscholarship.org"
  }'
```

### Example: Create a student

```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jordan Lee",
    "gpa": 3.4,
    "gradeLevel": "High School Senior",
    "interests": ["Scholarship", "Fellowship"]
  }'
```

### Example: Get ranked matches for a student

```bash
curl "http://localhost:8080/api/students/1/matches"
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
| gradeLevel | String | e.g. Middle School, High School Senior, College Sophomore |
| link | String | Application link |

**Student**
| Field | Type | Description |
|---|---|---|
| id | Long | Auto-generated primary key |
| name | String | Student name |
| gpa | Double | Student's GPA |
| gradeLevel | String | e.g. High School Senior, College Sophomore |
| interests | List\<String\> | Opportunity types the student is interested in, e.g. Scholarship, Fellowship |

## How Matching Works

Each opportunity is scored out of 100 for a given student:

- **GPA fit (35 pts)**: full credit if there's no stated minimum or the student meets it, partial credit if they're close (holistic review), reduced but non-zero credit if they're further below
- **Grade level fit (35 pts)**: full credit for an exact match or an opportunity open to all levels, reduced credit for a mismatch
- **Interest fit (30 pts)**: full credit if the opportunity's type matches one of the student's stated interests, neutral credit if the student hasn't specified interests, zero if there's a clear mismatch

Results are sorted highest score first, and every match includes a list of plain-language reasons behind its score.

## Roadmap

- [ ] Expanded seed dataset (scholarships, fellowships, and pipeline programs across grade levels)
- [ ] Deployment (Railway or Render)

## Author

Built by [Eman Mohamed](https://github.com/emanmoh25).
