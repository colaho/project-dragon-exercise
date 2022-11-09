package com.mettle.exercise.featureflag.repo;

import java.util.Set;


public interface UserFeatureRepo {
    Set<String> getEnabled(String user);
    void toggleFeature(String name, String user, boolean isEnable);
}
