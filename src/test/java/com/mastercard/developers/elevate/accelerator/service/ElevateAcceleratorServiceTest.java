package com.mastercard.developers.elevate.accelerator.service;

import com.mastercard.developers.elevate.accelerator.generated.apis.ElevateApi;
import com.mastercard.developers.elevate.accelerator.generated.invokers.ApiException;
import com.mastercard.developers.elevate.accelerator.generated.models.CheckEligibility;
import com.mastercard.developers.elevate.accelerator.generated.models.Eligibility;
import com.mastercard.developers.elevate.accelerator.generated.models.EligibilityData;
import com.mastercard.developers.elevate.accelerator.generated.models.RedemptionInfo;
import com.mastercard.developers.elevate.accelerator.generated.models.RedemptionInfoData;
import com.mastercard.developers.elevate.accelerator.generated.models.Redemptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ElevateAcceleratorServiceTest {

    @InjectMocks
    ElevateAcceleratorService elevateAcceleratorService;

    @Mock
    ElevateApi elevateApi;

    private static final String CHECK_ELIGIBILITY = "checkEligibility";
    private static final String REDEMPTION = "redemption";
    private static final String OK = "OK";
    private static final String ID = "390008_0265_2921";

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCheckEligibilityService() throws ApiException {
        String[] args = {CHECK_ELIGIBILITY};
        when(elevateApi.checkEligibility(any(CheckEligibility.class))).thenReturn(getEligibilityObject());
        elevateAcceleratorService.callElevateServiceApis(args);
        verify(elevateApi).checkEligibility(any(CheckEligibility.class));
    }

    @Test
    void testRedemptionService() throws ApiException {
        String[] args = {REDEMPTION};
        when(elevateApi.createRedemption(any(Redemptions.class))).thenReturn(getRedemptionInfoObject());
        elevateAcceleratorService.callElevateServiceApis(args);
        verify(elevateApi).createRedemption(any(Redemptions.class));
    }

    @Test
    void testCheckEligibilityServiceError() throws ApiException {
        String[] args = {};
        when(elevateApi.checkEligibility(any(CheckEligibility.class))).thenThrow(new ApiException());
        elevateAcceleratorService.callElevateServiceApis(args);
        verify(elevateApi).checkEligibility(any(CheckEligibility.class));
    }

    @Test
    void testRedemptionServiceError() throws ApiException {
        when(elevateApi.createRedemption(any(Redemptions.class))).thenThrow(new ApiException());
        elevateAcceleratorService.callElevateServiceApis(null);
        verify(elevateApi).createRedemption(any(Redemptions.class));
    }

    private Eligibility getEligibilityObject(){
        Eligibility eligibility = new Eligibility();
        eligibility.setMsg(OK);
        EligibilityData data = new EligibilityData();
        data.setEligibile(true);
        data.setEligibilityId(ID);
        eligibility.setData(data);
        return eligibility;
    }

    private RedemptionInfo getRedemptionInfoObject(){
        RedemptionInfo redemptionInfo = new RedemptionInfo();
        redemptionInfo.setMsg(OK);
        RedemptionInfoData data = new RedemptionInfoData();
        data.setRedemptionId(ID);
        redemptionInfo.setData(data);
        return redemptionInfo;
    }
}