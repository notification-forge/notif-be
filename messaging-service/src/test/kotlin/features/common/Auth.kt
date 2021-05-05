package features.common

import features.stepdefs.CommonStepDefs
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody

data class TestUser(
    val isAuthenticated: Boolean = false,
    val token: String? = null,
    val username: String? = null
)

private val accessTokenPattern = "\"accessToken\":\\s*\"(?<accessToken>.*?)\"".toRegex()
private const val ACCESS_TOKEN = "accessToken"

fun login(username: String, password: String): TestUser{
    val req = """
        {
            "username": "$username",
            "password": "$password"
        }
    """.trimIndent()

    val request = Request.Builder().post(
        RequestBody.create(MediaType.get("application/json"), req)
    ).url("${CommonStepDefs.localhost}/auth/login").build()

    val response = client.newCall(request).execute()

    if (response.isSuccessful){
        val mr = accessTokenPattern.find(response.body()?.string()!!)
        if (mr != null){
            return TestUser(isAuthenticated = true, token = mr.groups[ACCESS_TOKEN]?.value!!, username = username)
        }
    }

    return TestUser(isAuthenticated = false)
}