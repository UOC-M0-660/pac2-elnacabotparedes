package edu.uoc.pac2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Book Dao (Data Access Object) for accessing Book Table functions.
 */

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAllBooks(): List<Book>

    @Query("SELECT * FROM book WHERE uid IN (:id)")
    fun getBookById(id: Int): Book?

    @Query("SELECT * FROM book WHERE title LIKE :titleBook")
    fun getBookByTitle(titleBook: String): Book?

    @Insert
    fun saveBook(book: Book): Long

    //New function to update the book
    @Update
    fun updateBook(book: Book)
}