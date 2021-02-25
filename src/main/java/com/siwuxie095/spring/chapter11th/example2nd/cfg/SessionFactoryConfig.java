package com.siwuxie095.spring.chapter11th.example2nd.cfg;

import com.siwuxie095.spring.chapter11th.example2nd.domain.Spitter;
import com.siwuxie095.spring.chapter11th.example2nd.domain.Spittle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Jiajing Li
 * @date 2021-02-24 22:19:48
 */
@SuppressWarnings("all")
@Configuration
public class SessionFactoryConfig {

    /*
     * Hibernate 3 中的 LocalSessionFactoryBean
     */
    //@Bean
    //public LocalSessionFactoryBean localSessionFactory(DataSource dataSource) {
    //    LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
    //    sfb.setDataSource(dataSource);
    //    sfb.setMappingResources(new String[] { "Spitter.hbm.xml" });
    //    Properties props = new Properties();
    //    props.setProperty("dialect", "org.hibernate.dialect.H2Dialect");
    //    sfb.setHibernateProperties(props);
    //    return sfb;
    //}


    /*
     * Hibernate 3 中的 AnnotationSessionFactoryBean
     */
    //@Bean
    //public AnnotationSessionFactoryBean annotationSessionFactory(DataSource dataSource) {
    //    AnnotationSessionFactoryBean sfb = new AnnotationSessionFactoryBean();
    //    sfb.setDataSource(dataSource);
    //    sfb.setPackagesToScan(new String[] { "com.habuma.spittr.domain" });
    //    Properties props = new Properties();
    //    props.setProperty("dialect", "org.hibernate.dialect.H2Dialect");
    //    sfb.setHibernateProperties(props);
    //    return sfb;
    //}


    /*
     * Hibernate 4 中的 LocalSessionFactoryBean
     */
    @Bean
    public LocalSessionFactoryBean sessionFactoryBean(DataSource dataSource) {
        LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
        sfb.setDataSource(dataSource);
        sfb.setPackagesToScan(new String[] { "com.habuma.spittr.domain" });
        Properties props = new Properties();
        props.setProperty("dialect", "org.hibernate.dialect.H2Dialect");
        sfb.setHibernateProperties(props);
        return sfb;
    }


}
