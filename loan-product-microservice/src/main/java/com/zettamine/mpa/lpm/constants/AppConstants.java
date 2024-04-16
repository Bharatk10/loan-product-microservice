package com.zettamine.mpa.lpm.constants;

public interface AppConstants {

	public static final String STATUS_201 = "201";

	public static final String CREATE_PROPERTY_RSTR_MESSAGE = "Property Restriction created successfully";

	public static final String UPDATE_PROPERTY_RSTR_MESSAGE = "Property Restriction Updated successfully";

	public static final String CREATE_PROPERTY_RSTR_CATG_MESSAGE = "Property Restriction Category created successfully";

	public static final String UPDATE_PROPERTY_RSTR_CATG_MESSAGE = "Property Restriction Category updated successfully";

	public static final String STATUS_200 = "200";

	public static final String UPDATE_MESSAGE = "Update Request Processed Successfully";

	public static final String DELETE_MESSAGE = "Delete Request Processed Successfully";

	public static final String PROP_RSTR_EXISTS_MSG = "Property Restriction Already Exists (%s) with the Restriction Type";
	
	public static final String PROP_RSTR_NOT_EXISTS_MSG = "Property Restriction not found with the given input data Restriction Type : %s";
	
	public static final String PROP_RSTR_NOT_EXISTS_BY_ID_MSG = "Property Restriction not found with the given input data Restriction Id : %s";
	
	public static final String LOAN_PROD_NOT_EXISTS_BY_ID_MSG = "Loan Product not found with the given input data Loan Product Id : %s";

	public static final String PROP_RSTR_CATG_NOT_EXISTS_MSG = "Property Restriction Category not found with the given input data Category Type : %s";
	
	public static final String PROP_RSTR_CATG_NOT_EXISTS_BY_ID_MSG = "Property Restriction Category not found with the given input data Category Id : %s";
	
	public static final String PROP_RSTR_CATG_EXISTS_MSG = "Property Restriction Category Already Exists (%s) With the Category Type";
	
	public static final String LOAN_PRODUCT_EXISTS_MSG = "Loan Product Already Exists (%s) with the Product Name";

	public static final String PROP_RSTR_CATG = "Property Restriction Category";
	
	public static final String LOAN_PRODUCT = "Loan Product";

	public static final String PROP_RSTR = "Property Restriction";

	public static final String PROP_RSTR_TYPE = "Restriction Type";

	public static final String PROP_RSTR_ID = "Restriction Id";
	
	public static final String LOAN_PRODUCT_NAME = "Loan Product Name";

	public static final String PROP_CATG_ID = "Category Id";

	public static final String PROP_CATG_TYPE = "Category Type";
	
	public static final String LOCKIN_PERIOD = "LockIn Period";
	
	public static final String MINDOWN_PAYMENT = "Minimum Down Paymnet";

	public static final Character ACTIVE = 'A';

	public static final Character INACTIVE = 'I';

	public static final String ABSOLUTE = "ABSOLUTE";

	public static final String PERCENTAGE = "PERCENTAGE";

	public static final String MINDOWN_PAYMENT_TYPE = "Minimum Down Paymnet Type";
	
	public static final String MIN_PENALITY_AMOUNT = "Minimum Penality Amount";
	
	public static final String PENALITY_PERCENTAGE = "Prepay Penalty Percentage";

	public static final String ORIGINATION_FEE = "Origination Fee";

	public static final Boolean STATUS_TRUE = true;

	public static final Boolean STATUS_FALSE = false;

	public static final String CREATE_LOAN_PRODUCT_MESSAGE = "Loan Product created Successfully";

	public static final String LOAN_PRODUCT_STATUS_MESSAGE = "Loan Product status updated successfully";

	public static final String LOAN_PROD_NOT_ACTIVE = "Loan Product for %s is never been active since it's Created";

	public static final Object END_DATE = "+10000-01-01T00:00";

	public static final Object CURENTLY_ACTIVE = "Currently Active";

	public static final String PRE_PAY_PENALITY_NOT_ACTIVE = "Pre Pay Penality is not active for the Loan Product:%s since it's created ";

	public static final String LOAN_PRODUCT_PENALITY_STATUS_MESSAGE = "Loan Product PrePay Penality status updated successfully";

	public static final String LOAN_PRODUCT_PENALITY_MESSAGE = "Loan Product PrePay Penality updated successfully";

	public static final String PRE_PAY_PENALITY_UPDATE_ERROR = "First Create Prepay Penality for the Loan Product: %s";

	public static final String PRE_PAY_PENALITY_UPDATE_FALSE_ERROR = "First Make the PrePay Penality  status true before updating";

	public static final String LOAN_PRODUCT_PENALITY_CREATED_MESSAGE = "Pre Pay Penality created ";

	public static final String PREPAY_PENALITY_EXITS = "Already Prepay Penality Exits with Loan Product : %s";
	
	public static final String LOAN_PROD_ID = "Loan Product Id";

	public static final String UPDATE_LOAN_PRODUCT_MESSAGE = "Loan Product updated Successfully";

	public static final String PROP_RSTR_TYPES_NOT_EXIST = "Property Restriction types are Not exits %s";
	
	public static final String LOAN_PROD_NOT_EXISTS_BY_NAME_MSG = "Loan Product not found with the given input data Loan Product Name : %s";
	
	public static final String PROP_RSTR_ADDED_SUCCESSFULL = "Property restrictions successfully added to loan product";

	public static final String PROP_RSTR_REMOVED_SUCCESSFULL = "Property restrictions successfully removed to loan product";

	public static final String PROP_RSTR_NOT_EXISTS = "Property restrictions are not exists %s with loan product %s";

	public static final String RESTS_EXITS = "These %s restrictions already associated with the given productId: %s";
	
	public static final String PREPAY_PENALTY_NOT_EXISTS_MSG = "prepay penalty details are not found for given loan product id : %s";

	public static final String PREPAY_PENALTY_ACTIVE_MSG = "prepay penalty is currently in ACTIVE state  for given loan product id : %s";

	public static final String PREPAY_PENALTY_INACTIVE_MSG = "prepay penalty is currently Not in INACTIVE state  for given loan product id : %s";

	public static final String CRI_REQ_NOT_EXISTS_MSG = "Underwriting Criteria not found with the given input data Criteria Name : %s";
	
	public static final String LOAN_PROD_NOT_FOUND_WITH_ID = "Loan Product not found with given loan product id : %s";

	public static final String ESCROW_REQ_REMOVED_SUCCESSFULL = "Escrow requirements removed successfully";

	public static final String ESCROW_REQUI_ADDED_SUCCESSFULL = "Escrow requirements added successfully";

	public static final String CRITERIA_ADDED_SUCCESSFULL = "Under Writing criteria added successully";

	public static final String RESOURCE_EMPTY_OR_NULL = "Resource List of data is empty or null";

	public static final String ESCROW_REQ_NOT_EXISTS = "No escrow requirements are exists for loan product : %s";
	
	public static final String RSTR_UPDATED_SUCCESS = "Restriction Type updated successfully";

	public static final String PROP_RSTR_TYPE_ALREADY_EXISTS = "%s property restrictions type is alredy exist";
	
}