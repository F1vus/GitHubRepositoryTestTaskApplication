package net.fiv.GitHubRepositoryTestTask;

import net.fiv.GitHubRepositoryTestTask.dto.BranchDto;
import net.fiv.GitHubRepositoryTestTask.dto.RepositoryDto;
import net.fiv.GitHubRepositoryTestTask.error.ErrorResponse;
import net.fiv.GitHubRepositoryTestTask.model.Branch;
import net.fiv.GitHubRepositoryTestTask.model.Commit;
import net.fiv.GitHubRepositoryTestTask.model.Owner;
import net.fiv.GitHubRepositoryTestTask.model.Repository;

import java.util.List;

public final class TestDataUtil {
    private TestDataUtil(){
    }

    public static Repository createNotForkRepository() {
        return new Repository("repo1"
                , new Owner("testuser")
                , false
        );
    }
    public static Repository createForkRepository() {
        return new Repository("repo2"
                , new Owner("testuser")
                , true
        );
    }

    public static Branch createTestBranchModelA() {
        return new Branch("main"
                , new Commit("abc123")
        );
    }

    public static RepositoryDto createTestRepositoryDto() {
        return new RepositoryDto(
                "repo1",
                "testuser",
                List.of(new BranchDto("main", "abc123"))
        );
    }

    public static ErrorResponse createTestErrorResponse() {
        return new ErrorResponse(404, "GitHub user ' ' not found");
    }
}
