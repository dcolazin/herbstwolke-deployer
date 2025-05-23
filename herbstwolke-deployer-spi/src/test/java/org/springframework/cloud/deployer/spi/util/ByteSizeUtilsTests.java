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
package org.springframework.cloud.deployer.spi.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ByteSizeUtils}.
 *
 * @author Eric Bottard
 */
public class ByteSizeUtilsTests {

	@Test
	public void testParse() {
		Assertions.assertEquals(1L, ByteSizeUtils.parseToMebibytes("1"));
		Assertions.assertEquals(2L, ByteSizeUtils.parseToMebibytes("2m"));
		Assertions.assertEquals(20L, ByteSizeUtils.parseToMebibytes("20M"));
		Assertions.assertEquals(1024_000L, ByteSizeUtils.parseToMebibytes("1000g"));
		Assertions.assertEquals(1024L, ByteSizeUtils.parseToMebibytes("1G"));
	}

	@Test
	public void illegalArgumentTest() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> ByteSizeUtils.parseToMebibytes("wat?124"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> ByteSizeUtils.parseToMebibytes("1PB"));
	}

}
