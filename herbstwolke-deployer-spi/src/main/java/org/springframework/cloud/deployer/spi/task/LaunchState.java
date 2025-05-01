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

package org.springframework.cloud.deployer.spi.task;

/**
 * Launch states for a Task.
 *
 * @author Mark Fisher
 */
public enum LaunchState {

	/**
	 * The task launch has been requested, but it is not yet known to be in a running state.
	 */
	launching,

	/**
	 * The task has been successfully launched, but has not yet completed.
	 */
	running,

	/**
	 * The task has been cancelled.
	 */
	cancelled,

	/**
	 * The task completed execution.
	 */
	complete,

	/**
	 * The task failed to launch.
	 */
	failed,

	/**
	 * A system error occurred trying to determine launch status.
	 */
	error,

	/**
	 * The task is not known to the system.
	 */
	unknown;

}
