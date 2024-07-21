package wf.ada.utils

import java.io.FileReader
import java.util.Properties

object Config {
    val driver: String
    val password: String
    val user: String
    val dbUrl: String
    val port: Int
    val url: String
    val jwtSecret: String

    init {
        val properties = Properties()
        properties.load(FileReader("kshort.properties"))

        port = properties.getProperty("port").toInt()

        url =
            if (properties.getProperty("url").last() == '/') {
                properties.getProperty("url")
            } else {
                properties.getProperty("url") + "/"
            }

        driver = properties.getProperty("db_driver")
        password = properties.getProperty("db_password")
        user = properties.getProperty("db_user")
        dbUrl = properties.getProperty("db_url")

        jwtSecret = properties.getProperty("jwt_secret")
    }
}
