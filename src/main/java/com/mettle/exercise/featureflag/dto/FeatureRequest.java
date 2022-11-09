package com.mettle.exercise.featureflag.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureRequest {

    @NotBlank
    @NotNull
    @JsonProperty("name")
    private String name;

    @JsonProperty("enabled")
    private boolean enabled;

    @JsonProperty("user")
    private String user;
}
