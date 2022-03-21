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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal;

/**
 * Base class for {@link Digests} unit tests.
 *
 * @author Valeriy Vyrva
 */
public abstract class DigestsBaseTest {
  static final byte[] DIGEST_TEST_1_BYTES = { -38, 57, -93, -18, 94, 107, 75, 13, 50, 85, -65, -17, -107, 96, 24, -112, -81, -40,
      7, 9 };
  static final String DIGEST_TEST_1_STR = "DA39A3EE5E6B4B0D3255BFEF95601890AFD80709";
  static final byte[] EXPECTED_MD5_DIGEST = { 58, -63, -81, -94, -88, -101, 126, 79, 24, 102, 80, 40, 119, -65, 29, -59 };
  static final String EXPECTED_MD5_DIGEST_STR = "3AC1AFA2A89B7E4F1866502877BF1DC5";
}
