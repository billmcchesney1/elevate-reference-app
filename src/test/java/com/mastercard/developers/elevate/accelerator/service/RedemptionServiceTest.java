package com.mastercard.developers.elevate.accelerator.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.mastercard.developers.elevate.accelerator.exception.ServiceException;
import com.mastercard.developers.elevate.accelerator.generated.apis.ElevateApi;
import com.mastercard.developers.elevate.accelerator.generated.invokers.ApiException;
import com.mastercard.developers.elevate.accelerator.generated.models.RedemptionInfo;
import com.mastercard.developers.elevate.accelerator.generated.models.Redemptions;

@ExtendWith(MockitoExtension.class)
class RedemptionServiceTest {

	@InjectMocks
	private RedemptionService redemptionService;

	@Mock
	private ElevateApi elevateApi;

	@Test
	@DisplayName("Test Redemption Success Scenario")
	void testRedemption() throws ServiceException, ApiException {
		Redemptions redemptions = new Redemptions();
		
		Mockito.when(redemptionService.createRedemption(any())).thenReturn(Resources.generateRedemptionInfo());
		RedemptionInfo redemptionInfo = redemptionService.createRedemption(redemptions);
		assertNotNull(redemptionInfo);
		assertEquals("45_6789",redemptionInfo.getData().getRedemptionId());
	}

	@Test
	@DisplayName("Test Redemption Exceptional Scenario")
	void testRedemptionException() throws ServiceException, ApiException {
		Redemptions redemptions = new Redemptions();

		Mockito.when(redemptionService.createRedemption(any())).thenThrow(Resources.getApiException());

		ServiceException exception = assertThrows(ServiceException.class,
				() -> redemptionService.createRedemption(redemptions));
		assertNotNull(exception);
		assertEquals("One of the request parameter is invalid",exception.getMessage());

	}
}
