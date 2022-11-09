package com.mettle.exercise.featureflag.repo;


import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryUserFeatureRepo implements UserFeatureRepo {

    private final Map<String, Set<String>> featurePool;

    public InMemoryUserFeatureRepo() {
        this(new HashMap<>());
    }

    public InMemoryUserFeatureRepo(Map<String, Set<String>> featurePool) {
        this.featurePool = featurePool;
    }

    @Override
    public Set<String> getEnabled(String userId) {
        return featurePool.getOrDefault(userId, Collections.emptySet());
    }

    @Override
    public void toggleFeature(String name, String user, boolean isEnable) {
        featurePool.compute(user, (k, v) -> {
            if (v == null) {
                v = new HashSet<>();
            }
            if (isEnable) {
                v.add(name);
            } else {
                v.remove(name);
            }
            return v;
        });
    }
}
