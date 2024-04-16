package com.zettamine.mpa.lpm.model;

import java.util.List;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(name = "Loan Requirments", description = "Schema to hold the data for add and delete the requirments for the loan product")
@AllArgsConstructor
public class LoanReqDto {
 
   
    @Schema(description = "The ID of the loan product.", example = "12345")
    private Integer prodId;
    
  
    @Schema(description = "The list of requirements for the loan product.", example = "[\"PROPERTY TAX PENALTY PAYMENTS\", \"PROPERTY TAXES\"]")
    private List<String> requirements;
    
}

