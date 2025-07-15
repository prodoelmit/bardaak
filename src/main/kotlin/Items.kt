package org.prodoelmit
import com.algolia.api.SearchClient
import com.algolia.model.search.Hit
import com.algolia.model.search.SearchForHits
import com.algolia.model.search.SearchMethodParams
import com.algolia.model.search.SearchParams
import com.algolia.model.search.SearchResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.jetbrains.exposed.v1.core.Index
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.prodoelmit.Environment
import org.prodoelmit.Item
import org.prodoelmit.ItemEntity
import org.prodoelmit.ItemsTable

object Items {
    // Algolia configuration
    private val algoliaClient = SearchClient(
        Environment.ALGOLIA_APP_ID,
        Environment.ALGOLIA_WRITE_KEY,
    )

    private val INDEX_NAME= Environment.INDEX_NAME


    // Sync item to Algolia
    private fun syncToAlgolia(item: Item) {
        runBlocking {
            try {
                val algoliaObject = mapOf(
                    "id" to item.id,
                    "name" to item.name
                )
                algoliaClient.addOrUpdateObject(INDEX_NAME, item.id.toString(), algoliaObject)
            } catch (e: Exception) {
                println("Failed to sync item ${item.id} to Algolia: ${e.message}")
            }
        }
    }

    // Search items by name using Algolia
    fun searchByName(query: String, limit: Int = 20): List<Item> {
        return runBlocking {
            try {
                val responses = algoliaClient.search(
                    SearchMethodParams().setRequests(
                        listOf(SearchForHits().setIndexName(INDEX_NAME).setQuery(query).setHitsPerPage(limit))
                    ),
                    Hit::class.java
                )

                val results = responses.results as List<SearchResponse<Hit>>
                val ids = results.flatMap { it.hits.map { hit -> hit.additionalProperties["id"] as Int} }
                Items.getItems(ids)
            } catch (e: Exception) {
                println("Search failed: ${e.message}")
                emptyList()
            }
        }
    }

    fun getItem(id: Int): Item? {
        return transaction {
            ItemEntity.findById(id)?.let { entity ->
                Item(
                    id = entity.id.value,
                    name = entity.name,
                    location = entity.location,
                    filename = entity.filename
                )
            }
        }
    }

    fun entityToItem(entity: ItemEntity): Item {
        return Item(
            id = entity.id.value,
            name = entity.name,
            location = entity.location,
            filename = entity.filename,
        )
    }

    fun getItems(ids: List<Int>): List<Item> {
        return transaction {
            ItemEntity.find { ItemsTable.id inList ids }.map(::entityToItem)
        }
    }

    fun createItem(
        name: String?,
        filename: String?,
        location: Int?
    ): Item {
        return transaction {
            val newEntity = ItemEntity.new {
                this.name = name
                this.filename = filename
                this.location = location
            }

            val item = Item(
                id = newEntity.id.value,
                name = newEntity.name,
                location = newEntity.location,
                filename = newEntity.filename
            )

            // Sync to Algolia
            syncToAlgolia(item)

            item
        }
    }

    fun updateItem(item: Item) {
        updateItem(item.id, item.name, item.filename, item.location)
    }

    fun updateItem(id: Int, name: String?, filename: String?, location: Int?): Item? {
        return transaction {
            ItemEntity.findById(id)?.let { entity ->
                entity.name = name
                entity.filename = filename
                entity.location = location

                val updatedItem = Item(
                    id = entity.id.value,
                    name = entity.name,
                    location = entity.location,
                    filename = entity.filename
                )

                // Sync to Algolia
                syncToAlgolia(updatedItem)

                updatedItem
            }
        }
    }

    fun deleteItem(id: Int): Boolean {
        return transaction {
            ItemEntity.findById(id)?.let { entity ->
                entity.delete()

                // Remove from Algolia
                runBlocking {
                    try {
                        algoliaClient.deleteObject(INDEX_NAME, id.toString())
                    } catch (e: Exception) {
                        println("Failed to remove item $id from Algolia: ${e.message}")
                    }
                }

                true
            } ?: false
        }
    }

    fun getItemsInLocation(locationId: Int): List<Item> {
        return transaction {
            addLogger(StdOutSqlLogger)
            ItemEntity.find { ItemsTable.location eq locationId }.map { entity ->
                Item(
                    id = entity.id.value,
                    name = entity.name,
                    location = entity.location,
                    filename = entity.filename
                )
            }
        }
    }
}
