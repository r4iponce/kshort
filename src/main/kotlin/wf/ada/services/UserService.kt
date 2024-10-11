package wf.ada.services

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import wf.ada.entities.ExposedUser
import wf.ada.utils.Database.dbQuery
import wf.ada.utils.hashToArgon2id
import wf.ada.utils.verifyHash
import java.time.LocalDateTime

class UserService {
    object Users : IntIdTable() {
        val name: Column<String> = varchar(name = "name", length = 32).uniqueIndex()
        val email: Column<String> = varchar(name = "email", length = 64).uniqueIndex()
        val password: Column<String> = varchar(name = "password", length = 100)
        val description: Column<String?> = varchar(name = "description", length = 1024).nullable()
        val role: Column<String> = varchar(name = "role", length = 16)
        val creationDate: Column<LocalDateTime> = datetime("creation_date")
    }

    init {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    suspend fun create(user: ExposedUser): Int {
        val userId: Int =
            dbQuery {
                Users
                    .insertAndGetId {
                        it[name] = user.name
                        it[email] = user.email
                        it[password] = user.password.hashToArgon2id()
                        it[description] = user.description
                        it[creationDate] = LocalDateTime.now()
                        it[role] = user.role.toString()
                    }.value
            }

        return userId
    }

    suspend fun readAll(): List<ExposedUser> =
        dbQuery {
            Users.selectAll().map {
                ExposedUser(
                    id = it[Users.id].value,
                    name = it[Users.name],
                    email = it[Users.email],
                    password = it[Users.password],
                    creationDate = it[Users.creationDate],
                    role = enumValueOf(it[Users.role]),
                    description = it[Users.description],
                )
            }
        }

    suspend fun checkAuth(
        email: String,
        password: String,
    ): ExposedUser? {
        val user =
            dbQuery {
                Users
                    .selectAll()
                    .where { Users.email eq email }
                    .map {
                        ExposedUser(
                            id = it[Users.id].value,
                            name = it[Users.name],
                            email = it[Users.email],
                            password = it[Users.password],
                            description = it[Users.description],
                            role = enumValueOf(it[Users.role]),
                            creationDate = it[Users.creationDate],
                        )
                    }.singleOrNull()
            }

        if (user != null && password.verifyHash(user.password)) {
            return user
        }

        return null
    }

    suspend fun read(name: String): ExposedUser? {
        val user: ExposedUser? =
            dbQuery {
                Users
                    .selectAll()
                    .where { Users.name eq name }
                    .map {
                        ExposedUser(
                            id = it[Users.id].value,
                            name = it[Users.name],
                            email = it[Users.email],
                            password = it[Users.password],
                            description = it[Users.description],
                            role = enumValueOf(it[Users.role]),
                            creationDate = it[Users.creationDate],
                        )
                    }.singleOrNull()
            }

        return user
    }

    suspend fun delete(name: String) {
        dbQuery {
            Users.deleteWhere { Users.name eq name }
        }
    }
}
