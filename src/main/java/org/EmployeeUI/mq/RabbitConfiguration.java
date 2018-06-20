package org.EmployeeUI.mq;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.GregorianCalendar;

import static org.EmployeeUI.mq.RabbitEmployee.*;


@Configuration
public class RabbitConfiguration {


    private static final long cal = GregorianCalendar.getInstance().getTimeInMillis();

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory("172.16.175.128");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }


    @Bean
    public Queue employeeEventQueue() {

        System.out.println("Ожидаю сообщений о событиях CRUD на канале: "+ FROM_SERVICE_EMPLOYEE_EVENT_QUEUE);
        return new Queue(FROM_SERVICE_EMPLOYEE_EVENT_QUEUE);
    }



    @Bean
    public FanoutExchange toServiceEmployeeFanoutExchange(){
        return new FanoutExchange(TO_SERVICE_EMPLOYEE_FANOUT_EXCHANGE);
    }


    @Bean
    public Queue fromServiceEmployeeEventQueue() {
        return new Queue(FROM_SERVICE_EMPLOYEE_EVENT_QUEUE);
    }

    @Bean
    public FanoutExchange fromServiceEmployeeFanoutExchange(){
        return new FanoutExchange(FROM_SERVICE_EMPLOYEE_FANOUT_EXCHANGE);
    }

    @Bean
    public Binding bindingQueueToFanoutExchange(){
        return BindingBuilder.bind(fromServiceEmployeeEventQueue()).to(fromServiceEmployeeFanoutExchange());
    }



}