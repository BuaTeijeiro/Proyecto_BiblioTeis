package com.example.app.API.models;

import java.time.LocalDate;

public class BookLending {
    private int id;
    private int bookId;
    private int userId;
    private String lendDate;
    private String returnDate;

    private User user;
    private Book book;

    // Getters & Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLendDate() {
        return lendDate;
    }

    public void setLendDate(String lendDate) {
        this.lendDate = lendDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

//    public String computeReturnDate(){
//        String[] period = getLendDate().substring(0,10).split("-");
//        LocalDate date = LocalDate.of(Integer.parseInt(period[0]), Integer.parseInt(period[1]), Integer.parseInt(period[2]));
//        date.plusDays(15);
//    }
}