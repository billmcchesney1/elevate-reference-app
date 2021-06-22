package com.mastercard.developers.elevate.accelerator.service;

import com.mastercard.developers.elevate.accelerator.exception.ServiceException;
import com.mastercard.developers.elevate.accelerator.generated.apis.ElevateApi;
import com.mastercard.developers.elevate.accelerator.generated.invokers.ApiException;
import com.mastercard.developers.elevate.accelerator.generated.models.CheckEligibility;
import com.mastercard.developers.elevate.accelerator.generated.models.Eligibility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EligibilityServiceTest {

	@InjectMocks
	private EligibilityService eligibilityService;
	
	@Mock
	private ElevateApi elevateApi;

	@Test
	@DisplayName("Test Eligibility Success Scenario")
	void testEligibility() throws ServiceException, ApiException {
		CheckEligibility checkEligibility = new CheckEligibility();

		Mockito.when(elevateApi.checkEligibility(any())).thenReturn(Resources.createEligibility());
		Eligibility eligibility = eligibilityService.checkEligibility(checkEligibility);
		assertNotNull(eligibility);
		assertEquals("1234_567",eligibility.getData().getEligibilityId());
	}
	
	@Test
	@DisplayName("Test Eligibility Exceptional Scenario")
	void testEligibilityException() throws ServiceException, ApiException {
		CheckEligibility checkEligibility = new CheckEligibility();

		Mockito.when(elevateApi.checkEligibility(any())).thenThrow(Resources.getApiException());
		
		ServiceException exception = assertThrows(ServiceException.class, ()->eligibilityService.checkEligibility(checkEligibility));
		assertNotNull(exception);
		assertEquals("One of the request parameter is invalid",exception.getMessage());
	}
}
