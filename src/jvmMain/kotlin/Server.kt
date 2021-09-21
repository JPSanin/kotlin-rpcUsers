
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
                var message:String

                println("Username: "+username);
                println("Password: "+password);

                if(!username.equals("") && !password.equals("")){
                    for(item in usersList){
                        if(item.username == username && item.password == password) {
                            existAccount = true
                            call.respond(usersList)
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

            post("/create"){
                val parameters = call.receiveParameters()

                var username:String = parameters["username"].toString()
                var password:String = parameters["password"].toString()
                var confirmPassword:String = parameters["confirmPassword"].toString()
                var firstName:String = parameters["firstName"].toString()
                var lastName:String = parameters["lastName"].toString()
                var birthdate:String = parameters["birthdate"].toString()
                var exist:Boolean = false

                if(!username.equals("") && !password.equals("") && !confirmPassword.equals("") && !firstName.equals("") && !lastName.equals("") && !birthdate.equals("")){
                    if(password.equals(confirmPassword)){
                        for(item in usersList){
                            if(item.username.equals(username)){
                                exist = true;
                            }
                        }
                        if(!exist){
                            var newUser:User = User(firstName, lastName, username, password, confirmPassword, birthdate)
                            usersList.add(newUser)
                            call.respondText(
                                this::class.java.classLoader.getResource("index.html")!!.readText(),
                                ContentType.Text.Html
                            )
                        }else{
                            print("El usuario ingresado ya existe")
                            call.respondText(
                                this::class.java.classLoader.getResource("create.html")!!.readText(),
                                ContentType.Text.Html
                            )
                        }
                    }else{
                        println("Las contraseñas no son iguales")
                        call.respondText(
                            this::class.java.classLoader.getResource("create.html")!!.readText(),
                            ContentType.Text.Html
                        )
                    }
                }else{
                    println("No pueden haber campos vacios")
                    call.respondText(
                        this::class.java.classLoader.getResource("create.html")!!.readText(),
                        ContentType.Text.Html
                    )
                }
            }

            static("/") {
                resources("")
            }

        }
    }.start(wait = true)
}