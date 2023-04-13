package com.mathworks.messaging.utilities;

import java.util.HashMap;

public class ExchangeProperties {
    private String name;
    private String type = "topic";
    private boolean create  = true;
    private boolean durable = true;
    private boolean autoDelete = false;
    private boolean internal = false;
    private HashMap<String,Object> arguments = new HashMap<String,Object>();
    
    public HashMap<String, Object> getArguments() {
        return arguments;
    }
    public void setArguments(HashMap<String, Object> arguments) {
        this.arguments = arguments;
    }
    public boolean isCreate() {
        return create;
    }
    public void setCreate(boolean create) {
        this.create = create;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public boolean isDurable() {
        return durable;
    }
    public void setDurable(boolean durable) {
        this.durable = durable;
    }
    public boolean isAutoDelete() {
        return autoDelete;
    }
    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }
    public boolean isInternal() {
        return internal;
    }
    public void setInternal(boolean internal) {
        this.internal = internal;
    }
}
