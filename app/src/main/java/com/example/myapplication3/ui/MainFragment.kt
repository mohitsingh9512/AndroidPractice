package com.example.myapplication3.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication3.R
import com.example.myapplication3.adapter.MainAdapter
import com.example.myapplication3.data.MainResult
import com.example.myapplication3.di.component.DaggerAppComponent
import com.example.myapplication3.di.component.ViewModelProviderFactory
import com.example.myapplication3.viewmodel.MainViewModel
import kotlinx.coroutines.launch
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
        getData()
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
        mainViewModel?.employeesList?.observe(viewLifecycleOwner) {
            when(it){
                is MainResult.Success -> {
                    successResult(it)
                }
                is MainResult.Fail -> {
                    failResult()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel?.employeeListFlow?.collect {
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

    private fun getData() {
        //mainViewModel?.getData()
        mainViewModel?.getDataFlow()
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
}