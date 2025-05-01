/*
 * Copyright 2018 the original author or authors.
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
package org.springframework.cloud.deployer.autoconfigure;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.deployer.resource.maven.MavenProperties;
import org.springframework.cloud.deployer.resource.maven.MavenResourceLoader;
import org.springframework.cloud.deployer.resource.support.DelegatingResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ResourceLoader;

/**
 * Autoconfiguration of a file or Maven based {@link ResourceLoader}.
 *
 * @author Michael Minella
 * @author Janne Valkealahti
 */
@Configuration
public class ResourceLoadingAutoConfiguration {

	@Configuration
	@ConditionalOnClass(MavenResourceLoader.class)
	@EnableConfigurationProperties(MavenConfigurationProperties.class)
	public static class MavenResourceLoaderConfig {

		@Bean
		@Order(0)
		public DelegatingResourceLoaderBuilderCustomizer mavenDelegatingResourceLoaderBuilderCustomizer(MavenProperties mavenProperties) {
			return customizer -> customizer.loader("maven", new MavenResourceLoader(mavenProperties));
		}
	}

	@Configuration
	@ConditionalOnMissingBean(DelegatingResourceLoader.class)
	public static class DelegatingResourceLoaderConfig {

		private final ObjectProvider<DelegatingResourceLoaderBuilderCustomizer> loaderBuilderCustomizers;

		public DelegatingResourceLoaderConfig(
				ObjectProvider<DelegatingResourceLoaderBuilderCustomizer> loaderBuilderCustomizers) {
			this.loaderBuilderCustomizers = loaderBuilderCustomizers;
		}

		@Bean
		public DelegatingResourceLoader delegatingResourceLoader() {
			DelegatingResourceLoaderBuilder builder = new DelegatingResourceLoaderBuilder();
			this.loaderBuilderCustomizers.orderedStream().forEach(customizer -> customizer.customize(builder));
			return builder.build();
		}
	}
}
