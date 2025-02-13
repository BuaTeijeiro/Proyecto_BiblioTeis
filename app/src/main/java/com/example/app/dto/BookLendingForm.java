package com.example.app.dto;

public class BookLendingForm {
    private int bookId;
    private int userId;

    public BookLendingForm() {
    }

    public BookLendingForm(int bookId, int userId) {
        this.bookId = bookId;
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }



}
