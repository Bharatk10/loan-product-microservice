package com.zettamine.mpa.lpm.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.zettamine.mpa.lpm.exception.ResourceNotFoundException;
import com.zettamine.mpa.lpm.model.LoanProdDto;
import com.zettamine.mpa.lpm.model.LoanProductDto;
import com.zettamine.mpa.lpm.model.LoanProductSearchCriteria;
import com.zettamine.mpa.lpm.model.ProductStatusHistoryDto;
import com.zettamine.mpa.lpm.service.ILoanProductService;

@AutoConfigureMockMvc
@SpringBootTest
public class LoanProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ILoanProductService loanProductService;

    @InjectMocks
    private LoanProductController loanProductController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loanProductController).build();
    }

    @Test
    void testAddLoanProduct() throws Exception {
    	
    	  LoanProductDto loanProductDto = new LoanProductDto();
    	  loanProductDto.setProductName("Home Loan");
    	  loanProductDto.setLoanTerm("240"); loanProductDto.setInterestRate("11.7");;
    	  loanProductDto.setMaxLoanAmount("23343422");
    	  loanProductDto.setMinDownPayment("3232");
    	  loanProductDto.setMinDownPaymentType("absolute");
    	  loanProductDto.setMinCreditScore("600");
    	  loanProductDto.setMaxLoanToValueRatio("90");
    	  loanProductDto.setPrivateMortgageInsuranceReq(true);
    	  loanProductDto.setOrginationFee("678"); loanProductDto.setLockinPeriod("20");
    	  loanProductDto.setMinPenalityAmount("123");
    	  loanProductDto.setPenalityPercentage("1.34");
    	  loanProductDto.setDocumentRequirement("document requirements");
        
        doNothing().when(loanProductService).create(loanProductDto);

        mockMvc.perform(post("/api/v1/loan-product/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loanProductDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_201))
                .andExpect(jsonPath("$.statusMsg").value(AppConstants.CREATE_LOAN_PRODUCT_MESSAGE));
    }
    
    @Test
    void testGetLoanProductById() throws Exception {
      
        Integer productId = 1;
        
        LoanProdDto loanProdDto = new LoanProdDto();
        loanProdDto.setProductId(productId);
        loanProdDto.setProductName("LoanProductName");
        
        when(loanProductService.getbyId(productId)).thenReturn(loanProdDto);

        mockMvc.perform(get("/api/v1/loan-product/product/fetch/{prodId}", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.productName").value("LoanProductName"));
    }
    
    @Test
    void testUpdateLoanProductStatus() throws Exception {
     
        Integer productId = 1;

        doNothing().when(loanProductService).updateLoanProductStatus(productId);

        mockMvc.perform(put("/api/v1/loan-product/product/update/loanProductStatus/{prodId}", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(AppConstants.LOAN_PRODUCT_STATUS_MESSAGE));
    }
    
    @Test
    void testGetProductStatusHistory() throws Exception {
        // Define the product ID to be used in the test
        Integer prodId = 1001;

        // Define a list of ProductStatusHistoryDto to be returned by the service
        List<ProductStatusHistoryDto> productStatusHistory = new ArrayList<>();
        

        // Mock the loanProductService.getProductStatusHistory method
        when(loanProductService.getProductStatusHistory(prodId)).thenReturn(productStatusHistory);

        // Perform the GET request to the endpoint with the product ID
        mockMvc.perform(get("/api/v1/loan-product/product/fetch/productHistory/{prodId}", prodId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()); // Check endTime value in the second item
    }

    
    @Test
    void testUpdateLoanProduct() throws Exception {
       
        Integer prodId = 1;

      LoanProductDto loanProductDto = new LoanProductDto();
      loanProductDto.setProductName("Home Loan");
  	  loanProductDto.setLoanTerm("240"); loanProductDto.setInterestRate("11.7");;
  	  loanProductDto.setMaxLoanAmount("23343422");
  	  loanProductDto.setMinDownPayment("3232");
  	  loanProductDto.setMinDownPaymentType("absolute");
  	  loanProductDto.setMinCreditScore("600");
  	  loanProductDto.setMaxLoanToValueRatio("90");
  	  loanProductDto.setPrivateMortgageInsuranceReq(true);
  	  loanProductDto.setOrginationFee("678"); loanProductDto.setLockinPeriod("20");
  	  loanProductDto.setMinPenalityAmount("123");
  	  loanProductDto.setPenalityPercentage("1.34");
  	  loanProductDto.setDocumentRequirement("document requirements");
       
        doNothing().when(loanProductService).updateLoanProduct(prodId, loanProductDto);

        mockMvc.perform(put("/api/v1/loan-product/product/update/loanProduct/{prodId}", prodId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loanProductDto)))
            .andExpect(status().isOk()) 
            .andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_200))
            .andExpect(jsonPath("$.statusMsg").value(AppConstants.UPDATE_LOAN_PRODUCT_MESSAGE)); 
    }
    
    @Test
    void testSearchLoanProduct() throws Exception {
       
        LoanProductSearchCriteria criteria = new LoanProductSearchCriteria();
        criteria.setCreditScore(450); 
        criteria.setPrivateMortgageInsurance(true); 
        
        List<LoanProdDto> expectedLoanProducts = new ArrayList<>();
        LoanProdDto loanProduct1 = new LoanProdDto();
        loanProduct1.setProductId(1);
        loanProduct1.setProductName("Product A");
        loanProduct1.setStatus(false);
      
        expectedLoanProducts.add(loanProduct1);

        LoanProdDto loanProduct2 = new LoanProdDto();
        loanProduct2.setProductId(2);
        loanProduct2.setProductName("Product B");
        loanProduct2.setStatus(true);
       
        expectedLoanProducts.add(loanProduct2);

        
        when(loanProductService.searchLoanProducts(criteria)).thenReturn(expectedLoanProducts);

      
        mockMvc.perform(post("/api/v1/loan-product/product/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criteria)))
            .andExpect(status().isOk()) 
            .andExpect(jsonPath("$[0].productName").value("Product A")) 
            .andExpect(jsonPath("$[0].status").value(false)) 
            .andExpect(jsonPath("$[1].productName").value("Product B")) 
            .andExpect(jsonPath("$[1].status").value(true)); 
    }
    
    @Test
    void testGetLoanProductIdByName() throws Exception {
       
        String loanProductName = "Product A";

        Integer expectedLoanProductId = 1001;

        when(loanProductService.getLoanProductIdByProductName(loanProductName)).thenReturn(expectedLoanProductId);

        
        mockMvc.perform(get("/api/v1/loan-product/product/fetch/loan-product-id/{prodName}", loanProductName)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) 
            .andExpect(jsonPath("$").value(expectedLoanProductId)); 
    }
    
    @Test
    void testUpdateLoanProdStatus() throws Exception {
        
        Integer productId = 1001;

        Boolean expectedStatus = true;

        when(loanProductService.updateLoanStatusHistory(productId)).thenReturn(expectedStatus);

        mockMvc.perform(put("/api/v1/loan-product/product/update/loan-status/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) 
            .andExpect(jsonPath("$").value(expectedStatus)); 
    }
    
    @Test
    void testAddPropRestrictions() throws Exception {
      
        Integer productId = 1001;

        List<String> restrictions = Arrays.asList("No pets", "No smoking");

        doNothing().when(loanProductService).addRestrictions(productId, restrictions);

        mockMvc.perform(post("/api/v1/loan-product/product/loan-product/add/prop-rstr/{prodId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restrictions)))
            .andExpect(status().isCreated()) 
            .andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_201)) 
            .andExpect(jsonPath("$.statusMsg").value(AppConstants.PROP_RSTR_ADDED_SUCCESSFULL)); 
    }
    
    @Test
    void testRemovePropRestrictions() throws Exception {
    
        Integer productId = 1001;

        List<String> restrictions = Arrays.asList("No pets", "No smoking");

        doNothing().when(loanProductService).removeRestrictions(productId, restrictions);

        mockMvc.perform(delete("/api/v1/loan-product/product/remove/loan-product-prop-rstr/{prodId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restrictions)))
            .andExpect(status().isOk()) 
            .andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_200)) 
            .andExpect(jsonPath("$.statusMsg").value(AppConstants.PROP_RSTR_REMOVED_SUCCESSFULL)); 
    }
    
    @Test
    void testRemoveEscrowRequirements() throws Exception {
        
        Integer productId = 1001;

        List<String> requirements = Arrays.asList("Insurance", "Taxes");

        doNothing().when(loanProductService).removeEscrowRequirements(productId, requirements);

        mockMvc.perform(delete("/api/v1/loan-product/product/remove/loan-product-escrw-req/{prodId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requirements)))
            .andExpect(status().isOk()) 
            .andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_200)) 
            .andExpect(jsonPath("$.statusMsg").value(AppConstants.ESCROW_REQ_REMOVED_SUCCESSFULL));
    }
    
    @Test
    void testAddEscrowRequirements() throws Exception {
       
        Integer productId = 1001;

        List<String> requirements = Arrays.asList("Escrow insurance", "Escrow property tax");


        doNothing().when(loanProductService).addEscrowRequirementsToLoanProduct(productId, requirements);

        mockMvc.perform(post("/api/v1/loan-product/product/add/loan-product-escrow-req/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requirements)))
            .andExpect(status().isCreated()) 
            .andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_201)) 
            .andExpect(jsonPath("$.statusMsg").value(AppConstants.ESCROW_REQUI_ADDED_SUCCESSFULL));
    }
    
    @Test
    void testAddUnderWritingCriteria() throws Exception {
        
        Integer productId = 1001;

        List<String> criterias = Arrays.asList("Credit score above 700", "Debt-to-income ratio below 40%");

        doNothing().when(loanProductService).addUnderWritingCriteriaToLoanProduct(productId, criterias);

        mockMvc.perform(post("/api/v1/loan-product/product/add/loan-product-criteria/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criterias)))
            .andExpect(status().isCreated()) 
            .andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_201)) 
            .andExpect(jsonPath("$.statusMsg").value(AppConstants.CRITERIA_ADDED_SUCCESSFULL));
    }







}














