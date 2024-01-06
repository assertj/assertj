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
package org.assertj.core.test;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class KeyStores {

  /**
   * Create an empty KeyStore which has no password, for testing purposes.
   */
  public static KeyStore newKeyStore() {
    try {
      final KeyStore keyStore = KeyStore.getInstance("PKCS12");
      keyStore.load(null);
      return keyStore;
    } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Create a KeyStore backed by the provided KeyStoreSpi.
   * You will typically mock the passed-in KeyStoreSpi object, to make testing the KeyStore API easier.
   */
  public static KeyStore newKeyStore(KeyStoreSpi keyStoreSpi) {
    final KeyStore ks = new KeyStore(keyStoreSpi, null, "test"){ };
    try {
      ks.load(null);
    } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
      throw new RuntimeException(e);
    }
    return ks;
  }
}
