package appdev.max.weatherapp.screen.cities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import appdev.max.weatherapp.R
import appdev.max.weatherapp.arch.BaseFragment
import appdev.max.weatherapp.databinding.FragmentCitiesBinding
import appdev.max.weatherapp.model.CityModel
import appdev.max.weatherapp.screen.MainActivity
import appdev.max.weatherapp.screen.MainViewModel
import appdev.max.weatherapp.utils.onClick
import kotlinx.android.synthetic.main.fragment_cities.*
import kotlinx.android.synthetic.main.item_city.view.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CitiesFragment : BaseFragment<MainActivity, FragmentCitiesBinding>() {
    override val layoutRes: Int get() = R.layout.fragment_cities

    // Main ViewModel
    private val _mainViewModel: MainViewModel by sharedViewModel()

    // Cities
    private lateinit var _cities: List<CityModel>

    // Adapter
    private lateinit var _adapter: CitiesAdapter

    // Filter cities job
    private lateinit var _filterCitiesJob: Job

    override fun onViewCreated() {
        // Observe cities from MainViewModel
        _mainViewModel.citiesLiveDta.observe(viewLifecycleOwner, { cities ->
            // Save cities to local list sorted by name
            _cities = cities.sortedBy { cityModel -> cityModel.name }

            // Setup cities adapter
            _adapter = CitiesAdapter(_cities)
            binding.citiesRv.adapter = _adapter
        })

        // Search city
        citiesSvQuery.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (::_filterCitiesJob.isInitialized) {
                    _filterCitiesJob.cancel()
                }
                _filterCitiesJob = GlobalScope.launch(Dispatchers.Main) {
                    // Make a little delay (do not update adapter on every text change)
                    delay(300)

                    // Filter by search query
                    newText?.toLowerCase()?.let { query ->
                        if (query.isBlank()) {
                            _cities
                        } else {
                            _cities.filter { cityModel -> cityModel.name.toLowerCase().let { name -> name.startsWith(query) || name.contains(query) } }
                        }.let { filteredItems ->
                            // Update adapter items
                            _adapter.updateItems(filteredItems)
                        }
                    }
                }
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        })
    }

    inner class CitiesAdapter(private var _items: List<CityModel>) : RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(_items[position])
        }

        override fun getItemCount(): Int = _items.size

        fun updateItems(items: List<CityModel>) {
            _items = items
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(cityModel: CityModel) {
                with(itemView) {
                    itemCityTvName.text = cityModel.name
                    onClick {
                        // Update location and go back to weather detail screen
                        _mainViewModel.run {
                            updateLocation(cityModel.coordinates[0], cityModel.coordinates[1])
                            updateSelectedCity(cityModel.name)
                        }
                        onBackPressed()
                    }
                }
            }
        }
    }
}