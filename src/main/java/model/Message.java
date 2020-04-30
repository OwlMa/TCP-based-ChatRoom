package model;

import java.io.Serializable;

public class Message implements Serializable{
    private String type; // personal or group or else
    private String Sender;
    private String Getter;
    private String Content;
    private Long Time;

    public Message(){

    }

    public Message(String sender, String getter, String content, Long time) {
        Sender = sender;
        Getter = getter;
        Content = content;
        Time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getGetter() {
        return Getter;
    }

    public void setGetter(String getter) {
        Getter = getter;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Long getTime() {
        return Time;
    }

    public void setTime(Long time) {
        Time = time;
    }
}
