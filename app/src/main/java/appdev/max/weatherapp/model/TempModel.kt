package appdev.max.weatherapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TempModel(
    val temp: Double,
    @SerializedName("min", alternate = ["temp_min"])
    val min: Double,
    @SerializedName("max", alternate = ["temp_max"])
    val max: Double,
    val pressure: Int,
    val humidity: Int,
) : Serializable