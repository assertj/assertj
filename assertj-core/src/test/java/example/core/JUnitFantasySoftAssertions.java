/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.core;

// @formatter:off

import org.assertj.core.api.JUnitSoftAssertions;

// tag::user_guide[]
// we extend JUnitSoftAssertions to get standard soft assertions classes
public class JUnitFantasySoftAssertions extends JUnitSoftAssertions
                                   implements TolkienSoftAssertionsProvider, GoTSoftAssertionsProvider {}
// end::user_guide[]
// @formatter:on
