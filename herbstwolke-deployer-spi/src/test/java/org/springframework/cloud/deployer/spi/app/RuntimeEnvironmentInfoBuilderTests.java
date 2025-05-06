/*
 * Copyright 2017 the original author or authors.
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

package org.springframework.cloud.deployer.spi.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.deployer.spi.core.RuntimeEnvironmentInfo;
import org.springframework.cloud.deployer.spi.util.RuntimeVersionUtils;
import org.springframework.core.SpringVersion;

/**
 * Tests for constructing a {@link RuntimeEnvironmentInfo}
 */
public class RuntimeEnvironmentInfoBuilderTests {

	@Test
	public void testCreatingRuntimeEnvironmentInfo() {
		RuntimeEnvironmentInfo rei = new RuntimeEnvironmentInfo.Builder()
				.spiClass(AppDeployer.class)
				.implementationName("TestDeployer")
				.implementationVersion("1.0.0")
				.platformClientVersion("1.2.0")
				.platformHostVersion("1.1.0")
				.platformType("Test")
				.platformApiVersion("1")
				.addPlatformSpecificInfo("foo", "bar")
				.build();
		Assertions.assertEquals(RuntimeVersionUtils.getVersion(AppDeployer.class), rei.getSpiVersion());
		Assertions.assertEquals("TestDeployer", rei.getImplementationName());
		Assertions.assertEquals("1.0.0", rei.getImplementationVersion());
		Assertions.assertEquals("Test", rei.getPlatformType());
		Assertions.assertEquals("1", rei.getPlatformApiVersion());
		Assertions.assertEquals("1.2.0", rei.getPlatformClientVersion());
		Assertions.assertEquals("1.1.0", rei.getPlatformHostVersion());
		Assertions.assertEquals(System.getProperty("java.version"), rei.getJavaVersion());
		Assertions.assertEquals(SpringVersion.getVersion(), rei.getSpringVersion());
		Assertions.assertEquals(RuntimeVersionUtils.getSpringBootVersion(), rei.getSpringBootVersion());
		Assertions.assertEquals("bar", rei.getPlatformSpecificInfo().get("foo"));
	}
}
