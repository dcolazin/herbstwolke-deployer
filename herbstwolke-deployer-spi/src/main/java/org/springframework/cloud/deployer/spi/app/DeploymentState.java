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

/**
 * Deployment states for apps and groups. These may represent the
 * state of:
 * <ul>
 *   <li>an entire group of apps</li>
 *   <li>the global state of a deployed app as part of a group</li>
 *   <li>the state of a particular instance of an app, in cases where
 *   {@code app.count > 1}</li>
 * </ul>
 *
 * @author Patrick Peralta
 * @author Eric Bottard
 * @author Mark Fisher
 */
public enum DeploymentState {

	/**
	 * The app or group is being deployed. If there are multiple apps or
	 * app instances, at least one of them is still being deployed.
	 */
	deploying,

	/**
	 * All apps have been successfully deployed.
	 */
	deployed,

	/**
	 * The app or group is known to the system, but is not currently deployed.
	 */
	undeployed,

	/**
	 * In the case of multiple apps, some have successfully deployed, while
	 * others have not. This state does not apply for individual app instances.
	 */
	partial,

	/**
	 * All apps have failed deployment.
	 */
	failed,

	/**
	 * A system error occurred trying to determine deployment status.
	 */
	error,

	/**
	 * The app or group deployment is not known to the system.
	 */
	unknown;

}
