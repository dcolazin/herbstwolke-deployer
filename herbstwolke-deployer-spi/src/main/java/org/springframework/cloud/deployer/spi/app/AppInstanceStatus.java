/*
 * Copyright 2015-2016 the original author or authors.
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

import java.util.Map;

/**
 * Status for an individual app instance deployment.
 * The underlying instance may be backed by an application context in
 * the JVM, or by a remote process managed by a distributed runtime.
 *
 * @author Patrick Peralta
 * @author Mark Fisher
 */
public interface AppInstanceStatus {

	/**
	 * Return a unique identifier for the deployed app.
	 *
	 * @return identifier for the deployed app
	 */
	String getId();

	/**
	 * Return the state of the deployed app instance.
	 *
	 * @return state of the deployed app instance
	 */
	DeploymentState getState();

	/**
	 * Return a map of attributes for the deployed app instance. The specific
	 * keys/values returned are dependent on the runtime executing the app.
	 * This may include extra information such as deployment location
	 * or specific error messages in the case of failure.
	 *
	 * @return map of attributes for the deployed app
	 */
	Map<String, String> getAttributes();
}
