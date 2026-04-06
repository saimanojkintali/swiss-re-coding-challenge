package com.swissre.assignment.service;

import com.swissre.assignment.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrganizationAnalyzerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        // Redirect System.out to capture the printed statements during tests
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        // Restore standard console output after tests
        System.setOut(originalOut);
    }

    @Test
    void testAnalyzeSalaries_ManagerEarnsTooLittle() {
        // Setup: CEO, an underpaid manager, and a subordinate
        Employee ceo = new Employee(1, "CEO", "Boss", 100000, null);
        // Subordinate earns 50,000. Manager should earn at least 20% more (60,000). 
        // We set manager salary to 55,000, so they are short by 5,000.
        Employee manager = new Employee(2, "Underpaid", "Manager", 55000, 1);
        Employee sub = new Employee(3, "Sub", "Worker", 50000, 2);

        List<Employee> employees = Arrays.asList(ceo, manager, sub);
        OrganizationAnalyzer analyzer = new OrganizationAnalyzer(employees);

        analyzer.analyzeSalaries();

        String output = outContent.toString();
        assertTrue(output.contains("Underpaid Manager earns 5000.00 less than they should"), 
                "Should detect underpaid manager and calculate shortfall correctly.");
    }

    @Test
    void testAnalyzeSalaries_ManagerEarnsTooMuch() {
        // Setup: CEO, an overpaid manager, and a subordinate
        Employee ceo = new Employee(1, "CEO", "Boss", 100000, null);
        // Subordinate earns 50,000. Manager should earn max 50% more (75,000).
        // We set manager salary to 80,000, so they are over by 5,000.
        Employee manager = new Employee(4, "Overpaid", "Manager", 80000, 1);
        Employee sub = new Employee(5, "Sub", "Worker", 50000, 4);

        List<Employee> employees = Arrays.asList(ceo, manager, sub);
        OrganizationAnalyzer analyzer = new OrganizationAnalyzer(employees);

        analyzer.analyzeSalaries();

        String output = outContent.toString();
        assertTrue(output.contains("Overpaid Manager earns 5000.00 more than they should"),
                "Should detect overpaid manager and calculate excess correctly.");
    }

    @Test
    void testAnalyzeReportingLines_TooLong() {
        // Setup: A deep hierarchy of 5 managers between the employee and the CEO
        Employee ceo = new Employee(1, "CEO", "Boss", 100000, null);
        Employee m1 = new Employee(2, "Manager", "One", 90000, 1);
        Employee m2 = new Employee(3, "Manager", "Two", 80000, 2);
        Employee m3 = new Employee(4, "Manager", "Three", 70000, 3);
        Employee m4 = new Employee(5, "Manager", "Four", 60000, 4);
        // Employee 6 reports to m4. Depth = 5 (m4, m3, m2, m1, ceo). Limit is 4.
        Employee e6 = new Employee(6, "Deep", "Worker", 50000, 5);

        List<Employee> employees = Arrays.asList(ceo, m1, m2, m3, m4, e6);
        OrganizationAnalyzer analyzer = new OrganizationAnalyzer(employees);

        analyzer.analyzeReportingLines();

        String output = outContent.toString();
        assertTrue(output.contains("Deep Worker has a reporting line that is too long by 1"),
                "Should detect reporting lines longer than 4 levels.");
    }
}
