package com.example.jingbin.cloudreader.dataclass

/**
 * @author One
 * @time 2020/1/16 23:09
 * @version 1.0
 */
data class PixabayDataClass(
    val hits: ArrayList<Hit>,
    val total: Int,
    val totalHits: Int
)

data class Hit(
    val comments: Int,
    val downloads: Int,
    val favorites: Int,
    val id: Int,
    val imageHeight: Int,
    val imageSize: Int,
    val imageWidth: Int,
    val largeImageURL: String,
    val likes: Int,
    val pageURL: String,
    val previewHeight: Int,
    val previewURL: String,
    val previewWidth: Int,
    val tags: String,
    val type: String,
    val user: String,
    val userImageURL: String,
    val user_id: Int,
    val views: Int,
    val webformatHeight: Int,
    val webformatURL: String,
    val webformatWidth: Int
)