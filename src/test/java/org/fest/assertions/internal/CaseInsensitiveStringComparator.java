package org.fest.assertions.internal;

import java.util.Comparator;

public class CaseInsensitiveStringComparator implements Comparator<String> {

  public final static CaseInsensitiveStringComparator instance = new CaseInsensitiveStringComparator();

  public int compare(String s1, String s2) {
    if (s1 == null) return s2 == null ? 0 : 1;
    if (s2 == null) return -1;
    return s1.toLowerCase().compareTo(s2.toLowerCase());
  }
}