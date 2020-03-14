/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

/**
 * Interface implemented by all proxies and serves as a type safe setter.
 * One could also define such setters and access them by reflection but with reflection
 * there is a notable performance overhead if the methods need to be located for every call.
 */
public interface AssertJProxySetup {

  void assertj$setup(ProxifyMethodChangingTheObjectUnderTest proxifyMethodChangingTheObjectUnderTest,
                     ErrorCollector errorCollector);
}
