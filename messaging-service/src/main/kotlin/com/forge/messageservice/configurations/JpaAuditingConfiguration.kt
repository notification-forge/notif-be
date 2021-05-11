package com.forge.messageservice.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@Configuration
@EnableJpaAuditing
class JpaAuditingConfiguration{

    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return LoggedInUsernameAuditorAware()
    }
}

class LoggedInUsernameAuditorAware: AuditorAware<String>{
    override fun getCurrentAuditor(): Optional<String> {
        //TODO: Change when we have LDAP
        return Optional.of("System")
    }
}