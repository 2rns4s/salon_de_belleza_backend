package com.sena.persistencia.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * NOTA:
 * La DECISION de que ciertos datos (documento, telefono) sean sensibles pertenece
 * al negocio y quedo registrada en los specs de la aplicacion. Sin embargo, el
 * MECANISMO de cifrado a nivel de columna SI es un detalle de la capa de
 * persistencia (un AttributeConverter de JPA que transforma el atributo de la
 * entity al valor que se guarda en la base de datos).
 *
 * Se incluye tal cual el original para no almacenar esos datos en texto plano,
 * garantizando que las columnas cifradas se comporten igual que lo planeado.
 */
@Converter
@Component
public class CryptoConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static byte[] KEY;

    @Value("${security.db.encryption.key}")
    public void setKey(String key) {
        KEY = key.getBytes();
    }

    @Override
    public String convertToDatabaseColumn(String datoEnTextoPlano) {
        if (datoEnTextoPlano == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY, "AES"));
            return Base64.getEncoder().encodeToString(cipher.doFinal(datoEnTextoPlano.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar el dato: " + e.getMessage());
        }
    }

    @Override
    public String convertToEntityAttribute(String datoEnTextoCifrado) {
        if (datoEnTextoCifrado == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY, "AES"));
            return new String(cipher.doFinal(Base64.getDecoder().decode(datoEnTextoCifrado)));
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar el dato: " + e.getMessage());
        }
    }
}
