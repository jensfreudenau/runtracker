package com.runnertracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/runs")
public class RunController {

    @GetMapping
    public String runsPage() {
        return "runs";
    }
}
