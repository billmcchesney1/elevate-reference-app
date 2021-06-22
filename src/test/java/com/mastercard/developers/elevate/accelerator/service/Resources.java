package com.mastercard.developers.elevate.accelerator.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

import com.google.gson.Gson;
import com.mastercard.developers.elevate.accelerator.generated.invokers.ApiException;
import com.mastercard.developers.elevate.accelerator.generated.models.CheckEligibility;
import com.mastercard.developers.elevate.accelerator.generated.models.Eligibility;
import com.mastercard.developers.elevate.accelerator.generated.models.EligibilityData;
import com.mastercard.developers.elevate.accelerator.generated.models.RedemptionInfo;
import com.mastercard.developers.elevate.accelerator.generated.models.RedemptionInfoData;
import com.mastercard.developers.elevate.accelerator.generated.models.Redemptions;

public class Resources {
	
	public static String getFileAsString(String filePath) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(filePath);
        return new String(readAll(classPathResource.getInputStream()));
    }
	private static byte[] readAll(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
	public static Redemptions getRedemptions() {
		Redemptions redemptions = new Redemptions();
		redemptions.setBenefitAmountGiven(new BigDecimal(3.99));
		redemptions.setBenefitCurrencyCode("USD");
		redemptions.setCreditCardNumber("5555555555552810");
		redemptions.setEligibilityId("17453_161041_1179");
		redemptions.setPartnerId(512);
		redemptions.setSpendAmount(new BigDecimal(1.99));
		redemptions.setSpendCurrencyCode("USD");
		return redemptions;
	}
	
	public String createResponse() {
		Redemptions redemptions = new Redemptions();
		redemptions.setBenefitAmountGiven(new BigDecimal(50.99));
		redemptions.setBenefitCurrencyCode("USD");
		redemptions.setCreditCardNumber("5555555555552729");
		redemptions.setEligibilityId("17217_161041_1179");
		redemptions.setPartnerId(512);
		redemptions.setSpendAmount(new BigDecimal(100.99));
		redemptions.setSpendCurrencyCode("USD");
		Gson gson = new Gson();
		String json = gson.toJson(redemptions);
		return json;
	}
	
	public static RedemptionInfo generateRedemptionInfo() {
		RedemptionInfo redemptionInfo = new RedemptionInfo();
		RedemptionInfoData redemptionInfoData = new RedemptionInfoData();
        redemptionInfoData.setRedemptionId("45_6789");
		redemptionInfo.setData(redemptionInfoData );
		redemptionInfo.setMsg("Ok");
		return redemptionInfo;
	}
	
	public static String createEligiblityResponse() {
    	CheckEligibility checkEligibility = new CheckEligibility();
    	checkEligibility.setAccessCode("ae-amazonprime");
    	checkEligibility.setCardHolderName("Nirmish Dholakia");
    	checkEligibility.setCreditCardNumber("5555555555552851");
    	checkEligibility.setEmail("nirmish.dholakia@mastercard.com");
    	checkEligibility.setPartnerId(512);
    	checkEligibility.setProductId(161041);
    	Gson gson = new Gson();
    	String json = gson.toJson(checkEligibility);
    	return json;
    }
    
    public static Eligibility createEligibility() {
		Eligibility eligibilityDTO = new Eligibility();
		EligibilityData eligibilityData = new EligibilityData();
		eligibilityData.setEligibile(true);
		eligibilityData.setEligibilityId("1234_567");
		eligibilityDTO.setMsg("Ok");
		eligibilityDTO.setData(eligibilityData);
		return eligibilityDTO;
	}
    
    public static CheckEligibility getCheckEligiblity() {
    	CheckEligibility checkEligibility = new CheckEligibility();
    	checkEligibility.setAccessCode("ae-amazonprime");
    	checkEligibility.setCardHolderName("Nirmish Dholakia");
    	checkEligibility.setCreditCardNumber("5555555555552851");
    	checkEligibility.setEmail("nirmish.dholakia@mastercard.com");
    	checkEligibility.setPartnerId(12);
    	checkEligibility.setProductId(161041);
    	return checkEligibility;
    }
    public static ApiException getApiException() {
		Map<String, List<String>> responseHeaders = new HashMap<>();
		responseHeaders.put("x-openApi-clientId", new ArrayList<>());
		ApiException exception = new ApiException("One of the request parameter is invalid",400,responseHeaders,"{'errorResponseBody': 'anyResponse'}");
		
		return exception;
	}

}
