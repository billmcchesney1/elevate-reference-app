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
package com.mastercard.developers.elevate.accelerator.service;

import com.mastercard.developers.elevate.accelerator.exception.ServiceException;
import com.mastercard.developers.elevate.accelerator.generated.apis.ElevateApi;
import com.mastercard.developers.elevate.accelerator.generated.invokers.ApiException;
import com.mastercard.developers.elevate.accelerator.generated.models.CheckEligibility;
import com.mastercard.developers.elevate.accelerator.generated.models.Eligibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mastercard.developers.elevate.accelerator.util.JSON.deserializeErrors;

@Service
public class EligibilityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EligibilityService.class);
    private ElevateApi elevateApi;

    
    @Autowired
    public EligibilityService(ElevateApi elevateApi) {
        LOGGER.info("Initializing Redemption Service");
        this.elevateApi = elevateApi;
    }

    public Eligibility checkEligibility(CheckEligibility checkEligibility) throws ServiceException {
        LOGGER.info("Calling Eligibility Service  API");
        try {
            Eligibility eligibility= elevateApi.checkEligibility(
                    checkEligibility);

            LOGGER.info("create redemptions  API call successful, returning Redemption Info.");

            return eligibility;

        } catch (ApiException e) {
            LOGGER.info("Exception caught while calling Eligibility Service  API.");
            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }
    }
}

