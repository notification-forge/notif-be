package features.common

import features.stepdefs.CommonStepDefs
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

val client = OkHttpClient()

fun post(user: TestUser, url: String, requestBody: RequestBody, extraHeaders: Map<String, String> = emptyMap()): Response {
    val request = Request.Builder().post(requestBody)
        .addHeader("Authorization", "Bearer ${user.token}")
        .addHeader("Content-Type", "application/json")
        .url(url)
        .build()
    return client.newCall(requestWithExtraHeaders(request, extraHeaders)).execute()
}

private fun requestWithExtraHeaders(request: Request, extraHeaders: Map<String, String> = emptyMap()): Request {
    if (extraHeaders.isNotEmpty()){
        var newBuilder = request.newBuilder()

        for (m in extraHeaders){
            newBuilder = newBuilder.addHeader(m.key, m.value)
        }

        return newBuilder.build()
    }
    return request
}