package ru.example.vacancyparser.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.example.vacancyparser.model.Vacancy;
import ru.example.vacancyparser.repository.VacancyRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class VacancyService {

    private final VacancyRepository repository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final Random random = new Random();

    private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>(100);

    public VacancyService(VacancyRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        Thread loggerThread = new Thread(() -> {
            while (true) {
                try {
                    String log = logQueue.take();
                    System.out.println("[LOG-DAEMON] " + log);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        loggerThread.setDaemon(true);
        loggerThread.start();
    }

    public void fetchInitialData() {
        new Thread(() -> {
            logQueue.add("Запуск парсинга вакансий в отдельном потоке...");
            parseAndSaveVacancies();
        }, "Initial-Parser-Thread").start();
    }

    @Scheduled(fixedRate = 60000) // Каждую минуту
    public void scheduledFetch() {
        logQueue.add("Плановая задача: запуск парсинга вакансий...");
        executorService.submit(this::parseAndSaveVacancies);
    }

    private void parseAndSaveVacancies() {
        String[] titles = {"Java Developer", "Spring Boot Engineer", "Backend Developer", "Senior Java Developer", "Middle Java Developer"};
        String[] companies = {"Yandex", "Sber", "Tinkoff", "VK", "Ozon"};
        String[] cities = {"Москва", "Санкт-Петербург", "Новосибирск", "Екатеринбург", "Казань"};

        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(500);

                String title = titles[random.nextInt(titles.length)];
                String company = companies[random.nextInt(companies.length)];
                String city = cities[random.nextInt(cities.length)];
                double salary = 80000 + random.nextInt(150000);
                LocalDate date = LocalDate.now();

                Vacancy vacancy = new Vacancy(title, company, salary, city, date);
                repository.save(vacancy);

                logQueue.add("Сохранена вакансия: " + title + " в " + company + " (" + city + ")");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public List<Vacancy> getAllVacanciesParallel() {
        List<Vacancy> allVacancies = repository.findAll();

        return allVacancies.parallelStream()
                .filter(v -> v.getSalary() > 0)
                .sorted((a, b) -> b.getPublishedDate().compareTo(a.getPublishedDate()))
                .collect(Collectors.toList());
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }
}