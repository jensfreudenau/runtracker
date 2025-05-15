package com.runnertracker.controller;

import com.runnertracker.model.Run;
import com.runnertracker.service.RunService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/runs")
public class RunController {

    private final RunService runService;

    public RunController(RunService runService) {
        this.runService = runService;
    }

    @GetMapping
    public String listRuns(Model model) {
        model.addAttribute("runs", runService.findAll());
        return "runs/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("run", new Run());
        return "runs/form";
    }

    @PostMapping
    public String saveRun(@ModelAttribute Run run) {
        runService.save(run);
        return "redirect:/runs";
    }

    @GetMapping("/edit/{id}")
    public String editRun(@PathVariable Long id, Model model) {
        model.addAttribute("run", runService.findById(id));
        return "runs/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteRun(@PathVariable Long id) {
        runService.deleteById(id);
        return "redirect:/runs";
    }
}
