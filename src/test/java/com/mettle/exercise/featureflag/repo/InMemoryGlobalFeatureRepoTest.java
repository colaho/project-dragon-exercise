package com.mettle.exercise.featureflag.repo;

import com.mettle.exercise.featureflag.exception.DuplicatedFeatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryGlobalFeatureRepoTest {

    private InMemoryGlobalFeatureRepo repo;

    @BeforeEach
    void setUp() {
        Map<String, Boolean> samplePool = new HashMap<>(
                Map.of(
                        "feature1", true,
                        "feature2", true,
                        "feature3", false,
                        "feature4", false,
                        "feature5", true
                )
        );
        repo = new InMemoryGlobalFeatureRepo(samplePool);
    }

    @Test
    void shouldValidateExistenceOfFeature() {
        assertTrue(repo.isFeatureExist("feature1"));
        assertTrue(repo.isFeatureExist("feature3"));
        assertFalse(repo.isFeatureExist("feature9"));
    }

    @Test
    void shouldReturnOnlyEnabledFeatures() {
        Set<String> expected = Set.of("feature1", "feature2", "feature5");
        Assertions.assertEquals(expected, repo.getEnabled());
    }

    @Test
    void shouldAddFeatureSuccessfully() throws DuplicatedFeatureException {
        repo.addFeature("newFeature");
        assertTrue(repo.isFeatureExist("newFeature"));
    }

    @Test
    void shouldThrowDuplicatedException() {
        assertThrows(DuplicatedFeatureException.class, () -> repo.addFeature("feature1"));
    }

    @Test
    void shouldToggleFeatureOnAndOffSuccessfully() {
        repo.toggleFeature("feature3", true);
        Assertions.assertEquals(Set.of("feature1", "feature2", "feature3", "feature5"), repo.getEnabled());
        repo.toggleFeature("feature5", false);
        Assertions.assertEquals(Set.of("feature1", "feature2", "feature3"), repo.getEnabled());
    }
}