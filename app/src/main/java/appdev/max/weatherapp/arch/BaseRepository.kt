package appdev.max.weatherapp.arch

import appdev.max.weatherapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import appdev.max.weatherapp.model.base.Result
import appdev.max.weatherapp.utils.*
import retrofit2.Response
import java.net.SocketTimeoutException

open class BaseRepository(protected val networkConnection: NetworkConnection) {

    protected suspend inline fun <Success : Any, reified Error : Any> processResponse(crossinline request: suspend () -> Response<Success>): Result<Success, Error> {
        return if (networkConnection.connected) {
            return try {
                // Make a request
                val response = withContext(Dispatchers.IO) { request() }
                return if (response.isSuccessful) {
                    Result.Success(data = response.body())
                } else {
                    return when {
                        response.code() == HttpCode.BAD_REQUEST.code -> {
                            Result.Error(
                                error = response.castError(),
                                exception = BadRequestException()
                            )
                        }
                        response.code() == HttpCode.UNAUTHORIZED.code -> {
                            Result.Error(
                                error = response.castError(),
                                exception = UnauthorizedException()
                            )
                        }
                        response.code() == HttpCode.NOT_FOUND.code -> {
                            Result.Error(
                                error = response.castError(),
                                exception = NotFoundException()
                            )
                        }
                        else -> {
                            Result.Error(
                                error = response.castError(),
                                exception = RuntimeException("Response error with code: ${response.code()}")
                            )
                        }
                    }
                }

            } catch (timeout: SocketTimeoutException) {
                timeout.print()
                Result.Error(null, errorMessage = getString(R.string.common_error_timeout), exception = timeout)

            } catch (e: Exception) {
                e.print()
                Result.Error(null, exception = e)
            }
        } else {
            Result.Error(errorMessage = getString(R.string.common_error_internet_connection), exception = NoInternetConnectionException())
        }

    }

}