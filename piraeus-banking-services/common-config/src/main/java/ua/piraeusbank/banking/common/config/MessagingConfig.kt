package ua.piraeusbank.banking.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import org.zalando.jackson.datatype.money.MoneyModule
import javax.jms.ConnectionFactory


@Configuration
class MessagingConfig {

    @Bean
    fun connectionFactory(connectionFactory: ConnectionFactory,
                          configurer: DefaultJmsListenerContainerFactoryConfigurer): JmsListenerContainerFactory<*> {
        val factory = DefaultJmsListenerContainerFactory()
        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, connectionFactory)
        // You could still override some of Boot's default if necessary.
        return factory
    }

    @Bean
    fun jacksonJmsMessageConverter(): MessageConverter {
        val converter = MappingJackson2MessageConverter()
        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_type")
        val objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()
        objectMapper.registerModule(Jdk8Module())
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.registerModule(MoneyModule())
        converter.setObjectMapper(objectMapper)
        return converter
    }
}