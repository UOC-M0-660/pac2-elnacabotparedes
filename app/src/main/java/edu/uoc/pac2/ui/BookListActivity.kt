package edu.uoc.pac2.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.data.FirestoreBookData

/**
 * An activity representing a list of Books.
 */
class BookListActivity : AppCompatActivity() {

    private val TAG = "BookListActivity"

    private lateinit var adapter: BooksListAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        // Init UI
        initToolbar()
        initRecyclerView()

        // TODO: Add books data to Firestore [Use once for new projects with empty Firestore Database]
        FirestoreBookData.addBooksDataToFirestoreDatabase()

        // Get Books
        getBooks()

    }

    // Init Top Toolbar
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
    }

    // Init RecyclerView
    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.book_list)
        // Set Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // Init Adapter
        adapter = BooksListAdapter(emptyList(), this)
        recyclerView.adapter = adapter
    }

    // TODO: Get Books and Update UI
    private fun getBooks() {

        //No internet
        if( !(applicationContext as MyApplication).hasInternetConnection())
        {
            val interactor = (applicationContext as MyApplication).getBooksInteractor()
            val books = interactor.getAllBooks()

            adapter.setBooks(books)
            Log.d("TAG", "DB")
        }
        else
        {
            val db = FirebaseFirestore.getInstance()
            db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

            db.collection("books")
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e)
                            return@addSnapshotListener
                        }
                        if (snapshot != null ) {


                            val books: List<Book> = snapshot.mapNotNull { it.toObject(Book::class.java) }

                            adapter.setBooks(books)

                            val interactor = (applicationContext as MyApplication).getBooksInteractor()
                            interactor.saveBooks(books)

                        } else {
                            Log.d("TAG", "Current data: null")
                        }
                    }
        }







    }

    // TODO: Load Books from Room
    private fun loadBooksFromLocalDb() {
        throw NotImplementedError()
    }

    // TODO: Save Books to Local Storage
    private fun saveBooksToLocalDatabase(books: List<Book>) {
        throw NotImplementedError()
    }
}