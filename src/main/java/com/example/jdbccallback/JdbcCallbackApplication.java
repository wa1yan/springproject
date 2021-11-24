package com.example.jdbccallback;

import com.example.jdbccallback.service.EmployeeReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class JdbcCallbackApplication implements CommandLineRunner {
    @Autowired
    private EmployeeReportService employeeReportService;

    public static void main(String[] args) {
        SpringApplication.run(JdbcCallbackApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        employeeReportService.printReport();
    }
}
