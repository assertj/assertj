package org.example.custom;


import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.util.VisibleForTesting;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.assertj.core.internal.Maps;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.data.MapEntry.entry;

public class map_test extends MapsBaseTest {

  @VisibleForTesting
  Maps maps = Maps.instance();


  // ============================= MY TEST HERE ===================================================

  @Test
  public void should_pass_if_case_insensitive_actual_contains_given_entries_in_rder() {
    // GIVEN
    Map actual = new CaseInsensitiveMap<>();
    actual.put( "color", "green" );
    actual.put( "Name", "yoda" );

    maps.assertContainsExactly(info, actual, array(entry("Color", "green"), entry("name", "yoda")));
  }

  @Test
  public void should_pass_more_keys() {
    // GIVEN

    Map actual = new CaseInsensitiveMap();
    // {color=green, dog=dog_val, aaa=aaa_val, cat=cat_val, name=yoda}
    actual.put( "Color", "green" );
    actual.put( "name", "yoda" );
    actual.put( "DOG", "dog_val" );
    actual.put( "cat", "cat_val" );
    actual.put( "AAA", "aaa_val" );

    maps.assertContainsExactly(info, actual, array(entry("color", "green"), entry("name", "yoda"), entry("dog", "dog_val"), entry("cat", "cat_val"), entry("aaa", "aaa_val")));
    // maps.assertContainsExactly(info, actual, expected.entrySet().toArray());
  }


  @Test
  public void empty_map() {
    // GIVEN
    Map actual = new CaseInsensitiveMap();
    maps.assertContainsExactly(info, actual, array());
  }

  @Test
  public void expected_captalized() {
    // GIVEN

    Map actual = new CaseInsensitiveMap();
    actual.put( "color", "green" );
    actual.put( "name", "yoda" );
    actual.put( "dog", "dog_val" );
    actual.put( "cat", "cat_val" );
    actual.put( "aaa", "aaa_val" );

    maps.assertContainsExactly(info, actual, array(entry("COlor", "green"), entry("NAME", "yoda"), entry("DOG", "dog_val"), entry("CAT", "cat_val"), entry("aaa", "aaa_val")));
    // maps.assertContainsExactly(info, actual, expected.entrySet().toArray());
  }

  @Test
  public void regular_pass() {
    // GIVEN
    Map actual = new LinkedHashMap<>();
    actual.put( "color", "green" );
    actual.put( "name", "yoda" );
    actual.put( "dog", "dog_val" );
    actual.put( "cat", "cat_val" );
    actual.put( "aaa", "aaa_val" );


    maps.assertContainsExactly(info, actual, array(entry("color", "green"), entry("name", "yoda"), entry("dog", "dog_val"), entry("cat", "cat_val"), entry("aaa", "aaa_val")));
  }

}


