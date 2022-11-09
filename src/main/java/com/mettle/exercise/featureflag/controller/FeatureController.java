package com.mettle.exercise.featureflag.controller;

import com.mettle.exercise.featureflag.dto.FeatureRequest;
import com.mettle.exercise.featureflag.exception.DuplicatedFeatureException;
import com.mettle.exercise.featureflag.exception.FeatureNotFoundException;
import com.mettle.exercise.featureflag.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
public class FeatureController {

    private final FeatureService featureService;

    @PostMapping("/feature")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFeature(@RequestBody @Valid FeatureRequest req) throws DuplicatedFeatureException {
        featureService.createFeature(req.getName());
    }

    @PutMapping("/feature")
    @ResponseStatus(HttpStatus.OK)
    public void toggleFeature(@RequestBody @Valid FeatureRequest req) throws FeatureNotFoundException {
        if (ObjectUtils.isEmpty(req.getUser())) {
            featureService.toggleGlobalFeature(req.getName(), req.isEnabled());
        } else {
            featureService.toggleUserFeature(req.getName(), req.getUser(), req.isEnabled());
        }
    }

    @GetMapping("/feature")
    public Set<String> getEnableFeatures(Principal principal) {
        return featureService.getEnabledFeatures(principal.getName());
    }

}
