package com.mettle.exercise.featureflag.service;

import com.mettle.exercise.featureflag.exception.FeatureNotFoundException;
import com.mettle.exercise.featureflag.repo.GlobalFeatureRepo;
import com.mettle.exercise.featureflag.repo.UserFeatureRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.when;

/**
 * Since the createFeature(), toggleGlobalFeature() and toggleUserFeature() are pretty thin and simple, they
 * are not included in this test class.
 */
@ExtendWith(MockitoExtension.class)
class FeatureServiceTest {

    private FeatureService featureService;
    @Mock
    private GlobalFeatureRepo globalFeatureRepo;
    @Mock
    private UserFeatureRepo userFeatureRepo;

    @BeforeEach
    void setUp() {
        featureService = new FeatureService(globalFeatureRepo, userFeatureRepo);
    }

    @Test
    void shouldReturnEnabledFeaturesForUserCorrectly() {
        when(globalFeatureRepo.getEnabled()).thenReturn(Set.of("feature1", "feature2"));
        when(userFeatureRepo.getEnabled("admin")).thenReturn(Collections.emptySet());
        when(userFeatureRepo.getEnabled("user1")).thenReturn(Set.of("feature2", "feature5"));
        when(userFeatureRepo.getEnabled("user2")).thenReturn(Set.of("feature3"));

        Assertions.assertEquals(Set.of("feature1", "feature2"), featureService.getEnabledFeatures("admin"));
        Assertions.assertEquals(Set.of("feature1", "feature2", "feature5"), featureService.getEnabledFeatures("user1"));
        Assertions.assertEquals(Set.of("feature1", "feature2", "feature3"), featureService.getEnabledFeatures("user2"));
    }

    @Test
    void shouldNotToggleGlobalFeature() {
        when(globalFeatureRepo.isFeatureExist("fakeFeature")).thenReturn(false);

        Assertions.assertThrows(FeatureNotFoundException.class, () -> featureService.toggleGlobalFeature("fakeFeature", true));
    }

    @Test
    void shouldNotToggleUserFeature() {
        when(globalFeatureRepo.isFeatureExist("fakeFeature")).thenReturn(false);

        Assertions.assertThrows(FeatureNotFoundException.class, () -> featureService.toggleUserFeature("fakeFeature", "user", true));
    }
}