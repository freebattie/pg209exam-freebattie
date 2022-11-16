package no.kristiania.socialbuzz.dto;

import java.util.List;


//TODO: add back missing icomareble and overwrite
public class Message  {
    private long idMessage;
    private long idChat;
    private User user; //TODO: change to string
    private String message;
    private String timestamp;
    private List<LastRead> lastReads;

    public long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(long idMessage) {
        this.idMessage = idMessage;
    }

    public long getIdChat() {
        return idChat;
    }

    public void setIdChat(long idChat) {
        this.idChat = idChat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<LastRead> getLastReads() {
        return lastReads;
    }

    public void setLastReads(List<LastRead> lastReads) {
        this.lastReads = lastReads;
    }


}
