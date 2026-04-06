# swiss-re-coding-challenge
swiss-re-coding-challenge

# Organization Structure & Salary Analyzer

A lightweight, dependency-free (except for testing) Java application designed to analyze an organization's structural integrity and salary distributions based on a provided employee CSV file.

## Table of Contents
- [Overview](#overview)
- [Design Choices & Architecture](#design-choices--architecture)
- [Prerequisites](#prerequisites)
- [How to Build and Run](#how-to-build-and-run)
- [How to Run Tests](#how-to-run-tests)
- [Assumptions Made](#assumptions-made)

## Overview
This tool parses employee data to identify two specific organizational anomalies:
1. **Salary Discrepancies:** Managers who earn less than 20% or more than 50% above the average salary of their *direct* subordinates.
2. **Reporting Line Length:** Employees who have more than 4 managers between themselves and the CEO.

## Design Choices & Architecture
The solution was built with simplicity, extensibility, and performance in mind:
* **Java 17 Records:** The `Employee` domain model utilizes Java `record` classes to ensure data immutability and thread safety, reducing boilerplate code.
* **Separation of Concerns:** The application is strictly divided into Domain Models (`Employee`), Utilities (`CsvParser` using NIO), and Business Logic (`OrganizationAnalyzer`), making it easy to swap out the data source (e.g., replacing the CSV parser with a database repository) without touching the core logic.
* **Performance / Time Complexity:** * The dataset is loaded into HashMaps. This ensures that hierarchical lookups (finding a manager by ID) happen in `O(1)` time.
  * The overall time complexity for the analysis is `O(N)`, where `N` is the number of employees, making the solution highly scalable even if the organization grows well beyond the stated 1000-employee limit.
* **Stream API:** Utilized for functional, declarative data transformations that are easy to read and maintain.

## Prerequisites
- **Java:** JDK 17 or higher
- **Build Tool:** Apache Maven 3.6+

## How to Build and Run

**1. Clone the repository and navigate into the directory:**
```bash
git clone <your-github-repo-url>
cd <your-repo-folder-name>

Compile the project and build the JAR file:
mvn clean package

Run the application:
You can run the built JAR file, passing the path to the CSV file as an argument. A sample employees.csv is included in the root directory for convenience.

java -cp target/org-analyzer-1.0-SNAPSHOT.jar com.swissre.assignment.Main employees.csv


