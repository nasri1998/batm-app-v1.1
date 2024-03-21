package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.RequestDemo;
import com.example.demo.handler.CustomResponse;
import com.example.demo.model.Demo;
import com.example.demo.model.Region;
import com.example.demo.repository.DemoRepository;

@RestController
@RequestMapping("api")
public class DemoRestController {

    @Autowired
    private DemoRepository demoRepository;

    @GetMapping("demos")
    public ResponseEntity<Object> get() {
        return CustomResponse.generate(HttpStatus.OK, "Data Successfully Fetched", demoRepository.findAll());
    }

    @PostMapping("demo")
    public ResponseEntity<Object> save(@RequestBody Demo demo) {
        Demo result = demoRepository.save(demo);
        if (demoRepository.findById(result.getId()).isPresent()) {
            return CustomResponse.generate(HttpStatus.OK, "Data Successfully Added");
        }

        return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Error Adding Data");
    }

    @PostMapping("demo/{id}")
    public ResponseEntity<Object> editDemo(@RequestBody RequestDemo requestDemo,
            @PathVariable(required = true) Integer id) {
        Demo demo = demoRepository.findDemo(id);

        if (requestDemo.getImage().equals("") || requestDemo.getLabel().equals("")) {
            return CustomResponse.generate(HttpStatus.BAD_REQUEST, "check your input");
        }
        if (demo == null) {
            return CustomResponse.generate(HttpStatus.OK, "not found");
        }

        demo.setImage(requestDemo.getImage());
        demo.setLabel(requestDemo.getLabel());

        demoRepository.save(demo);

        return CustomResponse.generate(HttpStatus.OK, "Data Successfully Updated Data");
    }

    @DeleteMapping("demo/{id}")
    public ResponseEntity<Object> delete(@PathVariable(required = true, name = "id") Integer id) {
        demoRepository.deleteById(id);
        return CustomResponse.generate(HttpStatus.OK, "id berhasil dihapus",
                !demoRepository.findById(id).isPresent());
    }
}
