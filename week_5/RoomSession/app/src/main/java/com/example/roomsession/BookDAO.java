package com.example.roomsession;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

//Book Data Access Object
@Dao
public interface BookDAO {

    @Query("SELECT * FROM Book")
    List<Book> getAllBooks();

    @Query("SELECT * FROM Book WHERE book_author = :author")
    List<Book> getBooksByAuthor(String author);

    @Insert // there is no need to worry about SQL as we explain everything in the class Book
    void insertBooks(Book... book); // the Book... means insert one or many

    @Delete // there is no need to worry about SQL as we explain everything in the class Book
    void deleteBooks(Book... books);

    @Query("DELETE FROM Book")
    void deleteAllBooks();
}
