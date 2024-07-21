package wf.ada

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.IgnoreTrailingSlash
import wf.ada.plugins.configureSecurity
import wf.ada.plugins.configureSerialization
import wf.ada.routes.configureRouting
import wf.ada.utils.Config
import wf.ada.utils.Database

fun main() {
    embeddedServer(Netty, port = Config.port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    Database.migrate()
    Database.connect()
    install(IgnoreTrailingSlash)
    configureSecurity()
    configureSerialization()
    configureRouting()
}
