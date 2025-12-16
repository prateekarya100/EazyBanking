package com.tomcatdevs.Accounts.controller;

import com.tomcatdevs.Accounts.entity.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class FormSubmitController {

    @PostMapping("/employee-data")
    public ResponseEntity<String> saveEmployee(@RequestBody Employee employee) {
        System.out.println(employee);
        return ResponseEntity.ok("Received: " + employee.getFirstName());
    }

}
