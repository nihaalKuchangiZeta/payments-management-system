package model;

public class Employee {
    private String eid;
    private String name;
    private String department;
    private long salary;

    public Employee(String eid, String name, String department, long salary) {
        this.eid = eid;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    // Getters and Setters
    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }
}
