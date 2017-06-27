package com.example.lukasz.wypozyczalnia.models;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.print.PageRange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Łukasz on 2016-12-29.
 */

public class BookModel implements Serializable {
    Long isbn;
    String title;
    String publisher;
    String category;
    int quantity;
    List<AuthorModel> authors;
    String description;
    Date add_date;
    transient Bitmap image;

    private static ByteBuffer dst;
    private static byte[] bytesar;

    public BookModel(){
        isbn= new Long(0);
        authors = new ArrayList<>();
        title="";
        publisher="";
        category="";
        description="";
        add_date = new Date();
        quantity=0;
    }

    public BookModel(Long isbn, String title, String publisher, String category, List<AuthorModel> authors, int quantity, String description, Date add_date, Bitmap image) {
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.category = category;
        this.quantity = quantity;
        this.authors = authors;
        this.description = description;
        this.add_date = add_date;
        this.image = image;
        //this.image.setCurrentImage(image);

    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAdd_date() {
        return add_date;
    }

    public void setAdd_date(Date add_date) {
        this.add_date = add_date;
    }

    public Bitmap getImage() { return (Bitmap)image; }

    public void setImage(Bitmap image) { this.image = image; }

    public List<AuthorModel> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorModel> authors) {
        this.authors = authors;
    }

}
