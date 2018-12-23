package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.util.Strings.join;

import java.util.List;

final class DualKey {

  private final List<String> path;
  final Object key1;
  final Object key2;

  DualKey(List<String> path, Object key1, Object key2) {
    this.path = path;
    this.key1 = key1;
    this.key2 = key2;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof DualKey)) {
      return false;
    }

    DualKey that = (DualKey) other;
    return key1 == that.key1 && key2 == that.key2;
  }

  @Override
  public int hashCode() {
    int h1 = key1 != null ? key1.hashCode() : 0;
    int h2 = key2 != null ? key2.hashCode() : 0;
    return h1 + h2;
  }

  @Override
  public String toString() {
    return "DualKey [key1=" + key1 + ", key2=" + key2 + "]";
  }

  public List<String> getPath() {
    return path;
  }

  public String getConcatenatedPath() {
    return join(path).with(".");
  }
}