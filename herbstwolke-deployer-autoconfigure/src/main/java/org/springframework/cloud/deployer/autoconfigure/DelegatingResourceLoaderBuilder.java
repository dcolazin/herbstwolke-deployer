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

import java.util.HashMap;
import java.util.Map;

import org.springframework.cloud.deployer.resource.support.DelegatingResourceLoader;
import org.springframework.core.io.ResourceLoader;

/**
 * Builder implementation for a {@link DelegatingResourceLoader} which allows to
 * register resource loaders with schemes before actual instance of a
 * {@link DelegatingResourceLoader} is created.
 *
 * @author Janne Valkealahti
 *
 */
public class DelegatingResourceLoaderBuilder {

	private final Map<String, ResourceLoader> loaders = new HashMap<>();

	/**
	 * Register a map of resource loaders.
	 *
	 * @param loaders the resource loaders to register
	 * @return builder instance for chaining
	 */
	public DelegatingResourceLoaderBuilder loaders(Map<String, ResourceLoader> loaders) {
		this.loaders.putAll(loaders);
		return this;
	}

	/**
	 * register a loader with a scheme.
	 *
	 * @param scheme the scheme
	 * @param loader the resource loader
	 * @return builder instance for chaining
	 */
	public DelegatingResourceLoaderBuilder loader(String scheme, ResourceLoader loader) {
		this.loaders.put(scheme, loader);
		return this;
	}

	/**
	 * Builds a {@link DelegatingResourceLoader}.
	 *
	 * @return the built delegating resource loader
	 */
	public DelegatingResourceLoader build() {
		return new DelegatingResourceLoader(this.loaders);
	}
}
