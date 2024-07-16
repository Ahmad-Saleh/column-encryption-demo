package com.progressoft.corpay.columnencryptiondemo;

import com.github.javafaker.Faker;
import com.progressoft.corpay.columnencryptiondemo.repository.StudentEntity;
import com.progressoft.corpay.columnencryptiondemo.repository.StudentsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ColumnEncryptionDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColumnEncryptionDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(StudentsRepository studentsRepository) {
        return args -> {
            Faker faker = new Faker();
            for (int i = 0; i < 100; i++) {
                studentsRepository.save(StudentEntity.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .email(faker.internet().emailAddress())
                        .phone(faker.phoneNumber().phoneNumber())
                        .build());
            }
        };
    }
}
