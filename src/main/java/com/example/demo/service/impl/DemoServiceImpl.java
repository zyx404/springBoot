package com.example.demo.service.impl;

import com.example.demo.service.DemoService;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello() {
        return "hi friends ";
    }
}
