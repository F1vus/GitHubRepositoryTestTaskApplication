package net.fiv.GitHubRepositoryTestTask.integration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import net.fiv.GitHubRepositoryTestTask.TestDataUtil;
import net.fiv.GitHubRepositoryTestTask.dto.RepositoryDto;
import net.fiv.GitHubRepositoryTestTask.error.ErrorResponse;
import net.fiv.GitHubRepositoryTestTask.model.Branch;
import net.fiv.GitHubRepositoryTestTask.model.Repository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.wiremock.spring.EnableWireMock;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@EnableWireMock
public class RouterIntegrationTests {

    private final WebTestClient webClient;

    private static WireMockServer wireMockServer;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    public RouterIntegrationTests(WebTestClient webClient) {
        this.webClient = webClient;
    }

    @BeforeAll
    static void setupWireMock() throws JsonProcessingException {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8081));
        wireMockServer.start();

        List<Repository> notForkedRepository = List.of(
                TestDataUtil.createNotForkRepository(),
                TestDataUtil.createForkRepository()
        );
        String notForkedRepositoryJson = OBJECT_MAPPER.writeValueAsString(notForkedRepository);

        wireMockServer.stubFor(WireMock.get("/users/testuser/repos")
                .willReturn(okJson(notForkedRepositoryJson)));


        List<Branch> branches = List.of(
                TestDataUtil.createTestBranchModelA()
        );
        String branchesJson = OBJECT_MAPPER.writeValueAsString(branches);

        wireMockServer.stubFor(WireMock.get("/repos/testuser/repo1/branches")
                .willReturn(okJson(branchesJson)));


        ErrorResponse errorResponseNotFound = TestDataUtil.createTestErrorResponse();

        String errorResponseNotFoundJson = OBJECT_MAPPER.writeValueAsString(errorResponseNotFound);

        wireMockServer.stubFor(WireMock.get("/users/ExampleUserNotFound/repos")
                .willReturn(okJson(errorResponseNotFoundJson).withStatus(404)));

    }
    @AfterAll
    static void teardownWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void testGetGitHubUserRepositories() throws JsonProcessingException {
        String repositoryJson = webClient.get().uri("/api/repos/testuser")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        List<RepositoryDto> actual = OBJECT_MAPPER.readValue(
                repositoryJson,
                new TypeReference<>() {}
        );

        List<RepositoryDto> expected = List.of(
                TestDataUtil.createTestRepositoryDto()
        );

        assertEquals(expected, actual);
    }

    @Test
    void thenStatus404() throws JsonProcessingException {
        String responseJson = webClient.get().uri("/api/repos/ExampleUserNotFound")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        ErrorResponse expected = OBJECT_MAPPER.readValue(
                responseJson,
                ErrorResponse.class
        );

        ErrorResponse actual = TestDataUtil.createTestErrorResponse();

        assertEquals(expected, actual);
    }


}


