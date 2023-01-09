package org.assertj.core.api.keystore;

import org.junit.jupiter.api.Test;

import java.security.KeyStore;
import java.security.KeyStoreSpi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.keystore.KeyStoreFactory.newKeyStore;
import static org.assertj.core.error.KeyStoreShouldNotContainAlias.shouldNotContainAlias;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KeyStoreAssert_doesNotContainAlias_Test {

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
    then(assertionError).hasMessage(shouldNotContainAlias("foo").create());
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
