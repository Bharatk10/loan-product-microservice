package com.zettamine.mpa.lpm.util;


import org.springframework.data.jpa.domain.Specification;

import com.zettamine.mpa.lpm.entity.LoanProduct;
import com.zettamine.mpa.lpm.entity.PropertyRestriction;
import com.zettamine.mpa.lpm.entity.PropertyRestrictionCategory;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class LoanProductSpecifications {
	
	public static Specification<LoanProduct> hasLoanTermLessThan(Integer loanTerm) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("loanTerm"), loanTerm);
    }

    public static Specification<LoanProduct> hasCreditScoreGreaterThan(Integer creditScore) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("minimumCreditScore"), creditScore);
    }
    
    public static Specification<LoanProduct> hasPrivateMortgageInsurance(Boolean status){
    	return (root,query,CriteriaBuilder) -> CriteriaBuilder.equal(root.get("privateMortgageInsuranceRequired"),status);
    }
    public static Specification<LoanProduct> hasRestrictionCategoryType(String categoryType) {
        return (root, query, criteriaBuilder) -> {
          
            Join<LoanProduct, PropertyRestriction> restrictionJoin = root.join("propertyRestrcitions", JoinType.LEFT);
            
            Join<PropertyRestriction, PropertyRestrictionCategory> restrictionCategoryJoin = restrictionJoin.join("restrictionCategory", JoinType.LEFT);
            
            Predicate predicate = criteriaBuilder.equal(restrictionCategoryJoin.get("categoryType"), categoryType);
            
            return predicate;
        };
    }
    public static Specification<LoanProduct> hasRestrictionType(String restrictionType) {
        return (root, query, criteriaBuilder) -> {
          
            Join<LoanProduct, PropertyRestriction> restrictionJoin = root.join("propertyRestrcitions", JoinType.LEFT);
            
            Predicate predicate = criteriaBuilder.equal(restrictionJoin.get("restrictionType"), restrictionType);
            
            return predicate;
        };
    }
    public static Specification<LoanProduct> hasStatus(Boolean status){
    	return (root,query,CriteriaBuilder) -> CriteriaBuilder.equal(root.get("status"),status);
    }
    public static Specification<LoanProduct> hasPrepayPenalityStatus(Boolean status){
    	return (root,query,CriteriaBuilder) -> CriteriaBuilder.equal(root.get("prepayPenalty"),status);
    }
    

}
