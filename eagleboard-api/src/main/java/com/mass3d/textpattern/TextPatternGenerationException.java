package com.mass3d.textpattern;

public class TextPatternGenerationException
    extends Exception {

  public TextPatternGenerationException(String message) {
    super("Could not generate value: " + message);
  }
}
