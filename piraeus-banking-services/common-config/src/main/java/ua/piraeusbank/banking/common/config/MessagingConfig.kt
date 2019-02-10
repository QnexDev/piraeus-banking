package ua.piraeusbank.banking.common.config

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
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
        return converter
    }
}