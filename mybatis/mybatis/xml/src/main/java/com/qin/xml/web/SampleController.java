package com.qin.xml.web;


import com.qin.xml.bean.Employee;
import com.qin.xml.mapper.EmployeeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
public class SampleController {

    private final EmployeeMapper employeeMapper;

    @GetMapping("/emp/{id}")
    @Transactional
    public Employee getEmp(@PathVariable("id") Integer id){
        return employeeMapper.getEmpById(id);
    }

    @GetMapping("/emp2/{id}")
    @Transactional
    public Employee getEmp2(@PathVariable("id") Integer id){
        return employeeMapper.getEmpById(id);
    }

}
