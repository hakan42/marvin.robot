package com.assetvisor.marvin.persistence.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.data.cassandra.core.cql.session.DefaultSessionFactory;

@Configuration
public class CassandraConfig {

    @Value("${spring.cassandra.contact-points}")
    private String contactPoints;
    @Value("${spring.cassandra.keyspace-name}")
    private String keyspaceName;
    @Value("${spring.cassandra.local-datacenter}")
    private String localDatacenter;

    @Bean
    public CqlSessionFactoryBean cqlSessionFactoryBean() {
        CqlSessionFactoryBean sessionFactoryBean = new CqlSessionFactoryBean();
        sessionFactoryBean.setContactPoints(contactPoints);
        sessionFactoryBean.setKeyspaceName(keyspaceName);
        sessionFactoryBean.setLocalDatacenter(localDatacenter);
        return sessionFactoryBean;
    }

    @Bean
    public CqlTemplate cqlTemplate(CqlSessionFactoryBean cqlSessionFactoryBean) throws Exception {
        return new CqlTemplate(new DefaultSessionFactory(cqlSessionFactoryBean.getObject()));
    }
}
