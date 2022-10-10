package com.example.roomsession;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.room.Room;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {


    private BookDatabase bookDatabase;
    private ExecutorService executorService;
    private TextView textView;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

        // build a BookDatabase
        bookDatabase = Room.databaseBuilder(
                this,
                BookDatabase.class,
                "MyBookDatabase"
        ).build();

        // create a means of running database operations in a background thread
        executorService = Executors.newSingleThreadExecutor();
        // create an object that enables code to be ran on the UI thread
        handler = HandlerCompat.createAsync(Looper.getMainLooper());


        // create a means of running database operations in a background thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // do database operations here

                // clear the book table
                bookDatabase.bookDAO().deleteAllBooks();

                // insert some books
                bookDatabase.bookDAO().insertBooks(
                        new Book("Book A", "Author A"),
                        new Book("Book B", "Author B"),
                        new Book("Book C", "Author A")
                );

                // query to get all books back out at a List of Book objects
                List<Book> allBooks = bookDatabase.bookDAO().getAllBooks();
                final Book example = allBooks.get(0);
                Log.d("DATA", allBooks.toString());
                for (Book book : allBooks) {
                    Log.d("BOOK DATA "+book.getId(), book.getName());
                }

                // get all books by author
                List<Book> bookListByAuthor = bookDatabase.bookDAO().getBooksByAuthor("Author A");
                Log.d("DATA", bookListByAuthor.toString());

//                // run this block of code back in the UI thread
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        textView.setText(example.getName());
//                    }
//                });
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // anything to do with the UI
                        textView.setText(example.getName());
                    }
                });

            }
        });
    }

}
