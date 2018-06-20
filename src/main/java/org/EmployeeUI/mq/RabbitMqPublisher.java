package org.EmployeeUI.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.EmployeeUI.entity.Employee;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.EmployeeUI.mq.RabbitEmployee.*;

public class RabbitMqPublisher {
    public static Message createMessage(String action, String payload){
        return MessageBuilder.withBody(payload.getBytes())
                .setHeader("action", action)
                .build();

    }

    public void sendUpdateMessage(RabbitTemplate rabbitTemplate, Employee oldEmployee, Employee newEmployee) throws JsonProcessingException {
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(oldEmployee);
        employees.add(newEmployee);
        String jsonEmployeeForUpdate= new ObjectMapper().writeValueAsString(employees);
        Message msg = createMessage(EMPLOYEE_UPDATE_EVENT, jsonEmployeeForUpdate);
        rabbitTemplate.setExchange(TO_SERVICE_EMPLOYEE_FANOUT_EXCHANGE);
        rabbitTemplate.send(msg);
    }

    public void sendDeleteMessage(RabbitTemplate rabbitTemplate, Employee employee) throws JsonProcessingException {
        String jsonEmployeeForRemove= new ObjectMapper().writeValueAsString(employee);
        Message msg = createMessage(EMPLOYEE_DELETE_EVENT, jsonEmployeeForRemove);
        rabbitTemplate.setExchange(TO_SERVICE_EMPLOYEE_FANOUT_EXCHANGE);
        rabbitTemplate.send(msg);
    }

    public void sendCreateMessage(RabbitTemplate rabbitTemplate, Employee employee) throws JsonProcessingException {
        String jsonEmployeeForInsert= new ObjectMapper().writeValueAsString(employee);
        Message msg = createMessage(EMPLOYEE_CREATE_EVENT, jsonEmployeeForInsert);
        rabbitTemplate.setExchange(TO_SERVICE_EMPLOYEE_FANOUT_EXCHANGE);
        rabbitTemplate.send(msg);
    }
}
