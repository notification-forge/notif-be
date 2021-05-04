package features

import com.forge.messageservice.MessageServiceApplication
import features.common.TestContext
import features.setup.GlobalHooks
import io.cucumber.java8.En
import io.cucumber.spring.CucumberContextConfiguration
import mu.KotlinLogging
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

private val LOGGER = KotlinLogging.logger {}

@Suppress("unused")
@CucumberContextConfiguration
@ActiveProfiles("integration-test")
@SpringBootTest(
    classes = [MessageServiceApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class SpringSetup : En {
    init {
        Before { _ ->
            TestContext.init()
        }

        After { _ ->

        }

        LOGGER.info("Application started")
    }
}