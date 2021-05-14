package features.stepdefs

import features.common.TestContext.response
import features.common.TestContext.set
import features.common.TestContext.user
import features.common.login
import features.common.post
import io.cucumber.datatable.DataTable
import io.cucumber.java8.En
import io.cucumber.spring.ScenarioScope
import mu.KotlinLogging
import okhttp3.MediaType
import okhttp3.RequestBody.create
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals

@ScenarioScope
class CommonStepDefs : En {

    private val logger = KotlinLogging.logger {}

    companion object {
        const val baseUrl = "http://localhost:8080"
        const val apiBaseUrl = "$baseUrl/api/v1"
        const val graphqlUrl = "$baseUrl/graphql"
    }

    init {
        Given("a user has logged in") { dataTable: DataTable ->
            val input = dataTable.asMap<String, String>(String::class.java, String::class.java)

            val username = input.getValue("username")
            val password = input.getValue("password")

            logger.info("Authenticating user using: $username/$password")
            set(login(username, password))
        }

        When("we call GraphQL with requestBody") { body: String ->
            val request = create(MediaType.get("application/json"), body.toByteArray())
            set(post(user(), "$baseUrl/graphql", request))
        }

        And("we print out the response") {
            println(response().body()?.string())
        }

        Then("we should receive a response code of {int}") { statusCode: Int ->
            assertEquals(statusCode, response().code())
        }
    }

}