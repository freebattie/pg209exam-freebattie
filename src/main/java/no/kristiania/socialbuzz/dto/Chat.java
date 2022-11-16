package no.kristiania.socialbuzz.dto;

import java.util.List;

public class Chat implements Comparable<Chat>{
    private long id_chat;
    private String title;
    private List<Integer> usersId;
    public long getId_chat() {
        return id_chat;
    }

    public void setId_chat(long id_chat) {
        this.id_chat = id_chat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public int compareTo(Chat chat) {
        return (int) (this.getId_chat() - chat.getId_chat());
    }

    public List<Integer> getUsersListId() {

        return usersId;
    }


}
