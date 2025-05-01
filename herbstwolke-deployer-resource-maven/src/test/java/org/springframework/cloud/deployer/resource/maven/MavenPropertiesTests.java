/*
 * Copyright 2020 the original author or authors.
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
package org.springframework.cloud.deployer.resource.maven;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cloud.deployer.resource.maven.MavenProperties.WagonHttpMethod;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

public class MavenPropertiesTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

	@Test
	public void testDefaults() {
		this.contextRunner
			.withUserConfiguration(Config1.class)
			.run((context) -> {
				MavenProperties properties = context.getBean(MavenProperties.class);
				assertThat(properties.isUseWagon()).isFalse();
			});
	}

	@Test
	public void testPreemtiveEnabled() {
		this.contextRunner
			.withInitializer(context -> {
				Map<String, Object> map = new HashMap<>();
				map.put("maven.use-wagon", "true");
				context.getEnvironment().getPropertySources().addLast(new SystemEnvironmentPropertySource(
					StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, map));
			})
			.withUserConfiguration(Config1.class)
			.run((context) -> {
				MavenProperties properties = context.getBean(MavenProperties.class);
				assertThat(properties.isUseWagon()).isTrue();
			});
	}

	@Test
	public void testRemoteRepositories() {
		this.contextRunner
			.withInitializer(context -> {
				Map<String, Object> map = new HashMap<>();
				map.put("maven.remote-repositories.repo1.url", "url1");
				map.put("maven.remote-repositories.repo1.wagon.http.all.use-preemptive", "true");
				map.put("maven.remote-repositories.repo1.wagon.http.all.use-default-headers", "true");
				map.put("maven.remote-repositories.repo1.wagon.http.all.connection-timeout", "2");
				map.put("maven.remote-repositories.repo1.wagon.http.all.read-timeout", "3");
				map.put("maven.remote-repositories.repo1.wagon.http.all.headers.header1", "value1");
				map.put("maven.remote-repositories.repo1.wagon.http.all.params.http.connection.timeout", "1");
				context.getEnvironment().getPropertySources().addLast(new SystemEnvironmentPropertySource(
					StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, map));
			})
			.withUserConfiguration(Config1.class)
			.run((context) -> {
				MavenProperties properties = context.getBean(MavenProperties.class);
				assertThat(properties.getRemoteRepositories().size()).isEqualTo(1);
				assertThat(properties.getRemoteRepositories()).containsOnlyKeys("repo1");
				assertThat(properties.getRemoteRepositories().get("repo1").getWagon().getHttp().size())
					.isEqualTo(1);
				assertThat(properties.getRemoteRepositories().get("repo1").getWagon().getHttp()
					.get(WagonHttpMethod.all).isUsePreemptive()).isTrue();
				assertThat(properties.getRemoteRepositories().get("repo1").getWagon().getHttp()
					.get(WagonHttpMethod.all).isUseDefaultHeaders()).isTrue();
				assertThat(properties.getRemoteRepositories().get("repo1").getWagon().getHttp()
					.get(WagonHttpMethod.all).getConnectionTimeout()).isEqualTo(2);
				assertThat(properties.getRemoteRepositories().get("repo1").getWagon().getHttp()
					.get(WagonHttpMethod.all).getReadTimeout()).isEqualTo(3);
				assertThat(properties.getRemoteRepositories().get("repo1").getWagon().getHttp()
					.get(WagonHttpMethod.all).getHeaders()).containsOnlyKeys("header1");
				assertThat(properties.getRemoteRepositories().get("repo1").getWagon().getHttp()
					.get(WagonHttpMethod.all).getHeaders().get("header1")).isEqualTo("value1");
				assertThat(properties.getRemoteRepositories().get("repo1").getWagon().getHttp()
					.get(WagonHttpMethod.all).getParams()).containsOnlyKeys("http.connection.timeout");
				assertThat(properties.getRemoteRepositories().get("repo1").getWagon().getHttp()
					.get(WagonHttpMethod.all).getParams().get("http.connection.timeout")).isEqualTo("1");
				});
	}

	@EnableConfigurationProperties({ MavenConfigurationProperties.class })
	private static class Config1 {
	}

	@ConfigurationProperties(prefix = "maven")
	public static class MavenConfigurationProperties extends MavenProperties {
	}
}
