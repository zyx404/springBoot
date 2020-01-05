package com.example.interceptor.service.impl;

import com.example.interceptor.service.DemoService;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello() {
        return "hello from service layer";
    }
}
