package it.shifty.datamask.aes;

import it.shifty.datamask.algorithm.aes.Aes;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;

class AesTest {

    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @Nested
    class test {

        @Test
        public void check_decryption()
                throws Exception {
            String iv_key = "12Cd#94qpz!%4/(0";
            String secret_key = "353fwafwg3plofmv";

            String secret = "this is a phrase to be encryptString";

            Aes aes = new Aes(iv_key, secret_key);
            String secretEncrypted = aes.encrypt(secret);
            String secretDecrypted = aes.decrypt(secretEncrypted);

            MatcherAssert.assertThat(secretDecrypted, is(secret));

        }

        @Test
        public void check_length()
                throws Exception {
            String iv_key = "12Cd#94qpz!%4/(0"; //16 length
            String secret_key = "353fwafwg3plofmv"; //16 length

            String secret = StringUtils.repeat("*", 1);

            Aes aes = new Aes(iv_key, secret_key);
            String secretEncrypted = aes.encrypt(secret);
            String secretDecrypted = aes.decrypt(secretEncrypted);

            MatcherAssert.assertThat(secretDecrypted, is(secret));
            System.out.println("secret length: " + secret.length());
            System.out.println("secretEncrypted length: " + secretEncrypted.length());

        }

        @Test
        public void check_length_2()
                throws Exception {
            String iv_key = StringUtils.repeat("*", 16);
            String secret_key = StringUtils.repeat("*", 16);

            String secret = StringUtils.repeat("*", 150);

            Aes aes = new Aes(iv_key, secret_key);
            String secretEncrypted = aes.encrypt(secret);
            String secretDecrypted = aes.decrypt(secretEncrypted);

            MatcherAssert.assertThat(secretDecrypted, is(secret));
            System.out.println("secret length: " + secret.length());
            System.out.println("secretEncrypted length: " + secretEncrypted.length());

        }
    }

}