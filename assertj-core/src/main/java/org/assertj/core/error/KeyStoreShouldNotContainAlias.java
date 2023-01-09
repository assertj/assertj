package org.assertj.core.error;

public class KeyStoreShouldNotContainAlias extends BasicErrorMessageFactory {

  public KeyStoreShouldNotContainAlias(String alias) {
    super("Expected KeyStore not to contain alias <%s>, but it did", alias);
  }

  public static KeyStoreShouldNotContainAlias shouldNotContainAlias(String alias) {
    return new KeyStoreShouldNotContainAlias(alias);
  }
}
