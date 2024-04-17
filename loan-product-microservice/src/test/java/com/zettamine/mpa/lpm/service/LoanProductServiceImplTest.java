package com.zettamine.mpa.lpm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.zettamine.mpa.lpm.constants.AppConstants;
import com.zettamine.mpa.lpm.constants.EscrowConstants;
import com.zettamine.mpa.lpm.entity.LoanProduct;
import com.zettamine.mpa.lpm.exception.ResourceNotFoundException;
import com.zettamine.mpa.lpm.model.LoanReqDto;
import com.zettamine.mpa.lpm.repository.LoanProductRepository;
import com.zettamine.mpa.lpm.service.client.EscrowFeignClient;

class LoanProductServiceImplTest {

    @Mock
    private LoanProductRepository loanProductRepo;

    @Mock
    private EscrowFeignClient escrowClient;

    @InjectMocks
    private LoanProductServiceImpl loanProductService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddEscrowRequirementsToLoanProduct_ValidScenario() {
       
        Integer productId = 1;
        List<String> escrowRequirements = Arrays.asList("EscrowRequirement1", "EscrowRequirement2");

        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setProductId(productId);
        loanProduct.setEscrowRequired(true);

        when(loanProductRepo.findById(productId)).thenReturn(Optional.of(loanProduct));
        when(escrowClient.getAllReq()).thenReturn(ResponseEntity.ok(Arrays.asList("EscrowRequirement1", "EscrowRequirement2")));
        doNothing().when(escrowClient).save(any(LoanReqDto.class));
  
        loanProductService.addEscrowRequirementsToLoanProduct(productId, escrowRequirements);

        assertTrue(loanProduct.getEscrowRequired());
        verify(loanProductRepo).save(loanProduct);
       
    }

    @Test
    void testAddEscrowRequirementsToLoanProduct_LoanProductNotFound() {
      
        Integer productId = 1;
        List<String> escrowRequirements = Arrays.asList("EscrowRequirement1", "EscrowRequirement2");

        when(loanProductRepo.findById(productId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            loanProductService.addEscrowRequirementsToLoanProduct(productId, escrowRequirements);
        });
        assertEquals(String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_ID_MSG, productId), exception.getMessage());
    }

    @Test
    void testAddEscrowRequirementsToLoanProduct_MissingRequirements() {
        
        Integer productId = 1;
        List<String> escrowRequirements = Arrays.asList("InvalidRequirement");

        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setProductId(productId);
        loanProduct.setEscrowRequired(false);

        when(loanProductRepo.findById(productId)).thenReturn(Optional.of(loanProduct));
        when(escrowClient.getAllReq()).thenReturn(ResponseEntity.ok(Arrays.asList("EscrowRequirement1", "EscrowRequirement2")));

        
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            loanProductService.addEscrowRequirementsToLoanProduct(productId, escrowRequirements);
        });
        assertEquals(String.format(EscrowConstants.ESCR_REQ_NOT_EXISTS_MSG, Arrays.asList("InvalidRequirement")), exception.getMessage());
    }
}
