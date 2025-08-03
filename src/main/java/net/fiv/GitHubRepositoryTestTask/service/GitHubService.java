package net.fiv.GitHubRepositoryTestTask.service;

import lombok.extern.slf4j.Slf4j;
import net.fiv.GitHubRepositoryTestTask.dto.BranchDto;
import net.fiv.GitHubRepositoryTestTask.dto.RepositoryDto;
import net.fiv.GitHubRepositoryTestTask.error.exception.ExternalApiFormatException;
import net.fiv.GitHubRepositoryTestTask.error.exception.NoSuchUserException;
import net.fiv.GitHubRepositoryTestTask.model.Branch;
import net.fiv.GitHubRepositoryTestTask.model.Repository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


@Service
@Slf4j
public class GitHubService {

    private final RestClient restClient;

    public GitHubService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<RepositoryDto> getUserRepositories(final String username) {
        final URI uri = UriComponentsBuilder.fromUriString("/users/" + username + "/repos").build().toUri();

        List<Repository> allRepos = restClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(statusCode -> statusCode.value()==404,(request, response) -> {
                    throw new NoSuchUserException("GitHub user '" + username + "' not found");
                })
                .body(new ParameterizedTypeReference<>() {});

        if(allRepos == null){
            throw new ExternalApiFormatException("GitHub API returned null instead of a list of repositories");
        }
        log.debug("GitHub API successfully return list of repositories with URI: {}", uri);
        return allRepos.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> {
                    var branches = getBranches(repo.owner().login(), repo.name());
                    return new RepositoryDto(repo.name(), repo.owner().login(), branches);
                }).toList();
    }

    private List<BranchDto> getBranches(final String owner, final String repoName) {
        final URI uri = UriComponentsBuilder.fromUriString("/repos/" + owner + "/" + repoName + "/branches").build().toUri();

        List<Branch> response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if(response == null){
            throw new ExternalApiFormatException("GitHub API returned null instead of a list of branches");
        }
        log.debug("GitHub API successfully return list of branches with URI: {}", uri);
        return response.stream()
                .map(branch -> new BranchDto(branch.name(), branch.commit().sha()))
                .toList();
    }
}
