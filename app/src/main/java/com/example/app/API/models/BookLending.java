package com.example.app.API.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookLending implements Comparable<BookLending>{
    public static final int LENDING_PERIOD = 15;
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

    public Calendar getDateFormat(String date){
        String[] period = date.substring(0,10).split("-");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(period[0]), Integer.parseInt(period[1]) - 1, Integer.parseInt(period[2]));
        return c;
    }

    public String getDateString(String string){
        Calendar c = getDateFormat(string);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        return dateFormat.format(c.getTime());
    }

    public Calendar computeDueDate(){
        Calendar c = getDateFormat(getLendDate());
        c.add(Calendar.DAY_OF_MONTH, LENDING_PERIOD);
        return c;
    }

    public String getDueDate(){
        Calendar c = computeDueDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        return dateFormat.format(c.getTime());
    }

    public boolean isLate(){
        Calendar today = Calendar.getInstance();
        Calendar dueDate = computeDueDate();
        return today.after(dueDate);
    }

    @Override
    public int compareTo(BookLending other) {
        if (getReturnDate() == null){
            if (other.getReturnDate() == null){
                return computeDueDate().compareTo(other.computeDueDate());
            } else {
                return 1;
            }

        } else {
            if (other.getReturnDate() == null){
                return -1;
            } else {
                return getDateFormat(getReturnDate()).compareTo(other.getDateFormat(other.getReturnDate()));
            }
        }
    }
}