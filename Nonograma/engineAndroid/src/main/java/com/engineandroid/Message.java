package com.engineandroid;

public class Message {
    MESSAGE_TYPE type;
    public int reward;
    public Message(MESSAGE_TYPE type){
        this.type = type;
    }
    public MESSAGE_TYPE getType(){
        return type;
    }
}
