package br.com.gdarlan.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "books")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(name = "author", nullable = false, length = 250)
    var author: String = "",
    @Column(name = "launch_date", nullable = false)
    var launchDate: Date? = null,
    @Column(name = "price", nullable = false)
    var price: Double = 0.0,
    @Column(name = "title", nullable = false)
    var title: String = "",

    )