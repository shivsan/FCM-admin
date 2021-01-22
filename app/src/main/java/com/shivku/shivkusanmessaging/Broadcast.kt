package com.shivku.shivkusanmessaging

data class Broadcast(
    val broadcastId: Long,
    val broadcastType: String,
    val text: String,
    val title: String,
    val type: String
)
