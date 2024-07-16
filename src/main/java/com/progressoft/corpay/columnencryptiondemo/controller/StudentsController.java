package com.progressoft.corpay.columnencryptiondemo.controller;

import com.progressoft.corpay.columnencryptiondemo.model.Student;
import com.progressoft.corpay.columnencryptiondemo.repository.StudentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/students")
@RequiredArgsConstructor
public class StudentsController {

    private final StudentsRepository repository;
    private final StudentMapper mapper;

    @GetMapping
    public List<Student> listStudents(){
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @GetMapping("/{id}")
    public Student getStudent(Long id){
        return mapper.toModel(repository.findById(id).orElseThrow());
    }
}