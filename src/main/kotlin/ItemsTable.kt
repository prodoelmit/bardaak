package org.prodoelmit

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object ItemsTable : IntIdTable("items") {
    val name = varchar("name", 255).nullable()
    val filename = varchar("filename", 255).nullable()
    val location = integer("location").nullable()
}

// Entity class
class ItemEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ItemEntity>(ItemsTable)

    var name by ItemsTable.name
    var filename by ItemsTable.filename
    var location by ItemsTable.location
}

