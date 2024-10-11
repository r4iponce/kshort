package wf.ada

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.IgnoreTrailingSlash
import wf.ada.plugins.configureSecurity
import wf.ada.plugins.configureSerialization
import wf.ada.routes.configureRouting
import wf.ada.utils.Config
import wf.ada.utils.Database
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    embeddedServer(Netty, port = Config.port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    if (!Files.isDirectory(Paths.get(Config.persistentPath))) {
        Files.createDirectory(Paths.get(Config.persistentPath))
        Files.createDirectory(Paths.get(Config.persistentPath + "/pictures"))
        Files.createDirectory(Paths.get(Config.persistentPath + "/pictures/profiles"))
    }

    Database.migrate()
    Database.connect()
    install(CORS) {
        allowHost("localhost:5173")
        allowHeader(HttpHeaders.ContentType)
    }
    install(IgnoreTrailingSlash)
    configureSecurity()
    configureSerialization()
    configureRouting()
}
