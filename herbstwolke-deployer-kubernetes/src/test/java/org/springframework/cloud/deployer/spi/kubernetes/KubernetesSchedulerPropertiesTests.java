/*
 * Copyright 2018-2021 the original author or authors.
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

package org.springframework.cloud.deployer.spi.kubernetes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.util.StringUtils;

/**
 * Tests for {@link KubernetesSchedulerProperties}.
 *
 * @author Chris Schaefer
 */
public class KubernetesSchedulerPropertiesTests {

	@Test
	public void testImagePullPolicyDefault() {
		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
		Assertions.assertNotNull(kubernetesSchedulerProperties.getImagePullPolicy(), "Image pull policy should not be null");
		Assertions.assertEquals(ImagePullPolicy.IfNotPresent, kubernetesSchedulerProperties.getImagePullPolicy(), "Invalid default image pull policy");
	}

	@Test
	public void testImagePullPolicyCanBeCustomized() {
		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
		kubernetesSchedulerProperties.setImagePullPolicy(ImagePullPolicy.Never);
		Assertions.assertNotNull(kubernetesSchedulerProperties.getImagePullPolicy(), "Image pull policy should not be null");
		Assertions.assertEquals(ImagePullPolicy.Never, kubernetesSchedulerProperties.getImagePullPolicy(), "Unexpected image pull policy");
	}

	@Test
	public void testRestartPolicyDefault() {
		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
		Assertions.assertNotNull(kubernetesSchedulerProperties.getRestartPolicy(), "Restart policy should not be null");
		Assertions.assertEquals(RestartPolicy.Never, kubernetesSchedulerProperties.getRestartPolicy(), "Invalid default restart policy");
	}

	@Test
	public void testRestartPolicyCanBeCustomized() {
		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
		kubernetesSchedulerProperties.setRestartPolicy(RestartPolicy.OnFailure);
		Assertions.assertNotNull(kubernetesSchedulerProperties.getRestartPolicy(), "Restart policy should not be null");
		Assertions.assertEquals(RestartPolicy.OnFailure, kubernetesSchedulerProperties.getRestartPolicy(), "Unexpected restart policy");
	}

	@Test
	public void testEntryPointStyleDefault() {
		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
		Assertions.assertNotNull(kubernetesSchedulerProperties.getEntryPointStyle(), "Entry point style should not be null");
		Assertions.assertEquals(EntryPointStyle.exec, kubernetesSchedulerProperties.getEntryPointStyle(), "Invalid default entry point style");
	}

	@Test
	public void testEntryPointStyleCanBeCustomized() {
		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
		kubernetesSchedulerProperties.setEntryPointStyle(EntryPointStyle.shell);
		Assertions.assertNotNull(kubernetesSchedulerProperties.getEntryPointStyle(), "Entry point style should not be null");
		Assertions.assertEquals(EntryPointStyle.shell, kubernetesSchedulerProperties.getEntryPointStyle(), "Unexpected entry point stype");
	}

	@Test
	public void testNamespaceDefault() {
		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
		if (kubernetesSchedulerProperties.getNamespace() == null) {
			kubernetesSchedulerProperties.setNamespace("default");

			Assertions.assertTrue(StringUtils.hasText(kubernetesSchedulerProperties.getNamespace()), "Namespace should not be empty or null");
			Assertions.assertEquals("default", kubernetesSchedulerProperties.getNamespace(), "Invalid default namespace");
		}
	}

	@Test
	public void testNamespaceCanBeCustomized() {
		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
		kubernetesSchedulerProperties.setNamespace("myns");
		Assertions.assertTrue(StringUtils.hasText(kubernetesSchedulerProperties.getNamespace()), "Namespace should not be empty or null");
		Assertions.assertEquals("myns", kubernetesSchedulerProperties.getNamespace(), "Unexpected namespace");
	}

	@Test
	public void testImagePullSecretDefault() {
		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
		Assertions.assertNull(kubernetesSchedulerProperties.getImagePullSecret(), "No default image pull secret should be set");
	}

	@Test
	public void testImagePullSecretCanBeCustomized() {
		String secret = "mysecret";
		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
		kubernetesSchedulerProperties.setImagePullSecret(secret);
		Assertions.assertNotNull(kubernetesSchedulerProperties.getImagePullSecret(), "Image pull secret should not be null");
		Assertions.assertEquals(secret, kubernetesSchedulerProperties.getImagePullSecret(), "Unexpected image pull secret");
	}

	@Test
	public void testEnvironmentVariablesDefault() {
		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
		Assertions.assertEquals(0, kubernetesSchedulerProperties.getEnvironmentVariables().length, "No default environment variables should be set");
	}

	@Test
	public void testEnvironmentVariablesCanBeCustomized() {
		String[] envVars = new String[] { "var1=val1", "var2=val2" };
		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
		kubernetesSchedulerProperties.setEnvironmentVariables(envVars);
		Assertions.assertNotNull(kubernetesSchedulerProperties.getEnvironmentVariables(), "Environment variables should not be null");
		Assertions.assertEquals(2, kubernetesSchedulerProperties.getEnvironmentVariables().length, "Unexpected number of environment variables");
	}

	@Test
	public void testTaskServiceAccountNameDefault() {
		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
		Assertions.assertNotNull(kubernetesSchedulerProperties.getTaskServiceAccountName(), "Task service account name should not be null");
		Assertions.assertEquals(KubernetesSchedulerProperties.DEFAULT_TASK_SERVICE_ACCOUNT_NAME, kubernetesSchedulerProperties.getTaskServiceAccountName(), "Unexpected default task service account name");
	}

	@Test
	public void testTaskServiceAccountNameCanBeCustomized() {
		String taskServiceAccountName = "mysa";
		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
		kubernetesSchedulerProperties.setTaskServiceAccountName(taskServiceAccountName);
		Assertions.assertNotNull(kubernetesSchedulerProperties.getTaskServiceAccountName(), "Task service account name should not be null");
		Assertions.assertEquals(taskServiceAccountName, kubernetesSchedulerProperties.getTaskServiceAccountName(), "Unexpected task service account name");
	}

	// Re-implement when we have a proper env binding via boot
	// @RunWith(PowerMockRunner.class)
	// @PrepareForTest({ KubernetesSchedulerProperties.class })
	// public static class EnvTests {
	// 	@Test
	// 	public void testNamespaceFromEnvironment() throws Exception {
	// 		PowerMockito.mockStatic(System.class);
	// 		PowerMockito.when(System.getenv(KubernetesSchedulerProperties.ENV_KEY_KUBERNETES_NAMESPACE))
	// 				.thenReturn("nsfromenv");
	// 		KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();
	// 		assertTrue("Namespace should not be empty or null",
	// 				StringUtils.hasText(kubernetesSchedulerProperties.getNamespace()));
	// 		assertEquals("Unexpected namespace from environment", "nsfromenv",
	// 				kubernetesSchedulerProperties.getNamespace());
	// 	}
	// }
}
