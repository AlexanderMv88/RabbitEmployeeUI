package org.EmployeeUI;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cucumber.api.java.it.Data;
import org.EmployeeUI.entity.Employee;




public class WebSocketMsg {
    public enum MsgType {EMPTY, EMPLOYEE_DELETE, EMPLOYEE_CREATE, EMPLOYEE_UPDATE}
    private MsgType msgType;

    private String status = "";
    private String sendto = "";
    private String text = "";
    private String username = "";

    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));


    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSendto() {
        return sendto;
    }

    public void setSendto(String sendto) {
        this.sendto = sendto;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Employee getEmpl() {
        return empl;
    }

    public void setEmpl(Employee empl) {
        this.empl = empl;
    }

    private Employee empl;

    public WebSocketMsg(String status, String sendto, String text, String username) {

        this.status = status;
        this.sendto = sendto;
        this.text = text;
        this.username = username;
        this.msgType = msgType.EMPTY;




    }

    public WebSocketMsg(String status) {
        this.status = status;
        this.msgType = msgType.EMPTY;
    }


    public WebSocketMsg(String status, String text) {
        this.status = status;
        this.text = text;
        this.msgType = msgType.EMPTY;
    }


    public WebSocketMsg() {
    }

    public WebSocketMsg(MsgType msgType) {
        this.msgType = msgType;
    }




    public String toString(){
        return this.date + " " +this.time+ " Статус: "+this.status+" Текст: "+this.text;
    }


}

