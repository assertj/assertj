package org.assertj.core.internal.objects.data;

import java.util.ArrayList;
import java.util.List;

public class FriendlyPerson extends Person {
  public List<FriendlyPerson> friends = new ArrayList<>();
}