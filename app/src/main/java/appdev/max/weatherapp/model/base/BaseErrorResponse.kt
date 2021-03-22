package appdev.max.weatherapp.model.base

import java.io.Serializable

data class BaseErrorResponse(
    val meta: Meta? = null
) : Serializable {

    data class Meta(
        val code: Int? = null,
        val status: String? = "",
        val message: String? = ""
    ) : Serializable

}