Markdown

# ⚓ Naval Inspection System (Spring Boot Edition)

An enterprise-grade, interactive console application developed for the **Brazilian Navy** (Delegacia Fluvial de Furnas) to automate maritime inspection workflows, manage naval assets, and generate official operational reports. 

Recently refactored from legacy vanilla Java to a modern **Spring Boot** architecture, this project demonstrates strong object-oriented principles, robust database integration, and scalable modular design.

---

## 🚀 Key Architectural Highlights & Tech Stack
* **Java 17**: Leveraging modern language features (records, enhanced pattern matching, and time APIs).
* **Spring Boot 3.2.5**: Utilizing `CommandLineRunner` to orchestrate application lifecycle management, dependency injection (`@Service`), and component scanning.
* **SQLite JDBC**: Integrated lightweight relational persistence with automated schema bootstrapping and robust error handling.
* **Maven**: Clean build automation and structured dependency management adhering to standard project directory layouts.

---

## 🛠️ Core Business Modules & Features
* **Vessel Inspection & Boarding Management**: Track vessel details, registration metadata, distinct vessel classes (*Moto Aquática, Esporte e Recreio, Balsa, Transporte de Passageiros*), infraction notices (*A.I.*), seizure records (*A.I.*), and legal custodians (*F.D.*).
* **Military Staff Control**: Dynamic registration and role-based assignment of naval inspectors, crew members, military ranks, and credentials.
* **Operational Chronology**: Flexible multi-day mission tracking to record events across extended patrols and changing geographical areas (*PIN*).
* **Logistics & Reporting**: Real-time tracking of fuel consumption (gasoline and diesel) alongside automated formatting and generation of official operational reports.
* **Interactive CLI Menu**: A comprehensive, bulletproof terminal interface featuring input validation and runtime configuration editing.

---

## 📁 Project Architecture
```text
naval-inspection-spring/
│
├── src/main/java/
│   ├── application/        # Main Spring Boot starter & interactive console menu
│   ├── entities/           # Domain models (Vessel, Boarding, InspectionReport, Military Staff)
│   ├── services/           # Business logic & SQLite persistence integration layer
│   └── exceptions/         # Custom domain exception handlers
│
├── lib/                    # External JDBC drivers
├── pom.xml                 # Maven configuration & Spring Boot parent dependencies
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
