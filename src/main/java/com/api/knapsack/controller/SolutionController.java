package com.api.knapsack.controller;

import com.api.knapsack.model.KnapSack;
import com.api.knapsack.service.KnapsackSolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/knapsack")
public class SolutionController {
    @Autowired
    KnapsackSolverService knapsackSolverService;

    @PostMapping
    public KnapSack solve(@RequestBody KnapSack request) {
        return knapsackSolverService.submit(request);
    }

    @GetMapping("/{id}")
    public KnapSack getSolution(@PathVariable String id) {
        return knapsackSolverService.getKnapSack(id);
    }
}
