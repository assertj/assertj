package org.assertj.core.api.keystore;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

class KeyStoreFactory {

  /**
   * Return an empty KeyStore backed by the provided KeyStoreSpi for testing purposes.
   */
  static KeyStore newKeyStore(KeyStoreSpi keyStoreSpi) {
    final KeyStore ks = new KeyStore(keyStoreSpi, null, "test"){ };
    try {
      ks.load(null);
    } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
      throw new RuntimeException(e);
    }
    return ks;
  }
}
