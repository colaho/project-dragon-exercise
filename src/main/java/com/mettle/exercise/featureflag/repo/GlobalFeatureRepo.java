package com.mettle.exercise.featureflag.repo;

import com.mettle.exercise.featureflag.exception.DuplicatedFeatureException;

import java.util.Set;

/**
 * feature-flag-api - com.mettle.exercise.featureflag.repos
 * Created by colaho on 9/11/2022 10:54.
 * -------------------
 */
public interface GlobalFeatureRepo {
    Set<String> getEnabled();
    boolean isFeatureExist(String name);
    void addFeature(String name) throws DuplicatedFeatureException;
    void toggleFeature(String name, boolean isEnable);

}
