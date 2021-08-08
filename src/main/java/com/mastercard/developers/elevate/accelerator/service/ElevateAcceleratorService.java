package com.mastercard.developers.elevate.accelerator.service;

import com.google.gson.Gson;
import com.mastercard.developers.elevate.accelerator.generated.apis.ElevateApi;
import com.mastercard.developers.elevate.accelerator.generated.invokers.ApiException;
import com.mastercard.developers.elevate.accelerator.generated.models.CheckEligibility;
import com.mastercard.developers.elevate.accelerator.generated.models.Eligibility;
import com.mastercard.developers.elevate.accelerator.generated.models.RedemptionInfo;
import com.mastercard.developers.elevate.accelerator.generated.models.Redemptions;
import com.mastercard.developers.elevate.accelerator.helper.RequestHelper;

import java.util.logging.Logger;

public class ElevateAcceleratorService {

    private final Logger logger = Logger.getLogger(ElevateAcceleratorService.class.getName());

    private static final String CHECK_ELIGIBILITY = "checkEligibility";
    private static final String REDEMPTION = "redemption";
    private static final String CHECKING_ELIGIBILITY = "CHECKING ELIGIBILITY";
    private static final String BENEFIT_REDEMPTION = "BENEFIT REDEMPTION";

    ElevateApi elevateApi = new ElevateApi(RequestHelper.signRequest());

    public void callElevateServiceApis(String[] args) {

        if (checkForScenario(args, CHECK_ELIGIBILITY)) {
            logScenario(CHECKING_ELIGIBILITY);
            checkEligibility(elevateApi);
        }

        if (checkForScenario(args, REDEMPTION)) {
            logScenario(BENEFIT_REDEMPTION);
            redemption(elevateApi);
        }
    }

    private void checkEligibility(ElevateApi elevateApi) {
        try {
            CheckEligibility checkEligibility = RequestHelper.getCheckEligibilityPayload();
            Eligibility eligibility = elevateApi.checkEligibility(checkEligibility);
            logResponse(eligibility);
        }catch (ApiException exception){
            logger.info("Exception occurred while checking eligibility");
        }
    }

    private void redemption(ElevateApi elevateApi) {
        try {
            Redemptions redemptions = RequestHelper.getRedemptionsPayload();
            RedemptionInfo redemptionInfo = elevateApi.createRedemption(redemptions);
            logResponse(redemptionInfo);
        }catch (ApiException exception){
            logger.info("Exception occurred while redemption");
        }
    }

    private boolean checkForScenario(String[] args, String scenario) {
        return (args != null && args.length > 0 && args[0].contains(scenario)) || (args == null || args.length == 0);
    }

    private void logScenario(String scenario) {
        String message = "\n--------------------------------------------------------------------" +
                "\n -------------------------------------------------------------------" +
                "\n----------------------- " + scenario + " -----------------------" +
                "\n--------------------------------------------------------------------" +
                "\n--------------------------------------------------------------------\n";
        logger.info(message);
    }

    private void logResponse(Object response) {
        Gson gson = new Gson();
        String responseString = "\n--------------------------- RESPONSE -------------------------------\n"
                + gson.toJson(response)
                + "\n--------------------------------------------------------------------\n";
        logger.info(responseString);
    }
}