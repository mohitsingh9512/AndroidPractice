package com.example.myapplication3.ui

import android.os.Bundle
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
import com.example.myapplication3.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class MainFragment : Fragment() {

    private val TAG = "TAGGGG"
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

    private val ceh = CoroutineExceptionHandler { coroutineContext, throwable ->
        log(throwable.message.toString())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(viewModelStore,viewModelProviderFactory)[MainViewModel::class.java]
        setUpViews(view)
        setUpAdapter()
        setUpObserver()
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

    fun coroutineExample() {
        lifecycleScope.launch {
            launch {
                api3()
            }
            coroutineScope {
                launch {

                    try {
                        api1()

                    }catch (e: Exception){
                        log("$e")
                    }

                }

                launch {
                    api2()
                }

            }
        }
    }

    suspend fun api1() {
        delay(500)
        throw Exception("")
        log("1: Done")
    }

    suspend fun api2() {
        delay(1000)
        log("2: Done")
    }

    suspend fun api3() {
        delay(1500)
        log("3: Done")
    }

    private fun multipleDownloads() : List<String>{
        val CRH = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d(TAG, "$throwable Exception occured at $coroutineContext")
        }
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CRH)
        scope.launch(CRH) {
            val parentScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CRH)
            val a = parentScope.async { downloadTask1() }
            val b = parentScope.async { downloadTask2() }
            val c = parentScope.async { downloadTask3() }
            val data = awaitAll(a, b, c)
            data.forEach {
                Log.d(TAG, "data ${it}")
            }
            //delay(3000)
            Log.d(TAG, "parent launch")
        }
        return arrayListOf<String>()
    }

    private suspend fun downloadTask1(): String {
        try {
            delay(500)
            erroroccured()
            return "downloadTask1"
        }catch (_: Exception){

        }
        return ""
    }


    private suspend fun downloadTask2(): String {
        delay(1000)
        Log.d(TAG, "downloadTask2")
        return "downloadTask2"
    }

    private suspend fun downloadTask3(): String {
        delay(2000)
        Log.d(TAG, "downloadTask3")
        return "downloadTask2"

    }

    private fun erroroccured() {
        throw IndexOutOfBoundsException()
    }
}
