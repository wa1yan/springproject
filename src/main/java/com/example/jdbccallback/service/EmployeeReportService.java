package com.example.jdbccallback.service;

import com.example.jdbccallback.dao.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeReportService {
    @Autowired
    private EmployeeDao employeeDao;

    public void printReport(){
        System.out.println("Employee Report start");
        System.out.println("Employee List");
        employeeDao.findEmployees()
                    .forEach(System.out::println);

        System.out.println("Employee Average salary Calculated Row By Row");
        System.out.println(employeeDao.findAverageSalaryRowByRow());

        System.out.println("Employee Average salary Calculated on Entire ResultSet");
        System.out.println(employeeDao.findAverageSalaryCalculatedOnEntireResultSet());

        System.out.println("Employee Average salary Calculated on SQL Level");
        System.out.println(employeeDao.findAverageSalarySQLLevel());

        System.out.println("Employee Average salary Calculated on Modern Implementation");
       // System.out.println(employeeDao.findAverageSalaryModernImplementation());

        System.out.println("Employee Id for suzan@gmail.com "+ employeeDao.findEmployeeIdByEmail("suzan@gmail.com"));

        System.out.println("Employee Report End");
    }
}
