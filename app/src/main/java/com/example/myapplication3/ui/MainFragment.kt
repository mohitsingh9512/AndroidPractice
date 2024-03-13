package com.example.myapplication3.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication3.MyApplication
import com.example.myapplication3.R
import com.example.myapplication3.adapter.MainAdapter
import com.example.myapplication3.data.MainResult
import com.example.myapplication3.di.component.DaggerAppComponent
import com.example.myapplication3.di.component.ViewModelProviderFactory
import com.example.myapplication3.extensions.log
import com.example.myapplication3.ui.dialog.MyDialogFragment
import com.example.myapplication3.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    private var mainViewModel : MainViewModel? = null
    private var recyclerView : RecyclerView? = null
    private var mainAdapter : MainAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(viewModelStore,viewModelProviderFactory)[MainViewModel::class.java]
        setUpViews(view)
        setUpAdapter()
        setUpObserver()
        lifecycleScope.launch(Dispatchers.Default) {  }
        //startActivity(Intent(activity, DetailActivity::class.java))
    }

    private fun setUpViews(view: View) {
        recyclerView = view.findViewById(R.id.main_rv)
    }

    private fun setUpAdapter() {
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            mainAdapter = MainAdapter()
            adapter = mainAdapter
        }
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel?.employeeListFlow?.collectLatest {
                    when(it) {
                        is MainResult.Loading -> {
                            showLoader()
                        }
                        is MainResult.Success -> {
                            successResult(it)
                        }
                        is MainResult.Fail -> {
                            failResult()
                        }
                    }
                }

                Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoader() {
        Toast.makeText(context, "Data Loading", Toast.LENGTH_SHORT).show()
    }

    private fun successResult(mainResult: MainResult.Success) {
        recyclerView?.visibility = View.VISIBLE
        mainAdapter?.submitList(mainResult.listEmployees)
    }

    private fun failResult(){
        recyclerView?.visibility = View.GONE
    }

    companion object {
        fun getInstance() : MainFragment {
            return MainFragment()
        }
    }

    fun inject() {
        activity?.application?.let { application ->
            DaggerAppComponent.builder()
                .baseAppComponent((application as MyApplication).getBaseComponent())
                .build().inject(this)
        }
    }

    override fun onPause() {
        super.onPause()
        log("Main Fragment, onPause")
    }
}