package com.swissre.assignment.service;

import com.swissre.assignment.model.Employee;

import java.util.*;
import java.util.stream.Collectors;

public class OrganizationAnalyzer {

    private final Map<Integer, Employee> employeeMap;
    private final Map<Integer, List<Employee>> subordinatesMap;

    public OrganizationAnalyzer(List<Employee> employees) {
        this.employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::id, e -> e));
        
        this.subordinatesMap = employees.stream()
                .filter(e -> !e.isCeo())
                .collect(Collectors.groupingBy(Employee::managerId));
    }

    public void analyzeSalaries() {
        for (Map.Entry<Integer, List<Employee>> entry : subordinatesMap.entrySet()) {
            Integer managerId = entry.getKey();
            List<Employee> subordinates = entry.getValue();
            Employee manager = employeeMap.get(managerId);

            if (manager == null) continue; // In case of invalid manager reference

            double avgSubordinateSalary = subordinates.stream()
                    .mapToDouble(Employee::salary)
                    .average()
                    .orElse(0.0);

            double minRequiredSalary = avgSubordinateSalary * 1.2;
            double maxAllowedSalary = avgSubordinateSalary * 1.5;

            if (manager.salary() < minRequiredSalary) {
                double shortfall = minRequiredSalary - manager.salary();
                System.out.printf("Manager %s %s earns %.2f less than they should.\n", 
                        manager.firstName(), manager.lastName(), shortfall);
            } else if (manager.salary() > maxAllowedSalary) {
                double excess = manager.salary() - maxAllowedSalary;
                System.out.printf("Manager %s %s earns %.2f more than they should.\n", 
                        manager.firstName(), manager.lastName(), excess);
            }
        }
    }

    public void analyzeReportingLines() {
        for (Employee employee : employeeMap.values()) {
            int depth = calculateReportingDepth(employee);
            if (depth > 4) {
                System.out.printf("Employee %s %s has a reporting line that is too long by %d.\n", 
                        employee.firstName(), employee.lastName(), depth - 4);
            }
        }
    }

    private int calculateReportingDepth(Employee employee) {
        int depth = 0;
        Integer currentManagerId = employee.managerId();
        
        while (currentManagerId != null) {
            depth++;
            Employee manager = employeeMap.get(currentManagerId);
            if (manager != null) {
                currentManagerId = manager.managerId();
            } else {
                break; // Break if hierarchy is broken
            }
        }
        return depth;
    }
}
