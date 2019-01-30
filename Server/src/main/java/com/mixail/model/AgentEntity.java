package com.mixail.model;

public class AgentEntity {
    private String name;
    private String password;
    private int maxCountActiveChat;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxCountActiveChat() {
        return maxCountActiveChat;
    }

    public void setMaxCountActiveChat(int maxCountActiveChat) {
        this.maxCountActiveChat = maxCountActiveChat;
    }

    @Override
    public String toString() {
        return "AgentEntity{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", maxCountActiveChat=" + maxCountActiveChat +
                '}';
    }
}
