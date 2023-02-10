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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.keystore;

import org.junit.jupiter.api.Test;

import java.security.KeyStore;
import java.security.KeyStoreSpi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.test.KeyStores.newKeyStore;
import static org.assertj.core.error.KeyStoreShouldNotContainAlias.keystoreShouldNotContainAlias;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KeyStoreAssert_doesNotContainAlias_Test {

  @Test
  void should_fail_if_keystore_contains_alias() {
    // GIVEN
    KeyStoreSpi keyStoreSpiMock = mock(KeyStoreSpi.class);
    when(keyStoreSpiMock.engineContainsAlias(anyString())).thenReturn(true);
    // AND
    KeyStore actual = newKeyStore(keyStoreSpiMock);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainAlias("foo"));
    // THEN
    then(assertionError).hasMessage(keystoreShouldNotContainAlias("foo").create());
  }

  @Test
  void should_pass_if_keystore_does_not_contain_alias() {
    // GIVEN
    KeyStoreSpi keyStoreSpiMock = mock(KeyStoreSpi.class);
    when(keyStoreSpiMock.engineContainsAlias(anyString())).thenReturn(false);
    // AND
    KeyStore actual = newKeyStore(keyStoreSpiMock);
    // WHEN/THEN
    then(actual).doesNotContainAlias("foo");
  }
}
