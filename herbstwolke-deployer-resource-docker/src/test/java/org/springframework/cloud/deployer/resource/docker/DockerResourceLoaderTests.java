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

package org.springframework.cloud.deployer.resource.docker;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

/**
 * Tests for the {@link DockerResourceLoader}.
 *
 * @author Thomas Risberg
 */
public class DockerResourceLoaderTests {

	@Test
	public void verifyImageUri() throws IOException {
		String location = "docker:springcloud/test-app:v1";
		DockerResourceLoader loader = new DockerResourceLoader();
		Resource resource = loader.getResource(location);
		Assertions.assertEquals(DockerResource.class, resource.getClass());
		DockerResource dockerResource = (DockerResource) resource;
		Assertions.assertEquals(location, dockerResource.getURI().toString());
		Assertions.assertEquals("springcloud/test-app:v1", dockerResource.getURI().getSchemeSpecificPart());
		Assertions.assertEquals("docker", dockerResource.getURI().getScheme());
	}

	@Test
	public void verifyImageUriWithSlashes() throws IOException {
		String location = "docker://springcloud/test-app:v1";
		DockerResourceLoader loader = new DockerResourceLoader();
		Resource resource = loader.getResource(location);
		Assertions.assertEquals(DockerResource.class, resource.getClass());
		DockerResource dockerResource = (DockerResource) resource;
		Assertions.assertEquals("docker:springcloud/test-app:v1", dockerResource.getURI().toString());
		Assertions.assertEquals("springcloud/test-app:v1", dockerResource.getURI().getSchemeSpecificPart());
		Assertions.assertEquals("docker", dockerResource.getURI().getScheme());
	}

	@Test
	public void verifyImageUriWithoutPrefix() throws IOException {
		String location = "springcloud/test-app:v1";
		DockerResourceLoader loader = new DockerResourceLoader();
		Resource resource = loader.getResource(location);
		Assertions.assertEquals(DockerResource.class, resource.getClass());
		DockerResource dockerResource = (DockerResource) resource;
		Assertions.assertEquals(DockerResource.URI_SCHEME + ":" + location, dockerResource.getURI().toString());
		Assertions.assertEquals("springcloud/test-app:v1", dockerResource.getURI().getSchemeSpecificPart());
		Assertions.assertEquals("docker", dockerResource.getURI().getScheme());
	}

}
