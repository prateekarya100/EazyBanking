package com.tomcat.Cards.auditing;

import com.tomcat.Cards.constants.CardsConstants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("AuditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {
    /**
     * @return
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(CardsConstants.CARD_ISSUER_BANK2);
    }
}
