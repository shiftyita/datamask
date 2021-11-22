package it.shifty.datamask.fpe;

import it.shifty.datamask.algorithm.fpe.FPE;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.equalTo;

class FPE_ImplTest {

    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @Nested
    class test {

        @Test
        public void check_decryption() {
            String iv_key = "12Cd#94qpz!%4/(0";
            String secretKey = "353fwafwg3214qaa";

            String secret = "abcaefoza";

            FPE fpe = new FPE(secretKey, iv_key);
            String secretEncrypted = fpe.encryptString(secret);
            String secretDecrypted = fpe.decryptString(secretEncrypted);

            MatcherAssert.assertThat(secretDecrypted, equalTo(secret));
        }

        @Test
        public void check_decryption_with_spaces() {
            String iv_key = "12Cd#94qpz!%4/(0";
            String secretKey = "353fwafwg3ad21414";

            String secret = "LAST day of JUNE";

            FPE fpe = new FPE(secretKey, iv_key);
            fpe.useCustomCharset();
            String secretEncrypted = fpe.encryptString(secret);
            String secretDecrypted = fpe.decryptString(secretEncrypted);

            MatcherAssert.assertThat(secretDecrypted, CoreMatchers.is(secret));
        }

        @Test
        public void check_decryption_with_samechars() {
            String iv_key = "12Cd#94qpz!%4/(0";
            String secretKey = "353fwafwg3ad21414";

            String secret = "aaaaaa";

            FPE fpe = new FPE(secretKey, iv_key);
            fpe.useCustomCharset();
            String secretEncrypted = fpe.encryptString(secret);
            String secretDecrypted = fpe.decryptString(secretEncrypted);

            MatcherAssert.assertThat(secretDecrypted, CoreMatchers.is(secret));
        }

        @Test
        public void check_length() {
            String iv_key = "12Cd#94qpz!%4/(0";
            String secretKey = "353fwafwg3ad21414";

            String secret = "abcdefghijklmopqrstuvzABCV";

            FPE fpe = new FPE(secretKey, iv_key);
            fpe.useCustomCharset();
            String secretEncrypted = fpe.encryptString(secret);

            MatcherAssert.assertThat(secretEncrypted.length(), CoreMatchers.is(secret.length()));
        }

        @Test
        public void check_decryption_with_alphanum() {
            String iv_key = "12Cd#94qpz!%4/(0";
            String secretKey = "353fwafwg3ad21414";

            String secret = "a13caBAZ";

            FPE fpe = new FPE(secretKey, iv_key);
            fpe.useUnicodeCharset();
            String secretEncrypted = fpe.encryptString(secret);
            String secretDecrypted = fpe.decryptString(secretEncrypted);

            MatcherAssert.assertThat(secretDecrypted, CoreMatchers.is(secret));
        }

        @Test
        public void check_decryption_with_email() {
            String iv_key = "12Cd#94qpz!%4/(0";
            String secretKey = "353fwafwg3ad21414";

            String secret = "tarapia.tapioco@unknowmail.com";

            FPE fpe = new FPE(secretKey, iv_key);
            fpe.useEmailCharset();
            String secretEncrypted = fpe.encryptEmail(secret);
            String secretDecrypted = fpe.decryptEmail(secretEncrypted);

            MatcherAssert.assertThat(secretDecrypted, CoreMatchers.is(secret));
        }

        @Test
        public void check_email_encrypted_valid_format() {
            String iv_key = "12Cd#94qpz!%4/(0";
            String secretKey = "353fwafwg3ad21414";

            String secret = "tarapia.tapioco@unknowmail.com";

            final Pattern VALID_EMAIL_ADDRESS_REGEX =
                    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

            FPE fpe = new FPE(secretKey, iv_key);
            fpe.useEmailCharset();
            String secretEncrypted = fpe.encryptEmail(secret);

            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(secretEncrypted);
            MatcherAssert.assertThat(matcher.find(), CoreMatchers.is(true));
        }

        @Test
        public void check_decryption_with_number() {
            String iv_key = "12Cd#94qpz!%4/(0";
            String secretKey = "353fwafwg3ad21414";

            String secret = "123456789";

            FPE fpe = new FPE(secretKey, iv_key);
            fpe.useNumericCharset();
            String secretEncrypted = fpe.encryptString(secret);
            String secretDecrypted = fpe.decryptString(secretEncrypted);

            MatcherAssert.assertThat(secretDecrypted, CoreMatchers.is(secret));
        }

        @Test
        public void check_decryption_with_decimal() {
            String iv_key = "ssdkF$HUy2A#D%kd";
            String secretKey = "weJiSEvR5yAC5ftB";

            String secret = "852.91";

            FPE fpe = new FPE(secretKey, iv_key);
            fpe.useNumericCharset();
            BigDecimal secretEncrypted = fpe.encryptAmount(new BigDecimal(secret));
            BigDecimal secretDecrypted = fpe.decryptAmount(secretEncrypted);

            MatcherAssert.assertThat(secretDecrypted, CoreMatchers.is(new BigDecimal(secret)));
        }

        @Test
        public void check_decryption_with_decimal_2() {
            String iv_key = "ssdkF$HUy2A#D%kd";
            String secretKey = "weJiSEvR5yAC5ftB";

            String secret = "089.001";

            FPE fpe = new FPE(secretKey, iv_key);
            fpe.useNumericCharset();
            BigDecimal secretEncrypted = fpe.encryptAmount(new BigDecimal(secret));
            BigDecimal secretDecrypted = fpe.decryptAmount(secretEncrypted);

            MatcherAssert.assertThat(secretDecrypted, CoreMatchers.is(new BigDecimal(secret)));
        }

        public void printAllAlphaChars() {
            char[] CHARS = new char[91];
            //iterate through 65535 chars
            for (int i = 32; i <= 122; i++) {
                CHARS[i - 32] = (char) i;
                System.out.println("CHAR " + (i - 32) + ":" + CHARS[i - 32]);
            }
        }
    }

}