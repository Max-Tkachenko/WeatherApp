package appdev.max.weatherapp.screen.forecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import appdev.max.weatherapp.R
import appdev.max.weatherapp.arch.BaseFragment
import appdev.max.weatherapp.databinding.FragmentWeatherForecastBinding
import appdev.max.weatherapp.model.base.Result
import appdev.max.weatherapp.model.response.DailyWeatherModel
import appdev.max.weatherapp.screen.MainActivity
import appdev.max.weatherapp.screen.MainViewModel
import appdev.max.weatherapp.utils.*
import kotlinx.android.synthetic.main.item_weather_forecast_day.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherForecastFragment : BaseFragment<MainActivity, FragmentWeatherForecastBinding>() {
    override val layoutRes: Int get() = R.layout.fragment_weather_forecast

    // ViewModel
    private val _viewModel: WeatherForecastViewModel by viewModel()

    // Main ViewModel
    private val _mainViewModel: MainViewModel by sharedViewModel()

    override fun onViewCreated() {
        // Binding setup
        binding.viewModel = _viewModel
        binding.mainViewModel = _mainViewModel

        // Back
        binding.weatherForecastIbBack.onClick {
            onBackPressed()
        }

        // Get weather forecast by location
        _mainViewModel.locationLiveDta.observe(viewLifecycleOwner, { (latitude, longitude) ->
            _viewModel.getWeatherForecast(latitude, longitude)
        })

        // Observe weather detail response
        _viewModel.weatherForecastResponseEventLiveData.observe(viewLifecycleOwner, { event ->
            event.consume()?.let { result ->
                when (result) {
                    is Result.Success -> {
                        result.data?.let { dailyForecastResponse ->
                            // Setup forecast adapter
                            binding.weatherForecastRvDays.adapter = ForecastAdapter(dailyForecastResponse.daily)
                        }
                    }
                    is Result.Error -> {
                        if (result.exception is NoInternetConnectionException) {
                            toast(R.string.common_error_internet_connection)
                        } else {
                            toast(result.errorMessage)
                        }
                    }
                }
            }
        })
    }

    inner class ForecastAdapter(private val _items: List<DailyWeatherModel>) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_weather_forecast_day, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(_items[position])
        }

        override fun getItemCount(): Int = _items.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(dailyWeatherModel: DailyWeatherModel) {
                with(itemView) {
                    // Date
                    itemForecastDayTvDate.text = forecastDateFormatted(dailyWeatherModel.dt)
                    // Weather description
                    errorSafety {
                        itemForecastDayTvDescription.text = dailyWeatherModel.weather[0].description
                    }
                    // Temperature
                    itemForecastDayTvTempMinValue.text = String.format("%.1f", dailyWeatherModel.temp.min).plus("°С")
                    itemForecastDayTvTempMaxValue.text = String.format("%.1f", dailyWeatherModel.temp.max).plus("°С")
                    // Pressure, humidity, clouds
                    itemForecastDayTvPressure.text = String.format(getString(R.string.pressure), dailyWeatherModel.pressure)
                    itemForecastDayTvHumidity.text = String.format(getString(R.string.humidity), dailyWeatherModel.humidity).plus("%")
                    itemForecastDayTvClouds.text = String.format(getString(R.string.clouds), dailyWeatherModel.clouds).plus("%")
                }
            }
        }
    }
}