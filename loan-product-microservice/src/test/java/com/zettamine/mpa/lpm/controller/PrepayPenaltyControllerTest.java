package com.zettamine.mpa.lpm.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zettamine.mpa.lpm.constants.AppConstants;
import com.zettamine.mpa.lpm.model.PrePayPenalityDto;
import com.zettamine.mpa.lpm.model.PrepayPenaltyStatusDto;
import com.zettamine.mpa.lpm.service.ILoanProductService;

@AutoConfigureMockMvc
@SpringBootTest
class PrepayPenaltyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ILoanProductService loanProductService;

    @InjectMocks
    private PrepayPenaltyController prepayPenaltyController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(prepayPenaltyController).build();
    }

    @Test
    void testCreatePrePayPenality() throws Exception {
      
        PrePayPenalityDto penalityDto = new PrePayPenalityDto();
        
        Float amount = 10001f;
        Float percentage = 11f;
        penalityDto.setMinPenalityAmount(amount);
        penalityDto.setPenalityPercentage(percentage);
        penalityDto.setProductId(1);

        
        doNothing().when(loanProductService).createPrePayPenality(penalityDto);

       
        mockMvc.perform(post("/api/v1/loan-product/prepay-penalty/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(penalityDto)))
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_201))
                .andExpect(jsonPath("$.statusMsg").value(AppConstants.LOAN_PRODUCT_PENALITY_CREATED_MESSAGE));
    }
    
    @Test
    void testUpdatePrePayPenality() throws Exception {
       
    	 PrePayPenalityDto penalityDto = new PrePayPenalityDto();
         
         Float amount = 10001f;
         Float percentage = 11f;
         penalityDto.setMinPenalityAmount(amount);
         penalityDto.setPenalityPercentage(percentage);
         penalityDto.setProductId(1);


        
        doNothing().when(loanProductService).updatePrePayPenality(penalityDto);

      
        mockMvc.perform(put("/api/v1/loan-product/prepay-penalty/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(penalityDto)))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_200)) 
                .andExpect(jsonPath("$.statusMsg").value(AppConstants.LOAN_PRODUCT_PENALITY_MESSAGE)); 
    }
    
    @Test
    void testUpdatePrePayPenalityStatus() throws Exception {
     
        Integer prodId = 1001; 
      
        doNothing().when(loanProductService).updatePrePayPenalityStatus(prodId);
        
        mockMvc.perform(put("/api/v1/loan-product/prepay-penalty/update/status/{prodId}", prodId))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_200)) 
                .andExpect(jsonPath("$.statusMsg").value(AppConstants.LOAN_PRODUCT_PENALITY_STATUS_MESSAGE)); 
    }
    
    @Test
    void testGetPrepayPenaltyById() throws Exception {
       
        Integer prodId = 1001; 
        Float amount = 10001f;
        
        PrepayPenaltyStatusDto prepayPenaltyStatusDto = new PrepayPenaltyStatusDto();
        prepayPenaltyStatusDto.setMinPenalityAmount(amount);
        
        when(loanProductService.getPrepayPenaltyById(prodId)).thenReturn(prepayPenaltyStatusDto);

        
        mockMvc.perform(get("/api/v1/loan-product/prepay-penalty/fetch/{prodId}", prodId))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.minPenalityAmount").value(amount)); 
    }



}
