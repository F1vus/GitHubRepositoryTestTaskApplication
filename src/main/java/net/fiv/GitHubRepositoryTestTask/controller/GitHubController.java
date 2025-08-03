package net.fiv.GitHubRepositoryTestTask.controller;

import lombok.extern.slf4j.Slf4j;
import net.fiv.GitHubRepositoryTestTask.dto.RepositoryDto;
import net.fiv.GitHubRepositoryTestTask.service.GitHubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class GitHubController {

    private final GitHubService service;

    public GitHubController(GitHubService service) {
        this.service = service;
    }

    @GetMapping("/repos/{username}")
    public List<RepositoryDto> getRepositories(@PathVariable final String username) {
        log.debug("Requesting repositories for \"{}\"", username);
        return service.getUserRepositories(username);
    }
}

