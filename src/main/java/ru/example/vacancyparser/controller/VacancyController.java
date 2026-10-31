package ru.example.vacancyparser.controller;

import org.springframework.web.bind.annotation.*;
import ru.example.vacancyparser.model.Vacancy;
import ru.example.vacancyparser.service.VacancyService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VacancyController {

    private final VacancyService service;

    public VacancyController(VacancyService service) {
        this.service = service;
    }

    @PostMapping("/start")
    public String startParsing() {
        service.fetchInitialData();
        return "Парсинг вакансий запущен в фоновом режиме!";
    }

    @GetMapping("/answer")
    public List<Vacancy> getResults() {
        return service.getAllVacanciesParallel();
    }
}