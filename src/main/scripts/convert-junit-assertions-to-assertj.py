#!/usr/bin/python3

import re
import sys
import os


def usage():
    str = '''
    NAME
    convert-junit-assertions-to-assertj.py - Converts most of JUnit assertions to AssertJ assertions
    
    It is difficult to convert ALL JUnit assertions (e.g. the ones that are multiline) but it should work for most of them.
    echo
    SYNOPSIS
    python3 convert-junit-assertions-to-assertj.py [Pattern]
    
    OPTIONS
     -h --help    this help
     [Pattern]    a find pattern, default to *Test.java if you don't provide a pattern
                  don't forget to enclose your pattern with double quotes \"\" to avoid pattern to be expanded by your shell prematurely
    echo
    EXAMPLE
     python3 convert-junit-assertions-to-assertj.py \"*Test.java\"
    exit 0
    '''
    print(str)


file_list = []

def search(path=".", pattern="*Test.java"):
    if os.path.isdir(path):
        for item in os.listdir(path):
            item_path = os.path.join(path, item)
            search(item_path, pattern)
    else:
        if(re.search(pattern, path)):
            file_list.append(path)


def replace(file):
    with open(file, "w+") as f:
        print("\nConverting JUnit assertions to AssertJ assertions in files matching pattern : $FILES_PATTERN\n")
        s = f.read()

        print(" 1 - Replacing : assertEquals(0, myList.size()) ................. by : assertThat(myList).isEmpty()")
        s = re.sub(r"assertEquals\((\".*\"),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*0,(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\.size\(\)\)", r"assertThat(\2).as(\1).isEmpty()", s)
        s = re.sub(r"assertEquals\(\s*0,(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\.size\(\)\)\)", r"assertThat(\1).isEmpty()", s)

        print(" 2 - Replacing : assertEquals(expectedSize, myList.size()) ...... by : assertThat(myList).hasSize(expectedSize)")
        s = re.sub(r"assertEquals\((\".*\"),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(\d*),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\s*(.*)\.size\(\)\)", r"assertThat(\3).as(\1).hasSize(\2)", s)
        s = re.sub(r"assertEquals\(\s*(\d*),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\.size\(\)\)", r"ssertThat(\2).hasSize(\1)", s)

        print(" 3 - Replacing : assertEquals(expectedDouble, actual, delta) .... by : assertThat(actual).isCloseTo(expectedDouble, within(delta))")
        s = re.sub(r"assertEquals\((\".*\"),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\)", r"assertThat(\3).as(\1).isCloseTo(\2, within(\4))", s)
        s = re.sub(r"assertEquals\(\s*([^\"]*),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*([^\"]*),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*([^\"]*)\)", r"assertThat(\2).isCloseTo(\1, within(\3))", s)

        print(" 4 - Replacing : assertEquals(expected, actual) ................. by : assertThat(actual).isEqualTo(expected)")
        s = re.sub(r"assertEquals\((\".*\"),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\)", r"assertThat(\3).as(\1).isEqualTo(\2)", s)
        s = re.sub(r"assertEquals\(\s*(.*)\s*,(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\)", r"assertThat(\2).isEqualTo(\1)", s)

        print(" 5 - Replacing : assertArrayEquals(expectedArray, actual) ....... by : assertThat(actual).isEqualTo(expectedArray)")
        s = re.sub(r"assertArrayEquals\((\".*\"),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\)", r"assertThat(\3).as(\1).isEqualTo(\2)", s)
        s = re.sub(r"assertArrayEquals\(\s*(.*)\s*,(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\)", r"assertThat(\2).isEqualTo(\1)", s)

        print(" 6 - Replacing : assertNull(actual) ............................. by : assertThat(actual).isNull()")
        s = re.sub(r"assertNull\((\".*\"),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\)", r"assertThat(\2).as(\1).isNull()", s)
        s = re.sub(r"assertNull\(\s*(.*)\)", r"assertThat(\1).isNull()", s)

        print(" 7 - Replacing : assertNotNull(actual) .......................... by : assertThat(actual).isNotNull()")
        s = re.sub(r"assertNotNull\((\".*\"),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\)", r"assertThat(\2).as(\1).isNotNull()", s)
        s = re.sub(r"assertNotNull\(\s*(.*)\)", r"assertThat(\1).isNotNull()", s)

        print(" 8 - Replacing : assertTrue(logicalCondition) ................... by : assertThat(logicalCondition).isTrue()")
        s = re.sub(r"assertTrue\((\".*\"),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\)", r"assertThat(\2).as(\1).isTrue()", s)
        s = re.sub(r"assertTrue\(\s*(.*)\)", r"assertThat(\1).isTrue()", s)

        print(" 9 - Replacing : assertFalse(logicalCondition) .................. by : assertThat(logicalCondition).isFalse()")
        s = re.sub(r"assertFalse\((\".*\"),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\)", r"assertThat(\2).as(\1).isFalse()", s)
        s = re.sub(r"assertFalse\(\s*(.*)\)", r"assertThat(\1).isFalse()", s)

        print("10 - Replacing : assertSame(expected, actual) ................... by : assertThat(actual).isSameAs(expected")
        s = re.sub(r"assertSame\((\".*\"),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\)", r"assertThat(\3).as(\1).isSameAs(\2)", s)
        s = re.sub(r"assertSame\(\s*(.*),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\)", r"assertThat(\2).isSameAs(\1)", s)

        print("11 - Replacing : assertNotSame(expected, actual) ................ by : assertThat(actual).isNotSameAs(expected)")
        s = re.sub(r"assertNotSame\((\".*\"),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\)", r"assertThat(\3).as(\1).isNotSameAs(\2)", s)
        s = re.sub(r"assertNotSame\(\s*(.*),(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\s*(.*)\)", r"assertThat(\2).isNotSameAs(\1)", s)

        print("\n12 - Replacing JUnit static imports by AssertJ ones, at this point you will probably need to :")
        print("12 --- optimize imports with your IDE to remove unused imports")
        print("12 --- add \"import static org.assertj.core.api.Assertions.within;\" if you were using JUnit number assertions with deltas")
        s = re.sub(r"import static org.junit.Assert.assertEquals;", r"import static org.assertj.core.api.Assertions.assertThat;", s)
        s = re.sub(r"import static org.junit.Assert.fail;", r"import static org.assertj.core.api.Assertions.fail;", s)
        s = re.sub(r"import static org.junit.Assert.\*;", r"import static org.assertj.core.api.Assertions.\*;", s)
        f.write(s)


if __name__ == "__main__":
    if len(sys.argv) == 1:
        search()
    elif len(sys.argv) > 2:
        usage()
        exit(1)
    else:
        arg = sys.argv[1]
        if arg == "-h" or arg == "--help":
            usage()
            exit(0)
        search(pattern=arg)
    for file in file_list:
        replace(file)
