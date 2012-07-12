package org.fest.assertions.util;

import java.util.Comparator;

public class CaseInsensitiveStringComparator implements Comparator<String> {

  public final static CaseInsensitiveStringComparator instance = new CaseInsensitiveStringComparator();

  public int compare(String s1, String s2) {

    return s1.toLowerCase().compareTo(s2.toLowerCase());
  }
}