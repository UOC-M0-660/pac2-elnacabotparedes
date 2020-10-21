package edu.uoc.pac2.data

import java.io.FileDescriptor

/**
 * A book Model representing a piece of content.
 */

data class Book(
        val author: String? = null,
        val descriptor: String? = null,
        val publicationDate:String? = null,
        val title: String? = null,
        val uid: Int? = null,
        val urlImage: String? = null
)