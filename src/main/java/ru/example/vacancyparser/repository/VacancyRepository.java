package ru.example.vacancyparser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.vacancyparser.model.Vacancy;
import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    List<Vacancy> findByCity(String city);
    List<Vacancy> findByCompany(String company);
}