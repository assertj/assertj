package org.assertj.core.api.recursive;

public class FieldsIntrospectionConfiguration {

  private boolean ignoreTransientFields;

  public FieldsIntrospectionConfiguration(boolean ignoreTransientFields) {
    this.ignoreTransientFields = ignoreTransientFields;
  }

  public boolean shouldIgnoreTransientFields() {
    return ignoreTransientFields;
  }

  public void ignoreTransientFields() {
    ignoreTransientFields = true;
  }

}
