package appdev.max.weatherapp.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry

abstract class BaseFragment<ActivityType : BaseActivity<*>, ViewBindingType : ViewDataBinding> : Fragment() {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    protected lateinit var binding: ViewBindingType

    // Lifecycle
    val viewLifecycle: LifecycleRegistry by lazy { LifecycleRegistry(this) }

    abstract fun onViewCreated()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Lifecycle state
        viewLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Call abstract onViewCreated
        onViewCreated()

        // Lifecycle state
        viewLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onStop() {
        super.onStop()
        // Lifecycle state
        viewLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Lifecycle state
        viewLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    fun onBackPressed() {
        activity?.onBackPressed()
    }

}