package core.service

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class ComeCookies : CookieJar {

    private val cookies = mutableListOf<Cookie>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.cookies.addAll(cookies)
    }


    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookies
    }

}
