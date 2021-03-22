package appdev.max.weatherapp.arch

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<ViewBindingType : ViewDataBinding> : AppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutRes: Int
    protected lateinit var binding: ViewBindingType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
    }

}