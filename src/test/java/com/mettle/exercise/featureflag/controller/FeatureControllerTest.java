package com.mettle.exercise.featureflag.controller;

import com.mettle.exercise.featureflag.FeatureFlagApiApplication;
import com.mettle.exercise.featureflag.dto.FeatureRequest;
import com.mettle.exercise.featureflag.exception.DuplicatedFeatureException;
import com.mettle.exercise.featureflag.exception.FeatureNotFoundException;
import com.mettle.exercise.featureflag.service.FeatureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FeatureFlagApiApplication.class)
class FeatureControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FeatureService featureService;

    @Test
    @WithAnonymousUser
    void shouldBlockAnonymousUserToCreateFeature() {
        webTestClient.post()
                .uri("/feature")
                .body(Mono.just(new FeatureRequest("feature1", false, null)), FeatureRequest.class)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void shouldCreateFeatureSuccessfully() throws DuplicatedFeatureException {
        doNothing().when(featureService).createFeature(anyString());

        webTestClient.post()
                .uri("/feature")
                .body(Mono.just(new FeatureRequest("feature1", false, null)), FeatureRequest.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @WithAnonymousUser
    void shouldBlockAnonymousUserToEnableFeature() {
        webTestClient.put()
                .uri("/feature")
                .body(Mono.just(new FeatureRequest("feature1", true, "user1")), FeatureRequest.class)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void shouldBlockNormalUserToEnableFeature() {
        webTestClient.put()
                .uri("/feature")
                .body(Mono.just(new FeatureRequest("feature1", true, "user1")), FeatureRequest.class)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void shouldEnableFeatureSuccessfully() throws FeatureNotFoundException {
        doNothing().when(featureService).toggleGlobalFeature(anyString(), anyBoolean());

        webTestClient.put()
                .uri("/feature")
                .body(Mono.just(new FeatureRequest("feature1", false, null)), FeatureRequest.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void shouldGetFeatureSuccessfullyForAdmin() {
        when(featureService.getEnabledFeatures(anyString())).thenReturn(Set.of("feature1"));

        webTestClient.get()
                .uri("/feature")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void shouldGetFeatureSuccessfullyForUser() {
        when(featureService.getEnabledFeatures(anyString())).thenReturn(Set.of("feature1"));

        webTestClient.get()
                .uri("/feature")
                .exchange()
                .expectStatus().isOk();
    }
}