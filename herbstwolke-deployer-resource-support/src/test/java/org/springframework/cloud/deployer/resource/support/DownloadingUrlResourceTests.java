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
package org.springframework.cloud.deployer.resource.support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author Mark Pollack
 */
public class DownloadingUrlResourceTests {

	@Test
	public void test() throws Exception {
		DownloadingUrlResource httpResource = new DownloadingUrlResource("https://repo1.maven.org/maven2/org/springframework/cloud/stream/app/file-sink-rabbit/3.2.1/file-sink-rabbit-3.2.1.jar");
		File file1 = httpResource.getFile();
		File file2 = httpResource.getFile();
		Assertions.assertEquals(file1, file2);
		Assertions.assertEquals("81a23583726958052fdf75c399b81a3c4fcab6d1-filesinkrabbit321jar", file1.getName());
	}
}
