package com.progressoft.corpay.columnencryptiondemo.controller;

import com.progressoft.corpay.columnencryptiondemo.model.Student;
import com.progressoft.corpay.columnencryptiondemo.repository.StudentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    Student toModel(StudentEntity studentEntity);
}
