/*
 *  Copyright (c) 2021 Mastercard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mastercard.developers.elevate.accelerator.configuration;

import com.mastercard.developer.encryption.FieldLevelEncryptionConfig;
import com.mastercard.developer.encryption.FieldLevelEncryptionConfigBuilder;
import com.mastercard.developer.interceptors.OkHttpFieldLevelEncryptionInterceptor;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developer.utils.EncryptionUtils;
import com.mastercard.developers.elevate.accelerator.generated.apis.ElevateApi;
import com.mastercard.developers.elevate.accelerator.generated.invokers.ApiClient;
import okhttp3.OkHttpClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * Api Client Setup
 */

@Configuration
public class ApiConfiguration {

	
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiConfiguration.class);

    @Value("${mastercard.api.authentication.consumer-key}")
    private String consumerKey;

    @Value("${mastercard.api.authentication.keystore-alias}")
    private String keyAlias;

    @Value("${mastercard.api.authentication.keystore-password}")
    private String keyPassword;

    @Value("${mastercard.api.authentication.key-file}")
    private String p12File;

    @Value("${mastercard.api.environment.base-path}")
    private String basePath;

    @Value("${mastercard.api.encryption.key-file}")
    private String encryptionKeyFile;

    @Value("${mastercard.api.encryption.fingerprint}")
    private String encryptionFingerprint;

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getEncryptionKeyFile() {
        return encryptionKeyFile;
    }

    public PrivateKey getSigningKey() {

        try (InputStream inputStream = ApiConfiguration.class.getClassLoader().getResourceAsStream(p12File)) {
            KeyStore pkcs12KeyStore = KeyStore.getInstance("PKCS12", "SunJSSE");
            pkcs12KeyStore.load(inputStream, keyPassword.toCharArray());
            return (PrivateKey) pkcs12KeyStore.getKey(keyAlias, keyPassword.toCharArray());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public FieldLevelEncryptionConfig getEncryptionConfig() {
        FieldLevelEncryptionConfig config = null;

        try {
            Certificate encryptionCertificate = EncryptionUtils.loadEncryptionCertificate(encryptionKeyFile);
            config = FieldLevelEncryptionConfigBuilder.aFieldLevelEncryptionConfig()
                    .withEncryptionCertificate(encryptionCertificate)
                    .withEncryptionPath("$.subscription.subscriptionPaymentCard", "$.subscription.encryptedPayload")
                    .withOaepPaddingDigestAlgorithm("SHA-256")
                    .withEncryptedValueFieldName("encryptedValue")
                    .withEncryptedKeyFieldName("encryptedKey")
                    .withIvFieldName("iv")
                    .withFieldValueEncoding(FieldLevelEncryptionConfig.FieldValueEncoding.HEX)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error while getting encryption configuration",e);
        }
        return config;
    }

    @Bean
    public ApiClient apiClient(){
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(
                new OkHttpFieldLevelEncryptionInterceptor(
                        getEncryptionConfig())).addInterceptor(
                new OkHttpOAuth1Interceptor(consumerKey, getSigningKey()))
                .build();

        return new ApiClient().setHttpClient(client).setBasePath(basePath);
    }

    @Bean
    public ElevateApi elevateApi(ApiClient apiClient) {
        return new ElevateApi(apiClient);
    }

}
