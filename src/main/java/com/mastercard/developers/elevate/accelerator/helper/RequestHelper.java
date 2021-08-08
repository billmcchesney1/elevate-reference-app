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
package com.mastercard.developers.elevate.accelerator.helper;

import com.google.gson.Gson;
import com.mastercard.developer.interceptors.OkHttpFieldLevelEncryptionInterceptor;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developers.elevate.accelerator.generated.invokers.ApiClient;
import com.mastercard.developers.elevate.accelerator.generated.models.CheckEligibility;
import com.mastercard.developers.elevate.accelerator.generated.models.Redemptions;
import okhttp3.OkHttpClient;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Objects;
import java.util.Properties;

import static com.mastercard.developers.elevate.accelerator.util.EncryptionConfigUtil.getEncryptionConfig;

public final class RequestHelper {

    private static final String BASE_URL = "mastercard.elevate.client.api.base.path";
    private static final String CONSUMER_KEY = "mastercard.elevate.client.ref.app.consumer.key";
    private static final String KEYSTORE_PATH = "mastercard.elevate.client.p12.path";
    private static final String KEYSTORE_ALIAS = "mastercard.elevate.client.ref.app.keystore.alias";
    private static final String KEYSTORE_PASSWORD = "mastercard.elevate.client.ref.app.keystore.password";
    private static final String TYPE_PK_CS12 = "PKCS12";

    private static final String CHECK_ELIGIBILITY_PAYLOAD = resourceContent("templates/" + "check-eligibility-payload.json");

    private static final String REDEMPTIONS_PAYLOAD = resourceContent("templates/" + "redemptions-payload.json");

    private static Properties prop = null;
    private static String propertyFile = "application.properties";

    private RequestHelper(){}

    public static void setProp(Properties prop) {
        RequestHelper.prop = prop;
    }

    public static void setPropertyFile(String propertyFile) {
        RequestHelper.propertyFile = propertyFile;
    }

    public static void loadProperties() {
        if (prop == null || prop.isEmpty()) {
            try {
                InputStream input = RequestHelper.class.getClassLoader()
                        .getResourceAsStream(propertyFile);
                prop = new Properties();
                if (input == null) {
                    return;
                }
                prop.load(input);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public static String getProperty(String key){
        loadProperties();
        return prop.getProperty(key);
    }

    public static ApiClient signRequest() {
        loadProperties();
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(
                new OkHttpFieldLevelEncryptionInterceptor(
                        getEncryptionConfig(prop))).addInterceptor(
                new OkHttpOAuth1Interceptor(prop.getProperty(CONSUMER_KEY), getPrivateKey()))
                .build();

        return new ApiClient().setHttpClient(client).setBasePath(prop.getProperty(BASE_URL));
    }

    private static PrivateKey getPrivateKey() {
        try {
            InputStream inputStream = getResourceStream(prop.getProperty(KEYSTORE_PATH));
            KeyStore keyStore = KeyStore.getInstance(TYPE_PK_CS12);
            keyStore.load(inputStream, prop.getProperty(KEYSTORE_PASSWORD).toCharArray());
            return (PrivateKey) keyStore.getKey(prop.getProperty(KEYSTORE_ALIAS), prop.getProperty(KEYSTORE_PASSWORD).toCharArray());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static CheckEligibility getCheckEligibilityPayload() {
        Gson gson = new Gson();
        return gson.fromJson(CHECK_ELIGIBILITY_PAYLOAD, CheckEligibility.class);
    }

    public static Redemptions getRedemptionsPayload() {
        Gson gson = new Gson();
        return gson.fromJson(REDEMPTIONS_PAYLOAD, Redemptions.class);
    }

    private static InputStream getResourceStream(String inputString) {
        return RequestHelper.class.getClassLoader().getResourceAsStream(inputString);
    }

    public static String resourceContent(String classpathName) {
        try {
            InputStream inputStream = RequestHelper.class.getClassLoader().getResourceAsStream(classpathName);
            return IOUtils.toString(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot load resource from classpath '" + classpathName + "'.", ex);
        }
    }
}