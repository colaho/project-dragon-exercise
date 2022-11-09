package com.mettle.exercise.featureflag.repo;

import com.mettle.exercise.featureflag.exception.DuplicatedFeatureException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InMemoryGlobalFeatureRepo implements GlobalFeatureRepo {

    private final Map<String, Boolean> featurePool ;

    public InMemoryGlobalFeatureRepo() { this(new HashMap<>()); }

    public InMemoryGlobalFeatureRepo(Map<String, Boolean> featurePool) {
        this.featurePool = featurePool;
    }

    @Override
    public boolean isFeatureExist(String name) {
        return featurePool.containsKey(name);
    }

    @Override
    public Set<String> getEnabled() {
        return featurePool.entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public void addFeature(String name) throws DuplicatedFeatureException {
        if (isFeatureExist(name)) {
            throw new DuplicatedFeatureException();
        }
        featurePool.put(name, false);
    }

    @Override
    public void toggleFeature(String name, boolean isEnable) {
        if (featurePool.containsKey(name)) {
            featurePool.put(name, isEnable);
        }
    }
}
