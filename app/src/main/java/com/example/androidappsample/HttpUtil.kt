package com.example.androidappsample

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection
import java.net.URL

class HttpUtil() {
    companion object {
        private var host = "10.0.2.2"
    }

    constructor(hostName: String) : this() {
        host = hostName
    }

    private enum class RequestType {
        GET,
        POST,
    }

    fun getRequest(path: String, query: String, isHttps: Boolean = false): String {
        return httpRequest(path, query, isHttps, RequestType.GET)
    }

    fun getRequest(path: String, data: MutableMap<String, String>, isHttps: Boolean = false): String {
        return getRequest(path, data.toQueryString(), isHttps)
    }

    fun postRequest(path: String, query: String, isHttps: Boolean = false): String {
        return httpRequest(path, query, isHttps, RequestType.POST)
    }

    fun postRequest(path: String, data: MutableMap<String, String>, isHttps: Boolean = false): String {
        return postRequest(path, data.toQueryString(), isHttps)
    }

    private fun httpRequest(path: String, query: String, isHttps: Boolean, type: RequestType): String {
        var res = ""
        var urlString = if (isHttps) {
            "https://$host/$path"
        } else {
            "http://$host/$path"
        }
        if (type == RequestType.GET && query.isNotEmpty()) {
            urlString += "?$query"
        }
        val url = URL(urlString)
        runBlocking {
            async(Dispatchers.IO) {
                val conn = url.openConnection() as HttpURLConnection
                try {
                    if (type == RequestType.POST) {
                        conn.doOutput = true
                        conn.outputStream.let {
                            it.write(query.toByteArray())
                            it.flush()
                            it.close()
                        }
                    }
                    when (conn.responseCode) {
                        HttpURLConnection.HTTP_OK -> {
                            res = conn.inputStream.bufferedReader().readText()
                        }
                        else -> {}
                    }
                } catch (e: Exception) {
                    Log.e("HttpUtil", e.toString())
                } finally {
                    conn.disconnect()
                }
            }.await()
        }
        return res
    }

    private fun MutableMap<String, String>.toQueryString(): String {
        var res = ""
        for (data in this) {
            if (res.isEmpty()) {
                res += "&"
            }
            res += "${data.key}=${data.value}"
        }
        return res
    }
}