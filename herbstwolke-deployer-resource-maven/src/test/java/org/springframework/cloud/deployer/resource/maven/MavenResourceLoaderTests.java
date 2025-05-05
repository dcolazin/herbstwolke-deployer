/*
 * Copyright 2016 the original author or authors.
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

/**
 * Tests for the {@link MavenResourceLoader}.
 *
 * @author Mark Fisher
 */
public class MavenResourceLoaderTests {

	@Test
	public void verifyCoordinates() {
		String location = "maven://foo:bar:1.0.1";
		MavenResourceLoader loader = new MavenResourceLoader(new MavenProperties());
		Resource resource = loader.getResource(location);
		Assertions.assertEquals(MavenResource.class, resource.getClass());
		MavenResource mavenResource = (MavenResource) resource;
		Assertions.assertEquals("foo", mavenResource.getGroupId());
		Assertions.assertEquals("bar", mavenResource.getArtifactId());
		Assertions.assertEquals("1.0.1", mavenResource.getVersion());
	}

	@Test
	public void invalidPrefix() {
		MavenResourceLoader loader = new MavenResourceLoader(new MavenProperties());
		Assertions.assertThrows(IllegalArgumentException.class, () -> loader.getResource("foo://bar"));
	}

}
