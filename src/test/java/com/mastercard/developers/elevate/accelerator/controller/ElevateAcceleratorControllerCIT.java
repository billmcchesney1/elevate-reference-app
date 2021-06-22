package com.mastercard.developers.elevate.accelerator.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercard.developers.elevate.accelerator.generated.apis.ElevateApi;
import com.mastercard.developers.elevate.accelerator.service.Resources;
import static com.mastercard.developers.elevate.accelerator.controller.ServiceEndpoints.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ElevateAcceleratorControllerCIT {
    
	@Autowired
    ObjectMapper objectMapper;
	
	@Autowired
    protected MockMvc mockMvc;
	
	@MockBean
	private ElevateApi elevateApi;
	
	@Test
	@DisplayName("Test Eligibility Service Success Scenario")
	void testEligiblityService() throws Exception {
		
		Mockito.when(elevateApi.checkEligibility(Resources.getCheckEligiblity())).thenReturn(Resources.createEligibility());
		String checkEligibility = objectMapper.writeValueAsString(Resources.getCheckEligiblity());
		// EXECUTION
        MvcResult mvcResult = mockMvc
                .perform(post(ELIGIBILITY)
                        .contentType("application/json")
                        .content(checkEligibility))
                        .andExpect(status().isOk()).andReturn();
        
        
        String actual = mvcResult.getResponse().getContentAsString();
        String expected = Resources.getFileAsString("eligibility-response.json");
        assertNotNull(actual);
        JSONAssert.assertEquals(expected, actual,JSONCompareMode.NON_EXTENSIBLE);
	}
	
	@Test
	@DisplayName("Test Redemptions Service Success Scenario")
	void testRedemptionsService() throws Exception {
		
		Mockito.when(elevateApi.createRedemption(Resources.getRedemptions())).thenReturn(Resources.generateRedemptionInfo());
		String redemptions = objectMapper.writeValueAsString(Resources.getRedemptions());
		// EXECUTION
        MvcResult mvcResult = mockMvc
                .perform(post(REDEMTIONS)
                        .contentType("application/json")
                        .content(redemptions))
                        .andExpect(status().isOk()).andReturn();
        
        
        String actual = mvcResult.getResponse().getContentAsString();
        String expected = Resources.getFileAsString("redemotions-response.json");
        assertNotNull(actual);
        JSONAssert.assertEquals(expected, actual,JSONCompareMode.NON_EXTENSIBLE);
	}

}
