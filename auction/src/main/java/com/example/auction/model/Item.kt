package com.example.auction.model

data class Item(
        val itemId: Long,
        val itemName: String,
        val bids: MutableList<Bid>,
        var winningBid: Double? = null
)