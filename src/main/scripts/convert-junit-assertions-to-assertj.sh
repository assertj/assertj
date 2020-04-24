#!/bin/bash

function usage() {
  echo
  echo "NAME"
  echo "convert-junit-assertions-to-assertj.sh - Converts most of JUnit assertions to AssertJ assertions"
  echo
  echo "It is difficult to convert ALL JUnit assertions (e.g. the ones that are multiline) but it should work for most of them."
  echo
  echo "SYNOPSIS"
  echo "convert-junit-assertions-to-assertj.sh [Pattern]"
  echo
  echo "OPTIONS"
  echo " -h --help    this help"
  echo " [Pattern]    a find pattern, default to *Test.java if you don't provide a pattern"
  echo "              don't forget to enclose your pattern with double quotes \"\" to avoid pattern to be expanded by your shell prematurely"
  echo
  echo "EXAMPLE"
  echo " convert-junit-assertions-to-assertj.sh \"*IT.java\""
  exit 0
}

if [ "$1" == "-h" ] || [ "$1" == "--help" ] ;
then
  usage
fi

# Handle the different ways of running `sed` without generating a backup file based on OS
# - GNU sed (Linux) uses `-i`
# - BSD sed (macOS) uses `-i ''`
SED_OPTIONS=(-i -e)
case "$(uname)" in
  Darwin*) SED_OPTIONS=(-i "" -e)
esac

FILES_PATTERN=${1:-*Test.java}

# what file do we want to convert ?
MATCHED_FILES=$(find . -name "$FILES_PATTERN")

# sed -E: Interpret regular expressions as extended (modern) regular expressions rather than basic regular expressions (BRE's).
function replace() {
  for file in ${MATCHED_FILES}; do
    sed -E "${SED_OPTIONS[@]}" "$1" "$file"
  done
}

# regular expressions patterns:
# `[^",]*` Match a single character not present in the list `^",`
# ".*[^\]" Match a single character within double quotes, and it can distinguish escaped double quotes
# .*\(.*\) Match a single character within round brackets
echo ''
echo "Converting JUnit assertions to AssertJ assertions in files matching pattern : $FILES_PATTERN"
echo ''
echo ' 1 - Replacing : assertEquals(0, myList.size()) ................. by : assertThat(myList).isEmpty()'
replace 's/assertEquals\((".*[^\]"),[[:blank:]]*0,[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\.size\(\)\)/assertThat(\2).as(\1).isEmpty()/g'
replace 's/assertEquals\([[:blank:]]*0,[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\.size\(\)\)/assertThat(\1).isEmpty()/g'

echo ' 2 - Replacing : assertEquals(expectedSize, myList.size()) ...... by : assertThat(myList).hasSize(expectedSize)'
replace 's/assertEquals\((".*[^\]"),[[:blank:]]*([[:digit:]]*),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\.size\(\)\)/assertThat(\3).as(\1).hasSize(\2)/g'
replace 's/assertEquals\([[:blank:]]*([[:digit:]]*),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\.size\(\)\)/assertThat(\2).hasSize(\1)/g'

echo ' 3 - Replacing : assertEquals(expectedDouble, actual, delta) .... by : assertThat(actual).isCloseTo(expectedDouble, within(delta))'
replace 's/assertEquals\(([^",]*|".*[^\]"|.*\(.*\)),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\)),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\)),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\3).as(\1).isCloseTo(\2, within(\4))/g'
# must be done before assertEquals("description", expected, actual) -> assertThat(actual).as("description").isEqualTo(expected)
# will only replace triplets without double quote to avoid matching : assertEquals("description", expected, actual)
replace 's/assertEquals\([[:blank:]]*([^",]*|".*[^\]"|.*\(.*\)),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\)),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\2).isCloseTo(\1, within(\3))/g'

echo ' 4 - Replacing : assertEquals(expected, actual) ................. by : assertThat(actual).isEqualTo(expected)'
replace 's/assertEquals\((".*[^\]"),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\)),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\3).as(\1).isEqualTo(\2)/g'
replace 's/assertEquals\([[:blank:]]*([^",]*|".*[^\]"|.*\(.*\)),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\2).isEqualTo(\1)/g'

echo ' 5 - Replacing : assertArrayEquals(expectedArray, actual) ....... by : assertThat(actual).isEqualTo(expectedArray)'
replace 's/assertArrayEquals\((".*[^\]"),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\)),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\3).as(\1).isEqualTo(\2)/g'
replace 's/assertArrayEquals\([[:blank:]]*([^",]*|".*[^\]"|.*\(.*\)),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\2).isEqualTo(\1)/g'

echo ' 6 - Replacing : assertNull(actual) ............................. by : assertThat(actual).isNull()'
replace 's/assertNull\((".*[^\]"),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\2).as(\1).isNull()/g'
replace 's/assertNull\([[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\1).isNull()/g'

echo ' 7 - Replacing : assertNotNull(actual) .......................... by : assertThat(actual).isNotNull()'
replace 's/assertNotNull\((".*[^\]"),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\2).as(\1).isNotNull()/g'
replace 's/assertNotNull\([[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\1).isNotNull()/g'

echo ' 8 - Replacing : assertTrue(logicalCondition) ................... by : assertThat(logicalCondition).isTrue()'
replace 's/assertTrue\((".*[^\]"),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\2).as(\1).isTrue()/g'
replace 's/assertTrue\([[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\1).isTrue()/g'

echo ' 9 - Replacing : assertFalse(logicalCondition) .................. by : assertThat(logicalCondition).isFalse()'
replace 's/assertFalse\((".*[^\]"),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\2).as(\1).isFalse()/g'
replace 's/assertFalse\([[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\1).isFalse()/g'

echo '10 - Replacing : assertSame(expected, actual) ................... by : assertThat(actual).isSameAs(expected)'
replace 's/assertSame\((".*[^\]"),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\)),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\3).as(\1).isSameAs(\2)/g'
replace 's/assertSame\([[:blank:]]*([^",]*|".*[^\]"|.*\(.*\)),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\2).isSameAs(\1)/g'

echo '11 - Replacing : assertNotSame(expected, actual) ................ by : assertThat(actual).isNotSameAs(expected)'
replace 's/assertNotSame\((".*[^\]"),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\)),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\3).as(\1).isNotSameAs(\2)/g'
replace 's/assertNotSame\([[:blank:]]*([^",]*|".*[^\]"|.*\(.*\)),[[:blank:]]*([^",]*|".*[^\]"|.*\(.*\))\)/assertThat(\2).isNotSameAs(\1)/g'

echo ''
echo '12 - Replacing JUnit static imports by AssertJ ones, at this point you will probably need to :'
echo '12 --- optimize imports with your IDE to remove unused imports'
echo '12 --- add "import static org.assertj.core.api.Assertions.within;" if you were using JUnit number assertions with deltas'
replace 's/import static org\.junit\.Assert\.assertEquals;/import static org.assertj.core.api.Assertions.assertThat;/g'
replace 's/import static org\.junit\.Assert\.fail;/import static org.assertj.core.api.Assertions.fail;/g'
replace 's/import static org\.junit\.Assert\.\*;/import static org.assertj.core.api.Assertions.*;/g'
echo ''
