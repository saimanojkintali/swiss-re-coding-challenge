package com.swissre.assignment.model;

public record Employee(
        int id,
        String firstName,
        String lastName,
        double salary,
        Integer managerId
) {
    public boolean isCeo() {
        return managerId == null;
    }
}
