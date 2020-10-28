package edu.uoc.pac2.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
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

    lateinit var mAdView : AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        // Init UI
        initToolbar()
        initRecyclerView()

        //Init the Google Ad
        MobileAds.initialize(this) {}

        //Load the ad
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


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

        //If no internet then load from DB
        if( !(applicationContext as MyApplication).hasInternetConnection())
        {
            loadBooksFromLocalDb()
        }
        else
        {
            //If internet get it from Firebase
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

                            //Pass the new data to the adapter
                            adapter.setBooks(books)

                            //Save the new data to the db
                            saveBooksToLocalDatabase(books)

                        } else {
                            Log.d("TAG", "Current data: null")
                        }
                    }
        }

    }

    // TODO: Load Books from Room
    private fun loadBooksFromLocalDb() {
        val interactor = (applicationContext as MyApplication).getBooksInteractor()
        val books = interactor.getAllBooks()

        adapter.setBooks(books)    }

    // TODO: Save Books to Local Storage
    private fun saveBooksToLocalDatabase(books: List<Book>) {
        val interactor = (applicationContext as MyApplication).getBooksInteractor()
        interactor.saveBooks(books)
    }
}