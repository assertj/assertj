#!/bin/bash

#This script is based on the one which converts JUnit assertions to AssertJ ones.

function usage() {
  echo
  echo "NAME"
  echo "convert-testng-assertions-to-assertj.sh - Converts most of TestNG assertions to AssertJ assertions"
  echo
  echo "It is difficult to convert ALL TestNG assertions (e.g. the ones that are multiline) but it should work for most of them."
  echo
  echo "SYNOPSIS"
  echo "convert-testng-assertions-to-assertj.sh [Pattern]"
  echo
  echo "OPTIONS"
  echo " -h --help    this help"
  echo " [Pattern]    a find pattern, default to *Test.java if you don't provide a pattern"
  echo "              don't forget to enclose your pattern with double quotes \"\" to avoid pattern to be expanded by your shell prematurely"
  echo
  echo "EXAMPLE"
  echo " convert-testng-assertions-to-assertj.sh \"*IT.java\""
  exit 0
}

if [ "$1" == "-h" -o "$1" == "--help" ] ;
then
 usage
fi

SED_OPTIONS=-i
FILES_PATTERN=${1:-*Test.java}
# echo "SED_OPTIONS = ${SED_OPTIONS}"

echo ''
echo "Converting TestNG assertions to AssertJ assertions in files matching pattern : $FILES_PATTERN"
echo ''
echo ' 1 - Replacing : assertEquals(myList.size(), 0) ................. by : assertThat(myList).isEmpty()'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\(.\+\).size(),[[:blank:]]*0,[[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).as(\2).isEmpty();/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\(.\+\).size(),[[:blank:]]*0)[[:blank:]]*;/assertThat(\1).isEmpty();/g' '{}' \;

echo ' 2 - Replacing : assertEquals(myList.size(), expectedSize) ...... by : assertThat(myList).hasSize(expectedSize)'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\(.\+\).size(),[[:blank:]]*\(.\+\)),[[:blank:]]*\([^)]+\))[[:blank:]]*;/assertThat(\1).as(\3).hasSize(\2);/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\(.\+\).size(),[[:blank:]]*\(.\+\)))[[:blank:]]*;/assertThat(\1).hasSize(\2);/g' '{}' \;

echo ' 3 - Replacing : assertEquals(actual, expectedDouble, delta) .... by : assertThat(actual).isCloseTo(expectedDouble, within(delta))'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).as(\4).isCloseTo(\2, within(\3));/g' '{}' \;
# must be done before assertEquals(actual, expected, "description") -> assertThat(actual).as("description").isEqualTo(expected) 
# will only replace triplets without double quote to avoid matching : assertEquals(actual, expected, "description")
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\([^"]\+\),[[:blank:]]*\([^"]\+\),[[:blank:]]*\([^"]\+\))[[:blank:]]*;/assertThat(\1).isCloseTo(\2, within(\3));/g' '{}' \;

echo ' 4 - Replacing : assertEquals(actual, expected) ................. by : assertThat(actual).isEqualTo(expected)'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).as(\3).isEqualTo(\2);/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).isEqualTo(\2);/g' '{}' \;

echo ' 5 - Replacing : assertNull(actual) ............................. by : assertThat(actual).isNull()'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNull([[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).as(\2).isNull();/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNull([[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).isNull();/g' '{}' \;

echo ' 6 - Replacing : assertNotNull(actual) .......................... by : assertThat(actual).isNotNull()'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNotNull([[:blank:]]*\(.\+\), [[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).as(\2).isNotNull();/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNotNull([[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).isNotNull();/g' '{}' \;

echo ' 7 - Replacing : assertTrue(logicalCondition) ................... by : assertThat(logicalCondition).isTrue()'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertTrue([[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).as(\2).isTrue();/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertTrue([[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).isTrue();/g' '{}' \;

echo ' 8 - Replacing : assertFalse(logicalCondition) .................. by : assertThat(logicalCondition).isFalse()'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertFalse([[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).as(\2).isFalse();/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertFalse([[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).isFalse();/g' '{}' \;

echo '9 - Replacing : assertSame(actual, expected) ................... by : assertThat(actual).isSameAs(expected)'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertSame([[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).as(\3).isSameAs(\2);/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertSame([[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).isSameAs(\2);/g' '{}' \;

echo '10 - Replacing : assertNotSame(actual, expected) ................ by : assertThat(actual).isNotSameAs(expected)'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNotSame([[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\1).as(\3).isNotSameAs(\2);/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNotSame([[:blank:]]*\(.\+\),[[:blank:]]*\(.\+\))[[:blank:]]*;/assertThat(\2).isNotSameAs(\1);/g' '{}' \;

echo ''
echo '11 - Replacing TestNG static imports by AssertJ ones, at this point you will probably need to :'
echo '11 --- optimize imports with your IDE to remove unused imports'
echo '11 --- add "import static org.assertj.core.api.Assertions.within;" if you were using TestNG number assertions with deltas'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/import static org.testng.Assert.assertEquals;/import static org.assertj.core.api.Assertions.assertThat;/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/import static org.testng.Assert.fail;/import static org.assertj.core.api.Assertions.fail;/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/import static org.testng.Assert.\*;/import static org.assertj.core.api.Assertions.*;/g' '{}' \;
echo ''