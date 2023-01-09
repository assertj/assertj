package org.assertj.core.api;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.error.KeyStoreShouldContainAlias.shouldContainAlias;
import static org.assertj.core.error.KeyStoreShouldNotContainAlias.shouldNotContainAlias;

public class KeyStoreAssert extends AbstractAssert<KeyStoreAssert, KeyStore> {
  public KeyStoreAssert(KeyStore keyStore) {
    super(keyStore, KeyStoreAssert.class);
  }

  public KeyStoreAssert containsEntry(String alias, Certificate[] certificateChain) {
    isNotNull();

    // FIXME implement and write tests

    return this;
  }

  /**
   * Verifies that the actual {@code KeyStore} contains the specified alias.
   * @param alias the expected alias to find
   * @return {@code this} assertion object.
   */
  public KeyStoreAssert containsAlias(String alias) {
    isNotNull();

    if (!doesContainAlias(actual, alias)) {
      throwAssertionError(shouldContainAlias(alias));
    }

    return this;
  }

  /**
   * Verifies that the actual {@code KeyStore} does not contain the specified alias.
   * @param alias the alias that should be absent
   * @return {@code this} assertion object.
   */
  public KeyStoreAssert doesNotContainAlias(String alias) {
    isNotNull();

    if (doesContainAlias(actual, alias)) {
      throwAssertionError(shouldNotContainAlias(alias));
    }

    return this;
  }

  private static boolean doesContainAlias(KeyStore keyStore, String alias) {
    final boolean doesContainAlias;
    try {
      doesContainAlias = keyStore.containsAlias(alias);
    } catch (KeyStoreException e) {
      // FIXME is there a better unchecked exception type for this?
      throw new RuntimeException(e);
    }
    return doesContainAlias;
  }

  private static Map<String, Certificate[]> keystoreToMap(KeyStore keyStore) {
    final Map<String, Certificate[]> ks = new HashMap<>();

    try {
      final Enumeration<String> aliases = keyStore.aliases();
      while (aliases.hasMoreElements()) {
        final String a = aliases.nextElement();
        final Certificate[] certificateChain = keyStore.getCertificateChain(a);
        ks.put(a, certificateChain);
      }
    } catch (KeyStoreException e) {
      throw new RuntimeException(e);
    }

    return ks;
  }
}
