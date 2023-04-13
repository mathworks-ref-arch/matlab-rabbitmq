package com.mathworks.messaging.utilities;

import java.util.HashMap;

public class QueueProperties {
    private String name;
    private boolean create = true;
    private boolean durable = false;
    private boolean exclusive = false;
    private boolean autoDelete = false;
    private HashMap<String,Object> arguments = new HashMap<String,Object>();


    public HashMap<String, Object> getArguments() {
        return arguments;
    }
    public void setArguments(HashMap<String, Object> arguments) {
        this.arguments = arguments;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isCreate() {
        return create;
    }
    public void setCreate(boolean create) {
        this.create = create;
    }
    public boolean isDurable() {
        return durable;
    }
    public void setDurable(boolean durable) {
        this.durable = durable;
    }
    public boolean isExclusive() {
        return exclusive;
    }
    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }
    public boolean isAutoDelete() {
        return autoDelete;
    }
    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

}
