package com.mass3d.textpattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TextPatternMethodUtils {

  /**
   * Returns a random String based on the format. The format (As specified in TextPatternMethod) can
   * contain '#' digits, 'X' capital letters and 'x' lower case letters.
   *
   * @param random a Random object to generate random numbers
   * @param format the format (as specified in TextPatternMethod)
   * @return the string generated
   */
  public static String generateRandom(Random random, String format) {
    StringBuilder result = new StringBuilder();

    List<Character> uppercase = IntStream.range(0, 26).mapToObj((n) -> (char) (n + 'A'))
        .collect(Collectors.toList());
    List<Character> lowercase = IntStream.range(0, 26).mapToObj((n) -> (char) (n + 'a'))
        .collect(Collectors.toList());
    List<Character> digits = IntStream.range(0, 10).mapToObj((n) -> (char) (n + '0'))
        .collect(Collectors.toList());
    List<Character> all = new ArrayList<>();
    all.addAll(uppercase);
    all.addAll(lowercase);
    all.addAll(digits);

    for (char c : format.toCharArray()) {
      switch (c) {
        case '*':
          result.append(all.get(random.nextInt(all.size())));
          break;
        case '#':
          result.append(digits.get(random.nextInt(digits.size())));
          break;
        case 'X':
          result.append(uppercase.get(random.nextInt(uppercase.size())));
          break;
        case 'x':
          result.append(lowercase.get(random.nextInt(lowercase.size())));
          break;
      }
    }

    return result.toString();
  }

  /**
   * Takes a format (as specified in TextPatternMethod) and attempts to apply it to the text. If
   * there is no match, the method returns null. This can happen if the text don't fit the format:
   * There are more '.' characters than there are characters in the text Both '^' (start) and '$'
   * end characters is present, but there is not an equal amount of '.' as characters in the text
   *
   * @param format the format defined (As specified in TextPatternMethod)
   * @param text the text to perform the format on.
   * @return the formatted text, or null if no match was found.
   */
  public static String formatText(String format, String text) {
    Matcher m = Pattern.compile("(" + format + ")").matcher(text);

    if (m.lookingAt()) {
      return m.group(0);
    }

    return null;
  }
}
