package org.assertj.core.util;

import java.util.Comparator;

public class CaseInsensitiveCharacterComparator implements Comparator<Character> {

  public final static CaseInsensitiveCharacterComparator instance = new CaseInsensitiveCharacterComparator();

  @Override
  public int compare(Character c1, Character c2) {
    return c1.toString().toLowerCase().compareTo(c2.toString().toLowerCase());
  }
}