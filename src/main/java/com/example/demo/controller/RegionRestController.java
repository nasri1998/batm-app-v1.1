package com.example.demo.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Region;
import com.example.demo.repository.ParameterRepository;
import com.example.demo.repository.RegionRepository;

@RestController
@RequestMapping("api")
public class RegionRestController {
    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ParameterRepository parameterRepository;

    @GetMapping("regions")
    public List<Region> get(){
        return regionRepository.findAll();
    }

    @PostMapping("region")
    public boolean newRegion(@RequestBody Region newRegion,@RequestHeader(name = "x-token") String token){
        if (token.equals(parameterRepository.findById("x-token").get().getValue())) {
            return true;
        }
        return false;
        
    }
    @GetMapping("region/{id}")
    public Region getId(@PathVariable(required = true)Integer id ){        
        return regionRepository.findById(id).orElse(null);
    }
    @DeleteMapping("region/{id}")
    public boolean delete(@PathVariable(required = true, name = "id")Integer id){
        regionRepository.deleteById(id);
        return !regionRepository.findById(id).isPresent();
    }
    
}
