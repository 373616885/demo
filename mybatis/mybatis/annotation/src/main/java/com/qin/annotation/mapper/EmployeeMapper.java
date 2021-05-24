package com.qin.annotation.mapper;


import com.qin.annotation.bean.Employee;


public interface EmployeeMapper {

    Employee getEmpById(Integer id);

    void insertEmp(Employee employee);
}
