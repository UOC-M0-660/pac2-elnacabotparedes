package edu.uoc.pac2.data

import android.util.Log
import androidx.room.Update

/**
 * This class Interacts with {@param bookDao} to perform operations in the local database.
 *
 * Could be extended also to interact with Firestore, acting as a single entry-point for every
 * book-related operation from all different datasources (Room & Firestore)
 *
 * Created by alex on 03/07/2020.
 */
class BooksInteractor(private val bookDao: BookDao) {

    // TODO: Get All Books from DAO
    fun getAllBooks(): List<Book> {
        return bookDao.getAllBooks()
    }

    // TODO: Save Book
    fun saveBook(book: Book) {
        bookDao.saveBook(book)
    }

    // TODO: Save List of Books
    fun saveBooks(books: List<Book>) {
        //books.forEach { saveBook(it) }

        var listBook = this.getAllBooks()
        var exist: Boolean = false;

        for(book: Book in books)
        {
            var exist: Boolean = false

            for(list: Book in listBook)
            {
                if(book.uid == list.uid)
                {
                    Log.d("TAG", "EXIST ELEMENT")
                    updateBook(book)
                    exist = true
                }
            }

            //No exist then insert
            if( !exist)
            {
                Log.d("TAG", "NO EXIST")
                saveBook(book);
            }
        }
    }

    // TODO: Get Book by id
    fun getBookById(id: Int): Book? {
        return bookDao.getBookById(id)
    }

    fun updateBook(book: Book)
    {
        bookDao.updateBook(book)
    }

}