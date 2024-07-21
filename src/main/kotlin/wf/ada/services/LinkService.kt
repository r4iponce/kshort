package wf.ada.services

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import wf.ada.entities.ExposedLink
import wf.ada.services.LinkService.Links.ownerId
import wf.ada.services.LinkService.Links.short
import wf.ada.services.LinkService.Links.url
import wf.ada.services.UserService.Users
import wf.ada.utils.Database.dbQuery
import java.time.LocalDateTime

class LinkService {
    object Links : Table() {
        val short: Column<String> = varchar("short", length = 32).uniqueIndex()
        val url: Column<String> = varchar("url", length = 2048).uniqueIndex()
        val ownerId: Column<EntityID<Int>?> = reference("owner_id", Users).nullable()
        val creationDate: Column<LocalDateTime> = datetime("creation_date")
        val expirationDate: Column<LocalDateTime?> = datetime("expiration_date").nullable()
        override val primaryKey = PrimaryKey(short, name = "links_pkey")
    }

    init {
        transaction {
            SchemaUtils.create(Links)
        }
    }

    suspend fun create(link: ExposedLink) {
        dbQuery {
            Links.insert {
                it[short] = link.short
                it[url] = link.url
                it[ownerId] = link.ownerId
                it[creationDate] = LocalDateTime.now()
            }
        }
    }

    suspend fun readAll(): List<ExposedLink> =
        dbQuery {
            Links.selectAll().map {
                ExposedLink(
                    it[short],
                    it[url],
                    it[ownerId]?.value,
                    it[Links.creationDate],
                    it[Links.expirationDate],
                )
            }
        }

    suspend fun read(short: String): ExposedLink? {
        val link: ExposedLink? =
            dbQuery {
                Links
                    .selectAll()
                    .where { Links.short eq short }
                    .map {
                        ExposedLink(
                            it[Links.short],
                            it[url],
                            it[ownerId]?.value,
                            it[Links.creationDate],
                            it[Links.expirationDate],
                        )
                    }.singleOrNull()
            }

        if (link != null) {
            return link
        }

        return null
    }

    suspend fun delete(short: String) {
        dbQuery { Links.deleteWhere { Links.short eq short } }
    }
}
