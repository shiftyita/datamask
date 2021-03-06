package it.shifty.datamask.algorithm.fpe;

import com.idealista.fpe.FormatPreservingEncryption;
import com.idealista.fpe.builder.FormatPreservingEncryptionBuilder;
import com.idealista.fpe.config.GenericDomain;
import com.idealista.fpe.config.GenericTransformations;
import it.shifty.datamask.algorithm.fpe.custom.*;
import it.shifty.datamask.validate.ValidateInput;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.security.SecureRandom;

public class FPE {


  private final String secretKey;
  private final String randomKey;
  private EnumChar enumChar;

  private FormatPreservingEncryption formatPreservingEncryption;

  public FPE(String secretKey, String randomKey) {
    this.secretKey = secretKey;
    this.randomKey = randomKey;
    useDefault();
  }

  public void useDefault() {
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDefaultDomain()
        .withDefaultPseudoRandomFunction(randomKey.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChar = EnumChar.DEFAULT;
  }

  public void useCustomCharset() {
    CustomChar customChar = new CustomChar();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(customChar, new GenericTransformations(customChar.availableCharacters()), new GenericTransformations(
            customChar.availableCharacters())))
        .withDefaultPseudoRandomFunction(randomKey.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChar = EnumChar.CUSTOM;
  }

  public void useAlphaNumericCharset() {
    AlphaNumericChar alphaNumericChar = new AlphaNumericChar();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(alphaNumericChar, new GenericTransformations(alphaNumericChar.availableCharacters()), new GenericTransformations(alphaNumericChar.availableCharacters())))
        .withDefaultPseudoRandomFunction(randomKey.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChar = EnumChar.ALPHANUMERIC;
  }

  public void useEmailCharset() {
    EmailChar emailChar = new EmailChar();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(
            emailChar, new GenericTransformations(emailChar.availableCharacters()), new GenericTransformations(
            emailChar.availableCharacters())))
        .withDefaultPseudoRandomFunction(randomKey.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChar = EnumChar.EMAIL;
  }

  public void useUnicodeCharset() {
    UnicodeChar unicodeChar = new UnicodeChar();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(
            unicodeChar, new GenericTransformations(unicodeChar.availableCharacters()), new GenericTransformations(
            unicodeChar.availableCharacters())))
        .withDefaultPseudoRandomFunction(randomKey.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChar = EnumChar.UNICODE;
  }

  public void useNumericCharset() {
    NumericChar numericChar = new NumericChar();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(
            numericChar, new GenericTransformations(numericChar.availableCharacters()), new GenericTransformations(
            numericChar.availableCharacters())))
        .withDefaultPseudoRandomFunction(randomKey.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChar = EnumChar.NUMBER;
  }

  public void usePhoneCharset() {
    PhoneChar phoneChar = new PhoneChar();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(
            phoneChar, new GenericTransformations(phoneChar.availableCharacters()), new GenericTransformations(
            phoneChar.availableCharacters())))
        .withDefaultPseudoRandomFunction(randomKey.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChar = EnumChar.PHONE;
  }

  public String encryptString(String stringToEncrypt) {
    return formatPreservingEncryption.encrypt(stringToEncrypt, secretKey.getBytes());
  }

  public String decryptString(String stringToDecrypt) {
    return formatPreservingEncryption.decrypt(stringToDecrypt, secretKey.getBytes());
  }

  public Integer encryptInt(Integer integer) {
    return Integer.valueOf(formatPreservingEncryption.encrypt(integer.toString(), secretKey.getBytes()));
  }

  public Integer decryptInt(Integer integer) {
    return Integer.valueOf(formatPreservingEncryption.decrypt(integer.toString(), secretKey.getBytes()));
  }

  public String encryptEmail(String emailToEncrypt) throws ValidationException {
    ValidateInput.validateEmail(emailToEncrypt);
    return encryptPart(emailToEncrypt.split("@")[0]) + "@" + encryptPart(emailToEncrypt.split("@")[1]);
  }

  public String decryptEmail(String emailToDecrypt) {
    return decryptPart(emailToDecrypt.split("@")[0]) + "@" + decryptPart(emailToDecrypt.split("@")[1]);
  }

  public BigDecimal encryptAmount(BigDecimal value) {
    String doubleAsString = String.valueOf(value);
    String leftSide = randomChar() + encryptString(doubleAsString.split("\\.")[0]);
    String rightSide = encryptString(doubleAsString.split("\\.")[1]);

    return new BigDecimal(leftSide + "." + rightSide);
  }

  public BigDecimal decryptAmount(BigDecimal value) {
    String doubleAsString = String.valueOf(value);
    return new BigDecimal(decryptString(doubleAsString.split("\\.")[0].substring(1)) + "." +
        decryptString(doubleAsString.split("\\.")[1]));
  }

  private char randomChar() {
    final String num = "123456789";
    final int N = num.length();

    SecureRandom random = new SecureRandom();
    return num.charAt(random.nextInt(N));
  }

  private String encryptPart(String totalPart) {
    StringBuilder result = new StringBuilder();
    String[] subPart = totalPart.split("\\.");
    for (String part : subPart) {
      result.append(encryptString(part)).append(".");
    }
    return StringUtils.chop(result.toString()); //remove last dot
  }

  private String decryptPart(String totalPart) {
    StringBuilder result = new StringBuilder();
    String[] subPart = totalPart.split("\\.");
    for (String part : subPart) {
      result.append(decryptString(part)).append(".");
    }
    return StringUtils.chop(result.toString());
  }

  public EnumChar getEnumChar() {
    return enumChar;
  }
}
