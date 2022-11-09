package com.mettle.exercise.featureflag.repo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


class InMemoryUserFeatureRepoTest {

    private InMemoryUserFeatureRepo repo;

    @BeforeEach
    void setUp() {
        Map<String, Set<String>> sample = new HashMap<>(
                Map.of(
                        "user", new HashSet<>(Set.of("feature1")),
                        "user2", new HashSet<>(Set.of("feature2"))
                )
        );
        repo = new InMemoryUserFeatureRepo(sample);
    }

    @Test
    void shouldGetEnabledSuccessfully() {
        Assertions.assertEquals(Set.of("feature1"), repo.getEnabled("user"));
        Assertions.assertEquals(Set.of(), repo.getEnabled("fakeUser"));
    }

    @Test
    void shouldToggleFeatureSuccessfully() {
        repo.toggleFeature("feature1", "user", false);
        Assertions.assertEquals(Set.of(), repo.getEnabled("user"));
    }

    @Test
    void shouldNotAffectOtherUser() {
        repo.toggleFeature("feature2", "user2", false);
        Assertions.assertEquals(Set.of("feature1"), repo.getEnabled("user"));
        Assertions.assertEquals(Set.of(), repo.getEnabled("user2"));
    }
}