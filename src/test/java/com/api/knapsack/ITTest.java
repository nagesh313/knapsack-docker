package com.api.knapsack;

import com.api.knapsack.model.KnapSack;
import com.api.knapsack.model.Problem;
import com.api.knapsack.model.Status;
import com.api.knapsack.service.KnapsackSolverService;
import com.google.ortools.Loader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = {KnapsackApplication.class})
@ContextConfiguration(classes = Loader.class)
public class ITTest {
    @Autowired
    KnapsackSolverService knapsackSolverService;

    KnapSack request;
    long weights[] = {10, 20L, 33L};
    long values[] = {10, 20L, 33L};
    int capacity = 60;

    @Before
    public void init() {
        request = new KnapSack();
        Problem problem = new Problem();
        problem.setCapacity(capacity);
        problem.setWeights(weights);
        problem.setKnapsack_values(values);
        request.setProblem(problem);
    }

    @Test
    public void testKnapsackSubmission() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:8080/knapsack";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<KnapSack> entity = restTemplate.postForEntity(uri, request, KnapSack.class);
        assertThat(entity.getStatusCode().toString()).isEqualTo("200 OK");
        assertThat(entity.getBody().getProblem()).isNotNull();
        assertThat(entity.getBody().getSolution()).isNull();
        assertThat(entity.getBody().getTaskId()).isNotNull();
        assertThat(entity.getBody().getStatus()).isEqualTo(Status.submitted);
        assertThat(entity.getBody().getTimeStamps()).isNotNull();
        assertThat(entity.getBody().getTimeStamps().getSubmitted()).isNotNull();
        assertThat(entity.getBody().getTimeStamps().getCompleted()).isNull();
        assertThat(entity.getBody().getTimeStamps().getStarted()).isNull();
        assertThat(entity.getBody().getProblem().getCapacity()).isEqualTo(60);
        assertThat(entity.getBody().getProblem().getKnapsack_values()).isEqualTo(values);
        assertThat(entity.getBody().getProblem().getWeights()).isEqualTo(weights);
    }
}