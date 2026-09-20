# ⚓ Naval Inspection Spring (`naval-inspection-spring`)

Enterprise backend system developed in **Spring Boot** to manage and automate naval inspection reports, strictly complying with the official regulatory layout of the **Brazilian Navy (Delegacia Fluvial de Furnas)**.

---

## 🚀 Key Architectural Highlights & Tech Stack
* **Java 17**: Leveraging modern language features (records, enhanced pattern matching, and time APIs).
* **Spring Boot 3.2.5**: Enterprise application lifecycle management, automated component scanning, and robust dependency injection.
* **Spring Web (REST API)**: Controllers exposing endpoints for remote inspection handling and integration.
* **OpenPDF**: Advanced programmatic generation of immutable official PDF documents featuring exact structured tables, headers, and official footer signoffs.
* **File Processing & Export**: Simultaneous, synchronized generation of structured PDF reports, editable CSV spreadsheets (compatible with LibreOffice/Excel), and operational TXT logs.
* **Maven**: Clean build automation and structured dependency management adhering to standard enterprise project directory layouts (`src/main/java`).

---

## 🛠️ Core Business Modules & Features
* **Official PDF & CSV Generation**: Automated creation of immutable PDF documents featuring precise institutional table layouts, alongside structured data export to CSV format.
* **Vessel Inspection & Boarding Management**: Track vessel details, registration metadata, distinct vessel classes (*Moto Aquática, Esporte e Recreio, Balsa, Transporte de Passageiros*), infraction notices (*A.I.*), seizure records (*A.I.*), and legal custodians (*F.D.*).
* **Military Staff Control**: Dynamic registration and role-based assignment of naval inspectors, crew members, military ranks, and credentials.
* **Operational Chronology**: Flexible multi-day mission tracking to record events across extended patrols and changing geographical areas (*PIN*).
* **Logistics & Reporting**: Real-time tracking of fuel consumption (gasoline and diesel) alongside automated formatting and generation of official operational reports.
* **Interactive CLI Menu & REST Endpoints**: A comprehensive terminal interface combined with REST controllers (`/api/inspections`) to handle operational commands locally or via remote triggers.

---

## 🌐 REST API Endpoints
* **`POST /api/inspections`**: Receives remote inspection parameters, triggers business logic, and generates synchronized official reports directly on the server environment.

---

## 📁 Project Architecture
```text
naval-inspection-spring/
│
├── src/main/java/
│   ├── application/        # Main Spring Boot starter, interactive CLI menu & REST Controllers
│   ├── entities/           # Domain models (Vessel, Boarding, InspectionReport, Military Staff)
│   ├── services/           # Business logic & export orchestration layers
│   └── exceptions/         # Custom domain exception handlers
│
├── pom.xml                 # Maven configuration & Spring Boot / OpenPDF dependencies
└── README.md

⚙️ How to Run Locally

    Ensure you have Java 17 and Maven installed on your machine.

    Clone this repository:
    Bash

    git clone [https://github.com/your-username/naval-inspection-spring.git](https://github.com/your-username/naval-inspection-spring.git)

    Navigate into the project directory:
    Bash

    cd naval-inspection-spring

    Run the application using Maven:
    Bash

    mvn spring-boot:run
