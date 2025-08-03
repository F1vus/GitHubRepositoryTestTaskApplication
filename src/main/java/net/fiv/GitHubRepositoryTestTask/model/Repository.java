package net.fiv.GitHubRepositoryTestTask.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Repository(String name, Owner owner, boolean fork) { }
