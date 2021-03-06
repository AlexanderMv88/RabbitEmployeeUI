/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.EmployeeUI.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import org.EmployeeUI.entity.Employee;
import org.EmployeeUI.mq.RabbitMqPublisher;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.util.List;

import static com.vaadin.ui.UI.getCurrent;
import static org.EmployeeUI.mq.RabbitEmployee.EMPLOYEE_SELECT_EVENT;
import static org.EmployeeUI.mq.RabbitEmployee.TO_SERVICE_EMPLOYEE_FANOUT_EXCHANGE;
import static org.EmployeeUI.mq.RabbitMqPublisher.createMessage;

;

/**
 *
 * @author a.martyushev
 */

public class MainMenuForm extends Panel implements View{

    private MenuBar menuBar = new MenuBar();
    private VerticalLayout vLayout = new VerticalLayout();
    private Label lbl = new Label();
    public Grid<Employee> employeeGrid = new Grid<Employee>("Пользователи");
    public List<Employee> employees;
    public void setLblTime(String currentTimeStr) {
        this.lbl.setCaption(currentTimeStr);
    }

    public MainMenuForm() {
        employeeGrid.addColumn(Employee::getFullName).setCaption("ФИО");
        Button addBtn = new Button("Добавить", e -> addEmployeeWindow());
        Button changeBtn = new Button("Изменить", e -> changeEmployeeWindow());
        Button deleteBtn = new Button("Удалить", e -> removeEmployee());
        HorizontalLayout hLayout = new HorizontalLayout(addBtn, changeBtn, deleteBtn);
        vLayout.addComponents(lbl, employeeGrid, hLayout);
        this.setContent(vLayout);
    }

    private void removeEmployee() {
        Employee employee;
        if (employeeGrid.asSingleSelect().getValue() != null) {
            employee = employeeGrid.asSingleSelect().getValue();
            RabbitTemplate rabbitTemplate = ((NavigatorUI) getCurrent()).getRabbitTemplate();
            try {
                new RabbitMqPublisher().sendDeleteMessage(rabbitTemplate, employee);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        try {
            getAllEmployeeToGrid();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getAllEmployeeToGrid() throws IOException {
        RabbitTemplate rabbitTemplate = ((NavigatorUI) getCurrent()).getRabbitTemplate();
        rabbitTemplate.setExchange(TO_SERVICE_EMPLOYEE_FANOUT_EXCHANGE);
        Message msgRequest = createMessage(EMPLOYEE_SELECT_EVENT, "");
        Message msg = rabbitTemplate.sendAndReceive(msgRequest);
        String jsonEntities = new String(msg.getBody());
        ObjectMapper mapper = new ObjectMapper();
        CollectionType javaType = mapper.getTypeFactory()
                .constructCollectionType(List.class, Employee.class);

        employees = mapper.readValue(jsonEntities, javaType);
        employeeGrid.setItems(employees);
    }

    private void addEmployeeWindow() throws NullPointerException, IllegalArgumentException {
        EmployeeWindow employeeWindow = new EmployeeWindow();
        getUI().addWindow(employeeWindow);
    }

    private void changeEmployeeWindow() throws NullPointerException, IllegalArgumentException {
        Employee employee;
        if (employeeGrid.asSingleSelect().getValue() != null) {
            employee = employeeGrid.asSingleSelect().getValue();
            EmployeeWindow employeeWindow = new EmployeeWindow(employee);
            getUI().addWindow(employeeWindow);
        }else{
            new Notification("Внимание",
                    "Выберите пользователя в таблице",
                    Notification.Type.ERROR_MESSAGE, true)
                    .show(Page.getCurrent());
        }
    }
}
