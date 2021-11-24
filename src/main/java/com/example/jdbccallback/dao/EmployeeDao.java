package com.example.jdbccallback.dao;

import com.example.jdbccallback.ds.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EmployeeDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
     public void setDatasource(DataSource datasource){
      jdbcTemplate = new JdbcTemplate(datasource);
    }

    //RowMapper Usage
    public List<Employee> findEmployees() {
        return jdbcTemplate.query("select * from employee", new RowMapper<Employee>() {
            @Override
            public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getDate("hire_date"),
                        rs.getFloat("salary")
                );
            }
        });
    }

    //RowCallBackHandaler Usage
    public float findAverageSalaryRowByRow(){
        AverageSalaryRowCallBackHandler averageSalaryRowCallBackHandler = new AverageSalaryRowCallBackHandler();
        jdbcTemplate.query("select salary from employee",
                averageSalaryRowCallBackHandler);
        return averageSalaryRowCallBackHandler.getAverageSalary();
    }

    private static class AverageSalaryRowCallBackHandler implements RowCallbackHandler{
        private float averageSalary = 0;
        private float salaryCount = 0;

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            averageSalary += rs.getInt("salary");
            ++salaryCount;
        }

        public float getAverageSalary(){
            return averageSalary/(float) salaryCount;
        }
    }

    //ResultSetExtractor Usage
    public float findAverageSalaryCalculatedOnEntireResultSet(){
        return jdbcTemplate.query("select salary from employee",
                new AverageSalaryResultsetExtractor());
    }
    private static class AverageSalaryResultsetExtractor implements ResultSetExtractor<Float>{
        private float averageSalary = 0;
        private float salaryCount = 0;
        @Override
        public Float extractData(ResultSet rs) throws SQLException, DataAccessException {
            while(rs.next()){
                averageSalary += rs.getInt("salary");
                ++salaryCount;
            }
            return averageSalary/ (float) salaryCount;
        }
    }

    public double findAverageSalarySQLLevel(){
         return jdbcTemplate.queryForObject("select avg(salary) from employee", Double.class);
    }

    public double findAverageSalaryModernImplementation(){
        return jdbcTemplate.queryForList("select salary form employee", Double.class)
                .stream()
                    .mapToDouble(Double::valueOf)
                    .average()
                    .orElse(0f);
    }


    //Another jdbc callback
    //PrepareStatementCreator, PrepareStatementSetter
    public int findEmployeeIdByEmail(String email){
        return jdbcTemplate.query(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        return con.prepareStatement("select employee_id from employee where email = ?");
                    }
                },
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1,email);
                    }
                },
                new ResultSetExtractor<Integer>() {
                    @Override
                    public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                        if(rs.next()){
                            return rs.getInt("employee_id");
                        } else {
                            throw new SQLException("Unable to find based on emial");
                        }

                    }
                }

        );
    }
}
