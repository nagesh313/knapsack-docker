package com.api.knapsack.service;

import com.api.knapsack.repository.KnapsackRepository;
import com.api.knapsack.model.KnapSack;
import com.api.knapsack.model.Solution;
import com.api.knapsack.model.Status;
import com.api.knapsack.model.Timestamps;
import com.google.ortools.Loader;
import com.google.ortools.algorithms.KnapsackSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class KnapsackSolverService {

    @Autowired
    KnapsackRepository knapsackRepository;

    public KnapSack submit(KnapSack request) {
        request.setProblem(request.getProblem());
        request.setStatus(Status.submitted);
        request.setTimeStamps(new Timestamps());
        knapsackRepository.save(request);
        return request;
    }

    public KnapSack getKnapSack(String id) {
        Optional<KnapSack> knapSack = knapsackRepository.findById(id);
        if (knapSack.isPresent()) {
            return knapSack.get();
        } else {
            return null;
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void solve() {
        List<KnapSack> submittedTasks = knapsackRepository.findAllByStatus(Status.submitted);
        for (KnapSack task : submittedTasks) {
            task.setStatus(Status.started);
            task.getTimeStamps().setStarted(new Date());
            knapsackRepository.save(task);
            process(task);
        }
    }

    public void process(KnapSack task) {
        Loader.loadNativeLibraries();
        KnapsackSolver solver = new KnapsackSolver(
                KnapsackSolver.SolverType.KNAPSACK_DYNAMIC_PROGRAMMING_SOLVER, "test");
        long[][] weights = new long[1][task.getProblem().getWeights().length];
        weights[0] = task.getProblem().getWeights();
        long[] values = task.getProblem().getKnapsack_values();
        long[] capacities = new long[1];
        capacities[0] = task.getProblem().getCapacity();
        solver.init(
                values,
                weights,
                capacities);
        final long computedValue = solver.solve();
        ArrayList<Integer> packedItems = new ArrayList<>();
        ArrayList<Long> packedWeights = new ArrayList<>();
        long totalValue = 0L;
        for (int i = 0; i < values.length; i++) {
            if (solver.bestSolutionContains(i)) {
                packedItems.add(i);
                packedWeights.add(weights[0][i]);
                totalValue = (totalValue + weights[0][i]);
            }
        }
        System.out.println("Task ID : " + task.getTaskId());
        System.out.println("Total Value: " + totalValue);
        System.out.println("Packed items: " + packedItems);
        System.out.println("Packed weights: " + packedWeights);
        Solution solution = new Solution(null, packedItems, totalValue);
        task.setStatus(Status.completed);
        task.getTimeStamps().setCompleted(new Date());
        task.setSolution(solution);
        knapsackRepository.save(task);
    }
}

