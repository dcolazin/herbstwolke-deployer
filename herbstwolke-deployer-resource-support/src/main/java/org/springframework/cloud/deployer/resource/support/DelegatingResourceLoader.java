/*
 * Copyright 2016-2018 the original author or authors.
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

package org.springframework.cloud.deployer.resource.support;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

/**
 * A {@link ResourceLoader} implementation that delegates to other {@link ResourceLoader} instances
 * that are stored in a Map with their associated URI schemes as the keys. If a scheme does not
 * exist within the Map, it will fallback to a {@link DefaultResourceLoader}.
 * The Map may be empty (or {@literal null}).
 *
 * @author Mark Fisher
 * @author Janne Valkealahti
 * @author Ilayaperumal Gopinathan
 */
public class DelegatingResourceLoader implements ResourceLoader, ResourceLoaderAware {

	private final ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

	private final Map<String, ResourceLoader> loaders;

	private ResourceLoader defaultResourceLoader = new DefaultResourceLoader();

	/**
	 * Instantiates a new delegating resource loader.
	 */
	public DelegatingResourceLoader() {
		this(null);
	}

	/**
	 * Instantiates a new delegating resource loader.
	 *
	 * @param loaders the loaders
	 */
	public DelegatingResourceLoader(Map<String, ResourceLoader> loaders) {
		this.loaders = CollectionUtils.isEmpty(loaders)
				? Collections.<String, ResourceLoader>emptyMap()
				: Collections.unmodifiableMap(loaders);
	}

	@Override
	public void setResourceLoader(ResourceLoader contextResourceLoader) {
		if (contextResourceLoader != null && contextResourceLoader != this) {
			this.defaultResourceLoader = contextResourceLoader;
		}
	}

	@Override
	public Resource getResource(String location) {
		try {
			URI uri = new URI(location);
			String scheme = uri.getScheme();
			Assert.notNull(scheme, "a scheme (prefix) is required");
			ResourceLoader loader = this.loaders.get(scheme);
			if (loader == null) {
				if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) {
					loader = new DownloadingUrlResourceLoader();
				}
				else {
					loader = this.defaultResourceLoader;
				}
			}
			return loader.getResource(location);
		}
		catch (Exception e) {
			throw new ResourceNotResolvedException(e.getMessage(), e);
		}

	}

	@Override
	public ClassLoader getClassLoader() {
		return this.classLoader;
	}

	/**
	 * Gets a map of configured loaders.
	 *
	 * @return the loaders
	 */
	public Map<String, ResourceLoader> getLoaders() {
		return loaders;
	}
}
