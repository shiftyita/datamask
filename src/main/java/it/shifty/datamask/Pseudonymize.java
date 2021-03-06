package it.shifty.datamask;

import it.shifty.datamask.algorithm.aes.Aes;
import it.shifty.datamask.algorithm.fpe.FPE;
import it.shifty.datamask.algorithm.fpe.custom.EnumChar;
import it.shifty.datamask.context.DataCrypt;

import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Pseudonymize {

    private FPE fpe;
    private Aes aes;

    public Pseudonymize(String secretKey, String IV_Key)
            throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException {
        this.fpe = new FPE(secretKey, IV_Key);
        this.aes = new Aes(secretKey, IV_Key);
    }

    public String decryptFieldString(String fieldEncryptedValue,
                                     DataCrypt dataCryptInstance) throws Exception {
        String fieldDecrypted = "";

        if (dataCryptInstance.dataType().equals(DataCrypt.DataType.DEFAULT_STRING)) {
            if (!fpe.getEnumChar().equals(EnumChar.CUSTOM))
                fpe.useCustomCharset();
            fieldDecrypted = fpe.decryptString(fieldEncryptedValue);
        } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.EMAIL)) {
            if (!fpe.getEnumChar().equals(EnumChar.EMAIL))
                fpe.useEmailCharset();
            fieldDecrypted = fpe.decryptEmail(fieldEncryptedValue);
        } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.LONG_STRING)) {
            fieldDecrypted = aes.decrypt(fieldEncryptedValue);
        } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.PHONE)) {
            if (!fpe.getEnumChar().equals(EnumChar.PHONE))
                fpe.usePhoneCharset();
            fieldDecrypted = fpe.decryptString(fieldEncryptedValue);
        } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.UNICODE)) {
            if (!fpe.getEnumChar().equals(EnumChar.UNICODE))
                fpe.useUnicodeCharset();
            fieldDecrypted = fpe.decryptString(fieldEncryptedValue);
        }
        return fieldDecrypted;
    }

    public String encryptFieldString(String fieldPlainValue,
                                     DataCrypt dataCryptInstance) throws Exception {
        String fieldEncrypted = "";

        if (dataCryptInstance.dataType().equals(DataCrypt.DataType.DEFAULT_STRING)) {
            if (!fpe.getEnumChar().equals(EnumChar.CUSTOM))
                fpe.useCustomCharset();
            fieldEncrypted = fpe.encryptString(fieldPlainValue);
        } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.EMAIL)) {
            if (!fpe.getEnumChar().equals(EnumChar.EMAIL))
                fpe.useEmailCharset();
            fieldEncrypted = fpe.encryptEmail(fieldPlainValue);
        } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.LONG_STRING)) {
            fieldEncrypted = aes.encrypt(fieldPlainValue);
        } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.PHONE)) {
            if (!fpe.getEnumChar().equals(EnumChar.PHONE))
                fpe.usePhoneCharset();
            fieldEncrypted = fpe.encryptString(fieldPlainValue);
        } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.UNICODE)) {
            if (!fpe.getEnumChar().equals(EnumChar.UNICODE))
                fpe.useUnicodeCharset();
            fieldEncrypted = fpe.encryptString(fieldPlainValue);
        }
        return fieldEncrypted;
    }

    public void cryptClass(Object classToCrypt) throws Exception {
        Field[] classFields = classToCrypt.getClass().getFields();
        for (Field field : classFields) {
            encryptSingleField(classToCrypt, field);
        }
    }

    public void cryptClass(Object classToCrypt,
                           List<String> fieldsToCrypt)
            throws Exception {
        Field[] classFields = classToCrypt.getClass().getFields();
        for (Field field : classFields) {
            if (fieldsToCrypt.contains(field.getName())) {
                encryptSingleField(classToCrypt, field);
            }
        }
    }

    private void encryptSingleField(Object classToCrypt, Field field) throws Exception {
        field.setAccessible(true);
        if (field.isAnnotationPresent(DataCrypt.class)) {
            DataCrypt dataCryptInstance = field.getAnnotation(DataCrypt.class);
            if (dataCryptInstance.dataType().equals(DataCrypt.DataType.DEFAULT_STRING)) {
                if (field.getType().equals(String.class)) {
                    if (!fpe.getEnumChar().equals(EnumChar.CUSTOM))
                        fpe.useCustomCharset();
                    field.set(classToCrypt, fpe.encryptString(field.get(classToCrypt).toString()));
                }
            } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.EMAIL)) {
                if (field.getType().equals(String.class)) {
                    if (!fpe.getEnumChar().equals(EnumChar.EMAIL))
                        fpe.useEmailCharset();
                    field.set(classToCrypt, fpe.encryptEmail(field.get(classToCrypt).toString()));
                }
            } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.NUMBER)) {
                if (field.getType().equals(Integer.class)) {
                    if (!fpe.getEnumChar().equals(EnumChar.NUMBER))
                        fpe.useNumericCharset();
                    field.set(classToCrypt, fpe.encryptInt((Integer) field.get(classToCrypt)));
                }
            } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.LONG_STRING)) {
                if (field.getType().equals(String.class)) {
                    field.set(classToCrypt, aes.encrypt(field.get(classToCrypt).toString()));
                }
            } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.AMOUNT)) {
                if (field.getType().equals(BigDecimal.class)) {
                    if (!fpe.getEnumChar().equals(EnumChar.NUMBER))
                        fpe.useNumericCharset();
                    field.set(classToCrypt, fpe.encryptAmount((BigDecimal) field.get(classToCrypt)));
                }
            } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.PHONE)) {
                if (field.getType().equals(String.class)) {
                    if (!fpe.getEnumChar().equals(EnumChar.PHONE))
                        fpe.usePhoneCharset();
                    field.set(classToCrypt, fpe.encryptString(field.get(classToCrypt).toString()));
                }
            } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.UNICODE)) {
                if (field.getType().equals(String.class)) {
                    if (!fpe.getEnumChar().equals(EnumChar.UNICODE))
                        fpe.useUnicodeCharset();
                    field.set(classToCrypt, fpe.encryptString(field.get(classToCrypt).toString()));
                }
            }
        }
    }

    public void decryptClass(Object classToDecrypt,
                             List<String> fieldsToDecrypt)
            throws Exception {
        Field[] classFields = classToDecrypt.getClass().getFields();
        for (Field field : classFields) {
            if (fieldsToDecrypt.contains(field.getName())) {
                decryptSingleField(classToDecrypt, field);
            }
        }
    }

    private void decryptSingleField(Object classToDecrypt,
                                    Field field) throws Exception {
        field.setAccessible(true);
        if (field.isAnnotationPresent(DataCrypt.class)) {
            DataCrypt dataCryptInstance = field.getAnnotation(DataCrypt.class);
            if (dataCryptInstance.dataType().equals(DataCrypt.DataType.DEFAULT_STRING)) {
                if (field.getType().equals(String.class)) {
                    if (!fpe.getEnumChar().equals(EnumChar.CUSTOM))
                        fpe.useCustomCharset();
                    field.set(classToDecrypt, fpe.decryptString(field.get(classToDecrypt).toString()));
                }
            } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.EMAIL)) {
                if (field.getType().equals(String.class)) {
                    if (!fpe.getEnumChar().equals(EnumChar.EMAIL))
                        fpe.useEmailCharset();
                    field.set(classToDecrypt, fpe.decryptEmail(field.get(classToDecrypt).toString()));
                }
            } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.NUMBER)) {
                if (field.getType().equals(Integer.class)) {
                    if (!fpe.getEnumChar().equals(EnumChar.NUMBER))
                        fpe.useNumericCharset();
                    field.set(classToDecrypt, fpe.decryptInt((Integer) field.get(classToDecrypt)));
                }
            } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.LONG_STRING)) {
                if (field.getType().equals(String.class)) {
                    field.set(classToDecrypt, aes.decrypt(field.get(classToDecrypt).toString()));
                }
            } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.AMOUNT)) {
                if (field.getType().equals(BigDecimal.class)) {
                    if (!fpe.getEnumChar().equals(EnumChar.NUMBER))
                        fpe.useNumericCharset();
                    field.set(classToDecrypt, fpe.decryptAmount((BigDecimal) field.get(classToDecrypt)));
                }
            } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.PHONE)) {
                if (field.getType().equals(String.class)) {
                    if (!fpe.getEnumChar().equals(EnumChar.PHONE))
                        fpe.usePhoneCharset();
                    field.set(classToDecrypt, fpe.decryptString(field.get(classToDecrypt).toString()));
                }
            } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.UNICODE)) {
                if (field.getType().equals(String.class)) {
                    if (!fpe.getEnumChar().equals(EnumChar.UNICODE))
                        fpe.useUnicodeCharset();
                    field.set(classToDecrypt, fpe.decryptString(field.get(classToDecrypt).toString()));
                }
            }
        }
    }

    public void decryptClass(Object classToDecrypt)
            throws Exception {
        Field[] classFields = classToDecrypt.getClass().getFields();
        for (Field field : classFields) {
            decryptSingleField(classToDecrypt, field);
        }
    }

}
