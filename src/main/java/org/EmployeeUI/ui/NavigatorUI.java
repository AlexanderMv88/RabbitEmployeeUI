/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.EmployeeUI.ui;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

import org.EmployeeUI.Broadcaster;
import org.EmployeeUI.WebSocketMsg;
import org.EmployeeUI.entity.Employee;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.io.IOException;


/**
 *
 * @author user
 */
@Component
@Push
@SpringUI
@Theme("valo")
public class NavigatorUI extends UI implements Broadcaster.BroadcastListener {

    private MainMenuForm mainMenuForm = new MainMenuForm();
    Navigator navigator;

    public static final String MAIN_MENU_FORM = "mainMenuForm";
    public RestTemplate restTemplate = new RestTemplate();

    private RabbitTemplate rabbitTemplate;

    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }

    @Autowired
    public NavigatorUI(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    protected void init(VaadinRequest request) {
        restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("Alexander", "12345"));
        navigator = new Navigator(this, this);
        navigator.addView(MAIN_MENU_FORM, mainMenuForm);
        navigator.navigateTo(MAIN_MENU_FORM);
        Broadcaster.register(this);

    }

    @PreDestroy
    private void destroy() {
        Broadcaster.unregister(this);
        this.destroy();
    }

    public Navigator getNavigator() {
        return navigator;
    }


    @Override
    public void receiveBroadcast(WebSocketMsg message) {
        getUI().access(() -> {

            switch (message.getMsgType()) {
                case EMPLOYEE_DELETE:
                    System.out.println("Получил сообщение в UI = " + message);
                    String jsonEmployee = message.getText();
                    Employee employee = null;
                    try {
                        employee = new ObjectMapper().readValue(jsonEmployee, Employee.class);
                        mainMenuForm.employees.remove(employee);
                        mainMenuForm.employeeGrid.setItems(mainMenuForm.employees);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        });
    }
}
