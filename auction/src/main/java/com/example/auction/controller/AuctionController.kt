package com.example.auction.controller

import com.example.auction.exception.InvalidItemException
import com.example.auction.exception.ItemNotFoundException
import com.example.auction.model.Bid
import com.example.auction.model.Item
import org.springframework.web.bind.annotation.*

import javax.annotation.PostConstruct

@RestController
@RequestMapping(value= ["/api"])
class AuctionController {

    /** Source of truth for items in the auction
     *  Requirement says persistent storage is not required
     *  This Items list could easily be migrated to a NoSQL DB
     *  It would handle atomic adds and possible race condition scenarios
     *  that would be expected with auction systems
     */
    private val items: MutableList<Item> = mutableListOf()

    @PostConstruct
    fun initial() {
        items.add(Item(1000, "Deck Chair",  mutableListOf()))
        items.add(Item(1001, "Table", mutableListOf()))
        items.add(Item(1002, "House Plant", mutableListOf()))
    }

    @GetMapping("/item/{id}/bids")
    fun getAllBidsForItem(@PathVariable id: Long) : List<Bid>? = getItem(id).bids

    @GetMapping("/item/{id}/winning")
    fun getWinningBidForItem(@PathVariable id: Long): Double = getItem(id).winningBid ?: 0.0

    @GetMapping("/user/{id}/items")
    fun getBidItemsForUser(@PathVariable id: Long): List<Item> =
        items.filter { it.bids.any { it.uid == id } }

    @PutMapping("/item/{id}")
    fun bidOnItem(@PathVariable id: Long, @RequestBody(required = true) bid: Bid) {
        val item: Item = getItem(id)

        if (id != bid.itemId) {
            throw InvalidItemException("Item id in post body doesn't match request")
        }

        if (item.winningBid == null || bid.price >= item.winningBid!!) {
            item.bids.add(bid)
            item.winningBid = bid.price
            println("Bid successful on ${item.itemName}")
        } else {
            println("Bid unsuccessful on ${item.itemName} - you must bid higher!")
        }
    }

    private fun getItem(id: Long): Item = items.find { item: Item -> item.itemId == id }
            ?: throw ItemNotFoundException("Item with id $id does not exist in auction")
}