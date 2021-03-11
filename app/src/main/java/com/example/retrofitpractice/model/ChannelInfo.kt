package com.example.retrofitpractice.model

data class ChannelInfo(
    val channel: List<Channel>,
    val is_adult: Int,
    val link: String,
    val time: String,
)

data class Channel(
    val id: String,
    val title: String,
    val name: String,
    val image: String,
    val type: Int,
    val category: Int,
    val sex: String,
    val lang: String,
    val count: Int,
    val total: Int,
    val login: Int,
    val video: Int,
    val app: Int,
    val pay: Int,
    val amount: Int,
    val interval: Int,
    val start_time: Long,
)