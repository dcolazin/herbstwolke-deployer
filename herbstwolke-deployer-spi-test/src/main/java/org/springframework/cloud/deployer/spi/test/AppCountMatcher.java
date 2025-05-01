/*
 * Copyright 2019 the original author or authors.
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
package org.springframework.cloud.deployer.spi.test;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import org.springframework.cloud.deployer.spi.app.AppDeployer;

/**
 * @author Christian Tzolov
 */
public class AppCountMatcher extends BaseMatcher<String> {

	private final Matcher<Integer> delegate;
	private final AppDeployer appDeployer;
	private Integer appInstanceCount;

	public AppCountMatcher(Matcher<Integer> delegate, AppDeployer appDeployer) {
		this.delegate = delegate;
		this.appDeployer = appDeployer;
	}

	@Override
	public boolean matches(Object actual) {
		appInstanceCount = appDeployer.status((String) actual).getInstances().size();
		return delegate.matches(appInstanceCount);

	}

	@Override
	public void describeMismatch(Object item, Description mismatchDescription) {
		mismatchDescription.appendText("App instance count of ").appendValue(item).appendText(" ");
		delegate.describeMismatch(appInstanceCount, mismatchDescription);
	}

	@Override
	public void describeTo(Description description) {
		delegate.describeTo(description);
	}

	public static AppCountMatcher hasAppCount(Matcher<Integer> delegate, AppDeployer appDeployer) {
		return new AppCountMatcher(delegate, appDeployer);
	}
}
