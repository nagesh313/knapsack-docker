package com.api.knapsack.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
public class KnapSack {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String taskId;
    private Status status;
    @OneToOne(cascade = CascadeType.ALL)
    private Timestamps timeStamps;
    @OneToOne(cascade = CascadeType.ALL)
    private Solution solution = null;
    @OneToOne(cascade = CascadeType.ALL)
    private Problem problem;
}
