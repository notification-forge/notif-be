package features.common

import okhttp3.Response

object TestContext {

    private const val RESP = "RESP"
    private const val USER = "USER"

    private val ctx = ThreadLocal<MutableMap<String, Any>>()

    fun init() {
        ctx.set(HashMap())
    }

    fun set(user: TestUser) {
        val m = ctx.get()
        m[USER] = user
    }

    fun set(resp: Response) {
        val m = ctx.get()
        m[RESP] = resp
    }

    fun user(): TestUser {
        val m = ctx.get()
        return m[USER] as TestUser
    }

    fun response(): Response {
        val m = ctx.get()
        return m[RESP] as Response
    }
}