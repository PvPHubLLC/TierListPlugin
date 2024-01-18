package co.pvphub.tierlist.api

import com.github.kittinunf.fuel.core.Response
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

val gson = GsonBuilder().create()

fun Response.strBody(): String {
    return body().asString(headers["Content-Type"].lastOrNull())
}
inline fun <reified T> Response.jsonBody(type: Type, arrType: Type? = null): T {
    if(type.typeName == "java.util.List") {
        return gson.fromJson(strBody(), List::class.java).map {
            gson.fromJson<Any>(gson.toJson(it), arrType)
        } as T
    }
    return gson.fromJson(strBody(), type) as T
}