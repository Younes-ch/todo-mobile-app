package com.example.todoapp.Models;

public class ToDoModel
{
    private int id;
    private final String title;
    private final int status;
    private final int user_id;

    public ToDoModel(int id, String title, int status, int user_id) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
