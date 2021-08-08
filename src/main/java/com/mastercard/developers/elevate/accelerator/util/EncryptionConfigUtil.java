package com.mastercard.developers.elevate.accelerator.util;

import com.mastercard.developer.encryption.FieldLevelEncryptionConfig;
import com.mastercard.developer.encryption.FieldLevelEncryptionConfigBuilder;
import com.mastercard.developer.utils.EncryptionUtils;

import java.security.cert.Certificate;
import java.util.Properties;
import java.util.logging.Logger;

public class EncryptionConfigUtil {

    private EncryptionConfigUtil(){}

    private static final String ENCRYPTION_KEY = "mastercard.elevate.client.ref.app.encryption.file";
    private static final String JSON_PATH = "$";
    private static final String SHA_256 = "SHA-256";
    private static final String OAEP_HASHING_ALGORITHM = "oaepHashingAlgorithm";
    private static final String ENCRYPTED_DATA = "encryptedData";
    private static final String ENCRYPTED_KEY = "encryptedKey";
    private static final String PUBLIC_KEY_FINGERPRINT = "publicKeyFingerprint";
    private static final String IV = "iv";

    private static final Logger LOGGER = Logger.getLogger(EncryptionConfigUtil.class.getName());

    public static FieldLevelEncryptionConfig getEncryptionConfig(Properties prop) {

        FieldLevelEncryptionConfig config = null;
        try {
            Certificate encryptionCertificate = EncryptionUtils.loadEncryptionCertificate(prop.getProperty(ENCRYPTION_KEY));
            config = FieldLevelEncryptionConfigBuilder.aFieldLevelEncryptionConfig()
                    .withEncryptionCertificate(encryptionCertificate)
                    .withEncryptionPath(JSON_PATH, JSON_PATH)
                    .withOaepPaddingDigestAlgorithm(SHA_256)
                    .withOaepPaddingDigestAlgorithmFieldName(OAEP_HASHING_ALGORITHM)
                    .withEncryptedValueFieldName(ENCRYPTED_DATA)
                    .withEncryptedKeyFieldName(ENCRYPTED_KEY)
                    .withEncryptionKeyFingerprintFieldName(PUBLIC_KEY_FINGERPRINT)
                    .withIvFieldName(IV)
                    .withFieldValueEncoding(FieldLevelEncryptionConfig.FieldValueEncoding.HEX)
                    .build();
        } catch (Exception e) {
            LOGGER.info("Error while getting encryption configuration" + e);
        }
        return config;
    }
}