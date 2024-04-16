package com.zettamine.mpa.lpm.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<Integer> {

	@Override
	public Optional<Integer> getCurrentAuditor() {
		
		return Optional.of(1);
	}
	 

}
