package com.mettle.exercise.featureflag.service;

import com.mettle.exercise.featureflag.exception.DuplicatedFeatureException;
import com.mettle.exercise.featureflag.exception.FeatureNotFoundException;
import com.mettle.exercise.featureflag.repo.GlobalFeatureRepo;
import com.mettle.exercise.featureflag.repo.UserFeatureRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FeatureService {

    private final GlobalFeatureRepo globalFeatureRepo;
    private final UserFeatureRepo userFeatureRepo;

    public void createFeature(String name) throws DuplicatedFeatureException {
        globalFeatureRepo.addFeature(name);
    }

    public Set<String> getEnabledFeatures(String user) {
        Set<String> enabled = new HashSet<>(globalFeatureRepo.getEnabled());
        enabled.addAll(userFeatureRepo.getEnabled(user));
        return enabled;
    }

    public void toggleGlobalFeature(String name, boolean isEnabled) throws FeatureNotFoundException {
        if (globalFeatureRepo.isFeatureExist(name)) {
            globalFeatureRepo.toggleFeature(name, isEnabled);
        } else {
            throw new FeatureNotFoundException();
        }
    }

    public void toggleUserFeature(String name, String user, boolean isEnabled) throws FeatureNotFoundException {
        if (globalFeatureRepo.isFeatureExist(name)) {
            userFeatureRepo.toggleFeature(name, user, isEnabled);
        } else {
            throw new FeatureNotFoundException();
        }
    }
}
