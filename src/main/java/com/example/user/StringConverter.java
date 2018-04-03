package com.example.user;

import javax.persistence.AttributeConverter;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Converter for encrypting and decrypting strings
 */
public class StringConverter implements AttributeConverter<String, String>
{
    /**
     * Password to use for encrypting strings
     */
    private static final String ENCRYPTION_PASSWORD = "HelloThisIsConverterKey";

    @Override
    public String convertToDatabaseColumn(String arrtibute)
    {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(ENCRYPTION_PASSWORD);
        return textEncryptor.encrypt(arrtibute);
    }

    @Override
    public String convertToEntityAttribute(String columnValue)
    {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(ENCRYPTION_PASSWORD);
        return textEncryptor.decrypt(columnValue);
    }

}
