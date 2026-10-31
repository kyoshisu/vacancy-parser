package ru.example.vacancyparser.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vacancies")
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String company;
    private Double salary;
    private String city;
    private LocalDate publishedDate;

    public Vacancy() {}

    public Vacancy(String title, String company, Double salary, String city, LocalDate publishedDate) {
        this.title = title;
        this.company = company;
        this.salary = salary;
        this.city = city;
        this.publishedDate = publishedDate;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public LocalDate getPublishedDate() { return publishedDate; }
    public void setPublishedDate(LocalDate publishedDate) { this.publishedDate = publishedDate; }
}