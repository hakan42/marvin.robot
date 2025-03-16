package com.assetvisor.marvin.persistence.config;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.cognitor.cassandra.migration.spring.CassandraMigrationAutoConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.data.cassandra.core.cql.session.DefaultSessionFactory;

@Configuration
public class CassandraConfig {

    @Bean
    @Qualifier(CassandraMigrationAutoConfiguration.CQL_SESSION_BEAN_NAME)
    public CqlSession cassandraMigrationCqlSession(CqlSessionBuilder cqlSessionBuilder) {
        return cqlSessionBuilder.withKeyspace((CqlIdentifier) null).build();
    }

    @Bean
    @Primary
    @DependsOn(CassandraMigrationAutoConfiguration.MIGRATION_TASK_BEAN_NAME)
    public CqlSession cqlSession(CqlSessionBuilder cqlSessionBuilder) {
        return cqlSessionBuilder.build();
    }

    @Bean
    public CqlTemplate cqlTemplate(CqlSession cqlSession) {
        return new CqlTemplate(new DefaultSessionFactory(cqlSession));
    }
}
