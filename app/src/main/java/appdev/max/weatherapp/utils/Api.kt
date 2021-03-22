package appdev.max.weatherapp.utils

import com.google.gson.Gson
import retrofit2.Response

enum class HttpCode(val code: Int) {
    SUCCESS(200),
    REDIRECT(302),

    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    CONFLICT(409),
    FORCE_UPDATE(410),
    UNPROCESSABLE_ENTITY(422)
}

inline fun <T : Any, reified ErrorType : Any> Response<T>.castError(): ErrorType? {
    val gson = Gson()
    return try {
        gson.fromJson(errorBody()?.string(), ErrorType::class.java)
    } catch (e: Exception) {
        e.print()
        null
    }
}

class BadRequestException : RuntimeException("Bad request exception")
class UnauthorizedException : RuntimeException("User is unauthorized")
class NotFoundException : RuntimeException("Not found exception")