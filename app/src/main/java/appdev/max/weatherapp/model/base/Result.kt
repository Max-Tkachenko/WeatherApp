package appdev.max.weatherapp.model.base

import appdev.max.weatherapp.R
import appdev.max.weatherapp.utils.getString

sealed class Result<out SuccessType : Any, out ErrorType : Any> {

    data class Success<SuccessType : Any>(var data: SuccessType?) : Result<SuccessType, Nothing>()

    data class Error<out ErrorType : Any>(
        val error: ErrorType? = null,
        var errorMessage: String = getString(R.string.common_error_technical),
        val exception: Exception? = null
    ) : Result<Nothing, ErrorType>()
}