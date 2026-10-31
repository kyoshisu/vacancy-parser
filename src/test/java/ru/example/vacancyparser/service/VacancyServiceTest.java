package ru.example.vacancyparser.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.example.vacancyparser.repository.VacancyRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class VacancyServiceTest {

    @Test
    void testServiceCreation() {
        VacancyRepository repository = Mockito.mock(VacancyRepository.class);
        VacancyService service = new VacancyService(repository);
        assertNotNull(service);
    }
}