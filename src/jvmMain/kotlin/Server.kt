import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.litote.kmongo.json


val shoppingList = mutableListOf(
    ShoppingListItem("Cucumbers 🥒", 1),
    ShoppingListItem("Tomatoes 🍅", 2),
    ShoppingListItem("Orange Juice 🍊", 3)
)

val usersList = mutableListOf(
    User("Juan Camilo", "Zorrilla", "juanmiloz", "juan","juan", "13/03/2002"),
    User("Juan Pablo", "Sanin", "jpSanin", "jpxsanin","jpxsanin", "24/05/2000")
)


fun main() {
    embeddedServer(Netty, 9090) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }
        routing {

            get("/") {
                call.respondText(
                    this::class.java.classLoader.getResource("index.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            get("/users") {
                call.respondText(
                    this::class.java.classLoader.getResource("listUsers.html")!!.readText(),
                    ContentType.Text.Html
                )
            }


            post("/"){

                val parameters = call.receiveParameters();
                val username: String = parameters["Username"].toString();
                val password: String = parameters["Password"].toString();
                var existAccount:Boolean = false
                println("Username: "+username);
                println("Password: "+password);

                if(username!=null && password !=null){
                    for(item in usersList){
                        if(item.username == username && item.password == password) {
                            existAccount = true
                            call.respondText(
                                this::class.java.classLoader.getResource("listUsers.html")!!.readText(),
                                ContentType.Text.Html
                            )

                        }
                    }
                    if(!existAccount){
                        println("El usuario o la contraseña son incorrectos")
                        call.respondText(
                            this::class.java.classLoader.getResource("index.html")!!.readText(),
                            ContentType.Text.Html
                        )
                    }
                }else{
                   println("No pueden existir campos vacios")
                  call.respondText(
                        this::class.java.classLoader.getResource("index.html")!!.readText(),
                        ContentType.Text.Html
                    )
                }




            }

            get("/create") {
                call.respondText(
                    this::class.java.classLoader.getResource("create.html")!!.readText(),
                    ContentType.Text.Html
                )
            }

            static("/") {
                resources("")
            }

        }
    }.start(wait = true)
}