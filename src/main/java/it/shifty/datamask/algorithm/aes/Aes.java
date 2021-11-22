package it.shifty.datamask.algorithm.aes;

import it.shifty.datamask.exception.DecryptException;
import it.shifty.datamask.exception.EncryptException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Aes {


  private SecretKeySpec secretKeySpec;
  private IvParameterSpec ivParameterSpec;
  private Cipher cipher;

  public Aes(String Secret_Key, String IV_Key) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
    secretKeySpec = new SecretKeySpec(Secret_Key.getBytes(StandardCharsets.UTF_8), "AES");
    ivParameterSpec = new IvParameterSpec(IV_Key.getBytes(StandardCharsets.UTF_8));
    cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
  }


  /**
   * Encrypt the string with this internal algorithm.
   *
   * @param plain string object to be encryptString.
   * @return returns encrypted string.
   * @throws EncryptException
   */
  public String encrypt(String plain) throws
      Exception {
    try {
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] encryptedValue = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
      byte[] encodedValue = Base64.getEncoder().encode(encryptedValue);
      return new String(encodedValue, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new EncryptException("ENCRYPT EXCEPTION while encrypting " + plain + " - Details: " + e.toString());
    }
  }

  /**
   * Decrypt this string with the internal algorithm. The passed argument should be encrypted using
   * {@link #encrypt(String) encryptString} method of this class.
   *
   * @param encrypted encrypted string that was encrypted using {@link #encrypt(String) encryptString} method.
   * @return decrypted string.
   * @throws DecryptException
   */
  public String decrypt(String encrypted) throws Exception {
    try {
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] decodedValue = Base64.getDecoder().decode(encrypted.getBytes(StandardCharsets.UTF_8));
      byte[] decryptedValue = cipher.doFinal(decodedValue);
      return new String(decryptedValue, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new DecryptException("DECRYPT EXCEPTION while decrypt " + encrypted + " - Details: " + e.toString());
    }
  }
}
