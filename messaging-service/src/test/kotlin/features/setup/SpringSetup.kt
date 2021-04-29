//package features.setup
//
//import com.forge.messageservice.MessageServiceApplication
//import io.cucumber.java8.En
//import io.cucumber.spring.CucumberContextConfiguration
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.orm.jpa.vendor.Database
//import org.springframework.test.context.ActiveProfiles
//import javax.sql.DataSource
//
//@Suppress("unused")
//@CucumberContextConfiguration
//@ActiveProfiles("integration-test")
//@SpringBootTest(
//    classes = [MessageServiceApplication::class],
//    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
//)
//
//class SpringSetup(
//    ds: DataSource,
//    @Value("\${spring.jpa.database}") database: String
//) : En {
//
//    init {
//        Before { _ ->
//            val db = Database.valueOf(database.toUpperCase())
//            GlobalHooks.initializeDatabase(db, ds)
//        }
//        After { _ -> }
//    }
//}