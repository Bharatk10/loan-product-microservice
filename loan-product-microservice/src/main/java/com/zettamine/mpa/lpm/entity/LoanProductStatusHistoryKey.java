package com.zettamine.mpa.lpm.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class LoanProductStatusHistoryKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private LoanProduct loanProduct;

	private LocalDateTime startDate;

	public LoanProductStatusHistoryKey(LoanProduct loanProduct, LocalDateTime startDate) {
		this.loanProduct = loanProduct;
		this.startDate = startDate;
	}

	public LoanProductStatusHistoryKey() {
	}

	@Override
	public int hashCode() {
		return Objects.hash(loanProduct, startDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoanProductStatusHistoryKey other = (LoanProductStatusHistoryKey) obj;
		return Objects.equals(loanProduct, other.loanProduct) && Objects.equals(startDate, other.startDate);
	}

}
