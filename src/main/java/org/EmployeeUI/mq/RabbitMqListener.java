package org.EmployeeUI.mq;

import com.vaadin.ui.UI;
import org.EmployeeUI.Broadcaster;
import org.EmployeeUI.WebSocketMsg;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

import static org.EmployeeUI.mq.RabbitEmployee.EMPLOYEE_CREATED_EVENT;
import static org.EmployeeUI.mq.RabbitEmployee.EMPLOYEE_DELETED_EVENT;
import static org.EmployeeUI.mq.RabbitEmployee.FROM_SERVICE_EMPLOYEE_EVENT_QUEUE;
import static com.vaadin.ui.UI.getCurrent;

@EnableRabbit //нужно для активации обработки аннотаций @RabbitListener
@Component
public class RabbitMqListener {


    @RabbitListener(queues = FROM_SERVICE_EMPLOYEE_EVENT_QUEUE)
    public void fromServiceEmployee(Message message){
        System.out.println("get message "+message);
        String action = (String) message.getMessageProperties().getHeaders().get("action");
        String body = null;
        try {
            body = new String(message.getBody(), "UTF-8");
            if (EMPLOYEE_CREATED_EVENT.equals(action)) {
                System.out.println("get message with action "+action+" "+body);
            }else if (EMPLOYEE_DELETED_EVENT.equals(action)) {
                System.out.println("get message with action "+action+" "+body);
                WebSocketMsg wsMsg = new WebSocketMsg(WebSocketMsg.MsgType.EMPLOYEE_DELETE);
                wsMsg.setText(body);
                Broadcaster.broadcast(wsMsg);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    }

