package org.assertj.core.error;

public class KeyStoreShouldContainAlias extends BasicErrorMessageFactory {

  public KeyStoreShouldContainAlias(String alias) {
    super("Expected KeyStore to contain alias <%s>, but it did not", alias);
  }

  public static KeyStoreShouldContainAlias shouldContainAlias(String alias) {
    return new KeyStoreShouldContainAlias(alias);
  }
}
