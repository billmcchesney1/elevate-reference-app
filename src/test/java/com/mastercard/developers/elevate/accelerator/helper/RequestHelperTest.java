package com.mastercard.developers.elevate.accelerator.helper;

import com.mastercard.developers.elevate.accelerator.generated.invokers.ApiClient;
import com.mastercard.developers.elevate.accelerator.generated.models.CheckEligibility;
import com.mastercard.developers.elevate.accelerator.generated.models.Redemptions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestHelperTest {

    private static final String KEY_BASE_URL = "mastercard.elevate.client.api.base.path";

    private static final String VALUE_BASE_URL = "https://sandbox.api.mastercard.com/elevate";

    private static final int PARTNER_ID = 512;
    private static final String CREDIT_CARD_NUMBER = "5555555555554444";
    private static final int PRODUCT_ID = 161041;
    private static final String ACCESS_CODE = "ae-amazonprime";
    private static final String CURRENCY_CODE = "USD";
    private static final String ELIGIBILITY_ID = "25161_161041_1179";
    private static final BigDecimal SPEND_AMOUNT = BigDecimal.valueOf(109.99);
    private static final BigDecimal BENEFIT_AMOUNT_GIVEN = BigDecimal.valueOf(50.99);

    @Test
    void testGetProperty(){
        String value = RequestHelper.getProperty(KEY_BASE_URL);
        assertEquals(VALUE_BASE_URL, value);
    }

    @Test
    void testLoadProperties(){
        RequestHelper.loadProperties();
        assertEquals(VALUE_BASE_URL, RequestHelper.getProperty(KEY_BASE_URL));
    }

    @Test
    void testClientId() {
        ApiClient apiClient = RequestHelper.signRequest();
        assertNotNull(apiClient);
        assertEquals(VALUE_BASE_URL, apiClient.getBasePath());
    }

    @Test
    void testCheckEligibilityPayload(){
        CheckEligibility checkEligibilityPayload = RequestHelper.getCheckEligibilityPayload();
        assertEquals(ACCESS_CODE, checkEligibilityPayload.getAccessCode());
        assertEquals(CREDIT_CARD_NUMBER, checkEligibilityPayload.getCreditCardNumber());
        assertEquals(PARTNER_ID, checkEligibilityPayload.getPartnerId());
        assertEquals(PRODUCT_ID, checkEligibilityPayload.getProductId());
    }

    @Test
    void testRedemptionsPayload(){
        Redemptions redemptions = RequestHelper.getRedemptionsPayload();
        assertEquals(CURRENCY_CODE, redemptions.getBenefitCurrencyCode());
        assertEquals(CREDIT_CARD_NUMBER, redemptions.getCreditCardNumber());
        assertEquals(PARTNER_ID, redemptions.getPartnerId());
        assertEquals(BENEFIT_AMOUNT_GIVEN, redemptions.getBenefitAmountGiven());
        assertEquals(ELIGIBILITY_ID, redemptions.getEligibilityId());
        assertEquals(SPEND_AMOUNT, redemptions.getSpendAmount());
        assertEquals(CURRENCY_CODE, redemptions.getSpendCurrencyCode());
    }

    @Test
    void testInvalidPropertiesFileError(){
        RequestHelper.setProp(null);
        RequestHelper.setPropertyFile("invalidFile");
        RequestHelper.loadProperties();
        assertNull(RequestHelper.getProperty(KEY_BASE_URL));
        RequestHelper.setPropertyFile("application.properties");
    }

    @Test
    void testNullPropertiesFileError(){
        RequestHelper.setProp(null);
        RequestHelper.setPropertyFile(null);
        assertThrows(IllegalArgumentException.class, RequestHelper::loadProperties);
        RequestHelper.setPropertyFile("application.properties");
    }

    @Test
    void testInvalidClientId() {
        RequestHelper.setProp(getProperties());
        assertThrows(IllegalArgumentException.class, RequestHelper::signRequest);
        RequestHelper.setProp(null);
    }

    @Test
    void testResourceContent(){
        assertThrows(IllegalArgumentException.class, ()-> RequestHelper.resourceContent(null));
    }

    private Properties getProperties(){
        Properties properties = new Properties();
        properties.setProperty("","");
        return properties;
    }
}