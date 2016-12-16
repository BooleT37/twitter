package ru.urfu.models;


public class MessageJson {
    private long id;

    private String content;

    private String userLogin;

    public MessageJson() {
    }

    public MessageJson(long id, String content, String userLogin) {
        this.id = id;
        this.content = content;
        this.userLogin = userLogin;
    }

    public MessageJson(String content, String userLogin) {
        this.content = content;
        this.userLogin = userLogin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
