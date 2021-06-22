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
package com.mastercard.developers.elevate.accelerator.controller;

/**
 * This controller class exposes the following endpoints
 * 1. /eligibilities
 * *  check eligibility for particular offer
 * 2. /redemptions
 * *  redemption for a credit card that was previously enrolled through the eligibilities resource
 */

import com.mastercard.developers.elevate.accelerator.exception.ServiceException;
import com.mastercard.developers.elevate.accelerator.generated.models.CheckEligibility;
import com.mastercard.developers.elevate.accelerator.generated.models.Eligibility;
import com.mastercard.developers.elevate.accelerator.generated.models.RedemptionInfo;
import com.mastercard.developers.elevate.accelerator.generated.models.Redemptions;
import com.mastercard.developers.elevate.accelerator.service.EligibilityService;
import com.mastercard.developers.elevate.accelerator.service.RedemptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/elevate")
public class ElevateAcceleratorController {

	private final EligibilityService eligibilityService;
	private final RedemptionService redemptionService;

	ElevateAcceleratorController(EligibilityService eligibilityService, RedemptionService redemptionService) {
		this.eligibilityService = eligibilityService;
		this.redemptionService = redemptionService;
	}

	@PostMapping("/eligibilities")
	public ResponseEntity<Eligibility> checkEligibility(@RequestBody CheckEligibility checkEligibility) throws ServiceException {
		return ResponseEntity.ok( eligibilityService.checkEligibility(checkEligibility));
	}


	@PostMapping("/redemptions")
	public ResponseEntity<RedemptionInfo> createRedemption(@RequestBody Redemptions redemptions) throws ServiceException {
		return ResponseEntity.ok(redemptionService.createRedemption(redemptions));
	}
}