package org.assertj.core.util;

public interface Doctor {
  default String getDegree() {
	return "PhD";
  }
}
