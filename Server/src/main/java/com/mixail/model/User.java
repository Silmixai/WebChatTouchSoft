package com.mixail.model;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class User {

    private Integer id;
    private String name;
    private String password;
    private static AtomicInteger isGenId = new AtomicInteger();
    private Session userSession;
    private TypeOfUser typeOfUser;
    private User userForChat;
    private Status status;
    private int maxCountActiveChat;
    private HashMap<Integer, User> interlocutors = new HashMap<>();
    boolean ConsoleAgent = false;
    private CopyOnWriteArrayList<String> firstMessages = new CopyOnWriteArrayList<>();

    public boolean isConsoleAgent() {
        return ConsoleAgent;
    }

    private boolean isRestUser;
    private HttpSession RestSession;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HttpSession getRestSession() {
        return RestSession;
    }

    public void setRestSession(HttpSession restSession) {
        RestSession = restSession;
    }

    public boolean isRestUser() {
        return isRestUser;
    }

    public void setRestUser(boolean restUser) {
        isRestUser = restUser;
    }

    public CopyOnWriteArrayList<String> getFirstMessages() {
        return firstMessages;
    }

    public void setFirstMessages(CopyOnWriteArrayList<String> firstMessages) {
        this.firstMessages = firstMessages;
    }

    public void setConsoleAgent(boolean consoleAgent) {
        ConsoleAgent = consoleAgent;
    }

    public HashMap<Integer, User> getInterlocutors() {
        return interlocutors;
    }

    public Integer getInterlocutorsCount() {
        return interlocutors.size();
    }

    public void addClientToRoom(int id, User user) {
        interlocutors.put(id, user);
    }

    public User getClientFromRoom(int id) {

        return interlocutors.get(id);
    }


    public void removeClientFromRoom(int id) {
        interlocutors.remove(id);
    }

    public void setMaxCountActiveChat(int maxCountActiveChat) {
        this.maxCountActiveChat = maxCountActiveChat;
    }


    public int getMaxCountActiveChat() {
        return maxCountActiveChat;
    }

    public boolean ready() {
        return getMaxCountActiveChat() > getInterlocutorsCount();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUserForChat() {
        return userForChat;
    }

    public void setUserForChat(User userForChat) {
        this.userForChat = userForChat;
    }

    public TypeOfUser getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(TypeOfUser typeOfUser) {
        this.typeOfUser = typeOfUser;
    }

    public Session getUserSession() {
        return userSession;
    }

    public void setUserSession(Session userSession) {
        this.userSession = userSession;
    }

    public User() {
        id = isGenId.incrementAndGet();
    }

    public Integer getId() {
        return id;
    }


    public static AtomicInteger getIsGenId() {
        return isGenId;
    }

    public static void setIsGenId(AtomicInteger isGenId) {
        User.isGenId = isGenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", typeOfUser=" + typeOfUser +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(userSession, user.userSession) &&
                typeOfUser == user.typeOfUser &&
                Objects.equals(userForChat, user.userForChat) &&
                status == user.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userSession, typeOfUser, userForChat, status);
    }
}
