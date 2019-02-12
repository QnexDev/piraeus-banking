package ua.piraeusbank.banking.common.config

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.h2.Driver
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.Database
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.zalando.jackson.datatype.money.MoneyModule
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = ["ua.piraeusbank.banking"])
@EntityScan("ua.piraeusbank.banking")
class PersistenceConfig {

    @Bean
    fun objectMapperBuilder(): Jackson2ObjectMapperBuilder {
        val builder = Jackson2ObjectMapperBuilder()
        builder.modules(Jdk8Module(), JavaTimeModule(), MoneyModule(), KotlinModule())
        return builder
    }

    @Bean
    fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val vendorAdapter = HibernateJpaVendorAdapter()
        vendorAdapter.setDatabase(Database.H2)
        vendorAdapter.setGenerateDdl(true)

        val emf = LocalContainerEntityManagerFactoryBean()
        emf.dataSource = dataSource
        emf.setPackagesToScan("ua.piraeusbank.banking")
        emf.jpaVendorAdapter = vendorAdapter
        emf.setJpaProperties(hibernateProperties())

        return emf
    }

    @Bean
    fun dataSource(@Value("\${db.connection.url}") url: String,
                   @Value("\${db.connection.username}") username: String,
                   @Value("\${db.connection.password}") password: String): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(Driver::class.java.name)
        dataSource.url = url
        dataSource.username = username
        dataSource.password = password
        return dataSource
    }

    @Bean
    fun transactionManager(entityManagerFactory: LocalContainerEntityManagerFactoryBean): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactory.getObject()
        return transactionManager
    }

    @Bean
    fun exceptionTranslation(): PersistenceExceptionTranslationPostProcessor {
        return PersistenceExceptionTranslationPostProcessor()
    }

    private fun hibernateProperties(): Properties {
        val hibernateProperties = Properties()

        hibernateProperties.setProperty("hibernate.show_sql", "true")
        hibernateProperties.setProperty("hibernate.format_sql", "true")
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop")
        hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true")

        return hibernateProperties}
    }

