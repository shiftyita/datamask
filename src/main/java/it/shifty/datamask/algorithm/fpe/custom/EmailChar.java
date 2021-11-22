package it.shifty.datamask.algorithm.fpe.custom;

import com.idealista.fpe.config.Alphabet;

public class EmailChar implements Alphabet {
  private static final char[] CHARS = new char[]
      {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
      '0','1','2','3','4','5','6','7','8','9'};

  public EmailChar() {
  }

  @Override
  public char[] availableCharacters() {
    return CHARS;
  }

  @Override
  public Integer radix() {
    return CHARS.length;
  }
}