package net.fiv.GitHubRepositoryTestTask.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Branch(String name, Commit commit) { }
