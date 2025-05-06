/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.deployer.spi.cloudfoundry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.matching.AbsentPattern;
import com.github.tomakehurst.wiremock.matching.NotPattern;
import org.cloudfoundry.operations.applications.ApplicationDetail;
import org.cloudfoundry.operations.applications.InstanceDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import reactor.core.publisher.Mono;

import org.springframework.cloud.deployer.spi.app.ActuatorOperations;
import org.springframework.cloud.deployer.spi.app.AppAdmin;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

public class CloudFoundryActuatorTemplateTests extends AbstractAppDeployerTestSupport {

	@RegisterExtension
	static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
		.options(wireMockConfig().globalTemplating(true))
		.build();

	private ActuatorOperations actuatorOperations;
	String appBaseUrl;

	private void setupMockServer() {
		WireMock.stubFor(WireMock.get("/actuator/info")
			.willReturn(WireMock.aResponse()
				.withBody(resourceAsString("actuator-info.json"))
				.withHeader("Content-Type", "application/json")
				.withStatus(HttpStatus.OK.value())));
		WireMock.stubFor(WireMock.get("/actuator/health")
			.willReturn(WireMock.aResponse()
				.withBody("\"status\":\"UP\"}")
				.withHeader("Content-Type", "application/json")
				.withStatus(HttpStatus.OK.value())));
		WireMock.stubFor(WireMock.get("/actuator/bindings")
			.willReturn(WireMock.aResponse()
				.withBody(resourceAsString("actuator-bindings.json"))
				.withHeader("Content-Type", "application/json")
				.withStatus(HttpStatus.OK.value())));
		WireMock.stubFor(WireMock.get("/actuator/bindings/input")
			.willReturn(WireMock.aResponse()
				.withBody(resourceAsString("actuator-binding-input.json"))
				.withHeader("Content-Type", "application/json")
				.withStatus(HttpStatus.OK.value())));
		WireMock.stubFor(WireMock.post("/actuator/bindings/input")
			.withRequestBody(new AbsentPattern(""))
			.willReturn(WireMock.badRequest()));
		WireMock.stubFor(WireMock.post("/actuator/bindings/input")
			.withRequestBody(new NotPattern(new AbsentPattern("")))
			.willReturn(WireMock.aResponse()
				.withBody("{{request.body}}")
				.withHeader("Content-Type", "application/json")
				.withStatus(HttpStatus.OK.value())));
		WireMock.stubFor(WireMock.any(WireMock.urlEqualTo("/actuator/bindings/input"))
			.willReturn(WireMock.badRequest())
			.atPriority(10));
		WireMock.stubFor(WireMock.any(WireMock.anyUrl()).willReturn(WireMock.notFound()).atPriority(20));
	}

	@BeforeEach
	void setupWiremockInfo() {
		setupMockServer();
		commonSetup();
		this.actuatorOperations = new CloudFoundryActuatorTemplate(new RestTemplate(), this.deployer, new AppAdmin());
		this.appBaseUrl = String.format("localhost:%s", wireMockExtension.getPort());
		givenRequestGetApplication("test-application-id", Mono.just(ApplicationDetail.builder()
			.diskQuota(0)
			.id("test-application-id")
			.instances(1)
			.memoryLimit(0)
			.name("test-application")
			.requestedState("RUNNING")
			.runningInstances(1)
			.stack("test-stack")
			.urls(appBaseUrl)
			.instanceDetail(InstanceDetail.builder().state("RUNNING").index("1").build())
			.build()));
	}

	@Test
	void actuatorInfo() {
		Map<String,Object> info = actuatorOperations
				.getFromActuator("test-application-id", "test-application:0", "/info", Map.class);

		assertThat(((Map<?,?>) (info.get("app"))).get("name")).isEqualTo("log-sink-rabbit");
	}

	@Test
	void actuatorBindings() {
		List<?> bindings = actuatorOperations
				.getFromActuator("test-application-id", "test-application:0", "/bindings", List.class);

		assertThat(((Map<?,?>) (bindings.get(0))).get("bindingName")).isEqualTo("input");
	}

	@Test
	void actuatorBindingInput() {
		Map<String, Object> binding = actuatorOperations
				.getFromActuator("test-application-id",  "test-application:0", "/bindings/input", Map.class);
		assertThat(binding.get("bindingName")).isEqualTo("input");
	}

	@Test
	void actuatorPostBindingInput() {
		Map<String, Object> state = actuatorOperations
				.postToActuator("test-application-id",  "test-application:0", "/bindings/input",
						Collections.singletonMap("state", "STOPPED"), Map.class);
		assertThat(state.get("state")).isEqualTo("STOPPED");
	}

	private static String resourceAsString(String path) {
		try {
			return StreamUtils.copyToString(new ClassPathResource(path).getInputStream(), StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
