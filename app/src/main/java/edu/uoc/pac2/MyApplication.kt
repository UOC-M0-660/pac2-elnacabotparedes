package edu.uoc.pac2

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import androidx.room.Room
import edu.uoc.pac2.data.*

/**
 * Entry point for the Application.
 */
class MyApplication : Application() {

    private lateinit var booksInteractor: BooksInteractor

    override fun onCreate() {
        super.onCreate()

        // TODO: Init Room Database
        val db = Room.databaseBuilder(this, ApplicationDatabase::class.java, "book-db")
                .allowMainThreadQueries()
                .build()


        // TODO: Init BooksInteractor
        booksInteractor = BooksInteractor(db.bookDao())
    }

    fun getBooksInteractor(): BooksInteractor {
        return booksInteractor
    }

    fun hasInternetConnection(): Boolean {
        // TODO: Add Internet Check logic.

        var internet: Boolean = false

        val connectivityManager: ConnectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

        internet = activeNetwork?.isConnectedOrConnecting == true

        return internet
    }
}