package com.api.knapsack.repository;

import com.api.knapsack.model.KnapSack;
import com.api.knapsack.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnapsackRepository extends JpaRepository<KnapSack, String> {
    public List<KnapSack> findAllByStatus(Status status);
}
