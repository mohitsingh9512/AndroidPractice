package com.example.myapplication3.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import retrofit2.Call
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/*
Coroutines in Kotlin are a language feature that allow you to write asynchronous code
in a sequential manner. They provide a way to perform asynchronous operations without
blocking the thread, making it easier to write concurrent and asynchronous code.
Provides Structured Concurrency
Cancellation and error handling
 */
class MyCoroutine {

    /*Coroutine Scope
        Entry Point to coroutines
        Create coroutines, suspend functions
        Keep track of coroutines
        Ability to cancel

     */

    private val scopeParent = CoroutineScope(Job())

    /*Job
        Handle to the coroutine
        Models the Lifecycle of cr
        Coroutine Hierarchy
        Root of cr is Job installed in the cr scope
        Job Lifecycle
        States : New, Active, Completing, Completed, Cancelling, Cancelled
        We don't have access to these states but have access to properties
        Properties: isActive, isCancelled, isCompleted
     */

    /*
    Cr by default started as Eagerly isActive = true
    But can be started as lazyily also
    In this case, the resulting Deferred is created in NEW state.
    It can be explicitly started with start function and will be started implicitly
    on the first invocation of join, await or awaitAll.
     */
    fun crLazy() {
        scopeParent.apply {
            getAllInfo { }
        }
        val job = scopeParent.launch(start = CoroutineStart.LAZY) {
            getAllInfo { }
            delay(2000)
        }
        job.start() // Now moving to Active State
        val deferred = scopeParent.async(start = CoroutineStart.LAZY) {  }
        //deferred.await() // Active State
    }

    /*
        start   complete      finish
                  -> Cancelling ->
    New -> Active -> Completing ->  Complete
    isActive true/Active when completing state
    Completing state until children complete/finish
    Completed only when all children have completed as well
     */

    /*
    if something happens in Active/Completing state ,
        cancel/fail -> Cancelling State -> Cancelled
     */

    /*
    To define the behaviour of Cr , use coroutine context
    that contains set of elements
                                    default
    Coroutine Dispatcher      -> Dispatchers.Default
    Job                       -> No Parent Job
    CoroutineExceptionHandler -> None
    CoroutineName             -> "coroutine"
     */

    /*
    New Coroutine inherits from Parent context
    Parent context => Defaults + inherited context + arguments
    New Context => Parent context + Job
    Context can change wrt the scope/cr
     */
    suspend fun jobs(){
        val scope = CoroutineScope(scopeParent.coroutineContext) // Parent Job
        val job1 = scope.launch {
            getAllInfo { }
        }
        val job2 = scope.launch {
            getAllInfo { }
        }
        // Dont want to manage all the jobs
        // Want to cancel all?
        scope.cancel()
        // When we cancel a scope all children get cancelled
        // But we have 2 scope , viewModelScope, lifecycleScope so that you don't
        // need to call cancel yourself

        // A cancel children job does not affect siblings

        // You cannot start a cr from a cancelled scope.

    }

    /*
    Print message twice  a second
     */
     fun cooperative(scope: CoroutineScope){
        scope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("Coroutine","CEH 1 $throwable")
        }) {
            getAllInfo { }
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default + CoroutineExceptionHandler { coroutineContext, throwable ->
                Log.d("Coroutine","CEH 2 $throwable")
            }) {
                getAllInfo { }
                var nextPrintTime = startTime
                var i = 0
                while(i<5 && isActive){ // isActive Makes it cooperative
                    // or use yield() checks for cancellation and throws Exception
                    // ensureActive() as well
                    try {
                        if (System.currentTimeMillis() >= nextPrintTime) {
                            Log.d("Coroutine", "Hello ${i++}")
                            nextPrintTime += 500L
                        }
                    }catch (e: Exception){
                        Log.d("Coroutine","catch $e")
                    } finally {
                        Log.d("Coroutine","coroutine finally")
                    }
                }
            }
            delay(1000)
            Log.d("Coroutine","Cancel")
            try {
                job.cancel()
            }catch (e: Exception){
                Log.d("Coroutine","catch $e")
            }

            Log.d("Coroutine","Done")
            /* No Cooperative : Hello 1 Hello 2 Cancel Done Hello 3 Hello 4
            Cooperative : Hello 1 Hello 2 Cancel Done
             */
        }
    }

    fun cooperativeRepeat(scope: CoroutineScope){
        scope.launch {
            val job = launch(Dispatchers.Default) {
                repeat(5){
                    Log.d("Coroutine","Hello $it")
                    // Don't need cooperation ??
                    // Since delay creates a suspend cancellable coroutine
                    // Already have mechanism has check for cancellation
                    // All cr extensions are cancellable
                    // You can make urs as well
                    delay(500)
                }
            }
            delay(1000)
            Log.d("Coroutine","Cancel")
            job.cancel()
            Log.d("Coroutine","Done")
        }
    }

    /*
    Do 2 tasks and ensure work2 is done only if the cr is not cancelled
     */

    fun ensure(lifeCycleScope: CoroutineScope) {
        val job = lifeCycleScope.launch {
            workOne()
            ensureActive() // yield()
            //workTwo()
            // work 1 executes then ensure works and then work 2
        }
    }

    private suspend fun workOne()  = withContext(Dispatchers.IO) {
        //otherWork()
        //yield()
        //someOtherWork()
    }

    /*
        // join() Blocks the running makes it's above code finish,
        // await() returns the result, will throw exception if called after completed
        job.cancel()
        val result = job.await()  -> JobCancellationException
     */



    suspend fun <T:Any> Call<T>.await() {
        return suspendCancellableCoroutine { cancellableContinuation ->
            cancellableContinuation.invokeOnCancellation {
                this.cancel()
            }
            // isActive() -> no Job to use this
            // yield() -> No Cr Context to use this
        }
    }

    /*
    With a failing child, uncaught exceptions are propagated up!
    coroutineScope, supervisorScope are cr scope builders,
    they don't propagate the exception nor cancel siblings
     */

    //TODO READ MORE , EXAMPLE

    /*
    // Supervisor Job : If one child cancels,
     it will notify it parent job and job will
     not cancel other sibling or itself or throw
     exception upwards
     SupervisorJob only works if its is the cr direct parent

     launch(job -> Governing job)
    */

    fun cancelJobAndChildren() {
        val job = scopeParent.launch() {
           val job1 = launch {
               val job2 = launch() {
                   delay(2000)
                   log("job2")
               }
               val job3 = launch {
                   delay(4000)
                   log("job3")
               }
               delay(3000)
               log("completed")
           }
            delay(2500)
            job1.cancel()
            log("job1 started")
        }
    }

    fun failJobAndChildren() {
        val job = scopeParent.launch() {
            //val job = scopeParent.launch(SupervisorScope()) { This will not work to save the sibling
            supervisorScope { // This will work.
                // Direct parent needed of supervisor
                // But exception will be propagated and thrown.
                // Crash will happen
                // So need CEH in scopeParent.launch(CEH)
                val job2 = launch() {
                    throw Exception("hwllo")
                    delay(2000)
                    log("job2")
                }
                val job3 = launch {
                    delay(2000)
                    log("job3")
                }
            }
            delay(3000)
            log("completed")
        }
        log("job1 started")
    }

    fun failJobAndChildren2() {
        val scope = CoroutineScope(Job() + CoroutineExceptionHandler{ coroutineContext, throwable ->
            log(throwable.message ?: "")
        })
        val sharedJob = SupervisorJob()
        scope.launch(sharedJob) {
            throw Exception("hwllo")
            delay(2000)
            log("job2")
        }
        scope.launch(sharedJob) {
            delay(2000)
            log("job3")
        }
    }

    // 2 Categories
    // Thrown - > launch
    // Exposed - > async ..  not called until await is called

    fun asyncLaunchException() {
        scopeParent.launch {
            async {
                // If async throws, launch throws
                // without calling .await()
                throw java.lang.Exception("Await throws launch throws")
            }
        }

        // so

        scopeParent.launch {
            try{
                // code that can throw exception
                throw java.lang.Exception("Crash from Launch")
            }catch (e: java.lang.Exception){
                // handle exception
                log("No exception thrown from launch")
            }
        }

        scopeParent.launch {
            val result = runCatching {
                throw java.lang.Exception("Crash from async : No supervisor")
            }
            if(result.isSuccess){
                // Happy Path
            }else {
                // Sad Path
            }
        }
    }

    fun asyncExampleWithSuperVisorScope() {
        scopeParent.launch {
            supervisorScope {
                val deferred = async {
                    throw java.lang.Exception("Crash from async")
                }
                try {
                    deferred.await()
                }catch (e:Exception){
                    log("No exception thrown from async")
                }
            }
        }

    }

    fun asyncExampleWithoutSupervisor() {
        scopeParent.launch {
            val deferred = async {
                throw java.lang.Exception("Crash from async : No supervisor")
            }
            try {
                deferred.await()
            }catch (e:Exception){
                log("No exception thrown from async : No supervisor")
            }
        }
    }

    fun asyncExampleWithoutSupervisorButCEH() {
        scopeParent.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            log("Saved by CEH")
        }) {
            val deferred = async {
                throw java.lang.Exception("Crash from async : No supervisor")
            }
            try {
                deferred.await()
            }catch (e:Exception){
                log("No exception thrown from async : No supervisor")
            }
        }
    }


    /*
    CEH : Humble uncaught exception for that context
    When called ? -> Automatically propagated exception i.e Launch
    but not async
    Where installed ? -> Scope || Root Cr || supervisorScope Direct Child
     */


    fun cehExample() {
        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->  
            log(throwable.message ?: "")
        }
        val scope = CoroutineScope(Job() + handler)
        scope.launch {
            launch {
                throw java.lang.Exception("Failed Cr") // sent to handler safe
            }
        }

        // Passed Inside inner launch
        val scope2 = CoroutineScope(Job())
        scope2.launch(handler) {
            launch {
                throw java.lang.Exception("Failed Cr") // sent to handler safe
            }
        }

        // Passed Inside inner inner launch -> will crash
        val scope3 = CoroutineScope(Job())
        scope3.launch() {
            launch(handler) {
                //throw java.lang.Exception("Failed Cr")
                // Will CRASH .
            }
        }

        // Passed Inside inner inner launch but supervisor-> safe
        val scope4 = CoroutineScope(Job())
        scope4.launch() {
           supervisorScope {
               launch(handler) {
                   throw java.lang.Exception("Failed Cr")
                   // No Crash .
               }
           }
        }
    }

    fun cehExampleAsync() {
        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            log(throwable.message ?: "")
        }


        val scope0 = CoroutineScope(Job())


        /*
        scope0.async {
            throw java.lang.Exception("Failed Cr without await")
            // No await called. The exception will be thrown but not processed
            // But scope will be cancelled
            // will not crash
        }
        log("$scope0")


        scope0.launch {
            async {
                throw java.lang.Exception("Failed Cr without await inside root cr") // sent to handler safe
            }
            // No await called. the exception will be thrown and wont be processed
            // but since it will also propagate it will go to launch and launch will error
            // will crash
        }
        log("$scope0")



        scope0.launch(handler) {
            async() {
                throw java.lang.Exception("Failed Cr without await inside root cr") // sent to handler safe
            }
            // No await called. the exception will be thrown and wont be processed
            // but since it will also propagate it will go to launch and launch will error
            // handler will be called
        }
        log("$scope0")





        scope0.launch() {
           supervisorScope {
               val d = async() {
                   throw java.lang.Exception("Failed Cr without await inside root cr")
               }
               // With await example below . till now we were not calling await
           }
            // another way to save using supervisorScope
            // No await called. the exception will be thrown and wont be processed
            // but since it will also propagate it will go to launch and launch will error
            // scope0 not cancelled
        }
        log("$scope0")

        scope0.launch(handler) {
            val d = async() { // placing handler here wont work
                throw java.lang.Exception("Failed Cr without await inside root cr") // sent to handler safe
            }
            d.await()
            // await called. the exception will be thrown and will be processed
            // but since it will also propagate it will go to launch and launch will error
            // handler will be called
            // scope will be cancelled
        }
        log("$scope0")

        scope0.launch() {
            supervisorScope {
                val d = async() { // placing handler here wont work
                    throw java.lang.Exception("Failed Cr without await inside root cr") // sent to handler safe
                }
                d.await()
            }
            // await called. the exception will be thrown and will be processed
            // supervisor will propagate the exception
            // but since it will also propagate it will go to launch and launch will error
            // Will CRASH
            // scope cancelled
        }
        log("$scope0")


        scope0.launch() {

            val d = async() { // placing handler here wont work
                throw java.lang.Exception("Failed Cr without await inside root cr") // sent to handler safe
            }
            try {
                d.await()
            }catch (e : Exception){
                log("Log from Catch")
            }
            // await called. the exception will be thrown and will be processed
            // but since it will also propagate it will go to launch and launch will error
            // Will CRASH, catch will be called
        }
        log("$scope0")


        scope0.launch() {

            supervisorScope {
                val d = async() { // placing handler here wont work
                    throw java.lang.Exception("Failed Cr without await inside root cr") // sent to handler safe
                }
                try {
                    d.await()
                }catch (e : Exception){
                    log("Log from Catch")
                }
            }
            // await called. the exception will be thrown and will be processed
            // supervisor will propagate the exception
            // but since it will also propagate it will go to launch and launch will error
            // Will NOT CRASH, catch will be called
        }
        log("$scope0")

        */


        /*

        // Passed Inside inner launch
        val scope2 = CoroutineScope(Job())
        scope2.launch(handler) {
            launch {
                throw java.lang.Exception("Failed Cr") // sent to handler safe
            }
        }

        // Passed Inside inner inner launch -> will crash
        val scope3 = CoroutineScope(Job())
        scope3.launch() {
            launch(handler) {
                //throw java.lang.Exception("Failed Cr")
                // Will CRASH .
            }
        }

        // Passed Inside inner inner launch but supervisor-> safe
        val scope4 = CoroutineScope(Job())
        scope4.launch() {
            supervisorScope {
                launch(handler) {
                    throw java.lang.Exception("Failed Cr")
                    // No Crash .
                }
            }
        }

         */
    }

    fun log(msg : String){
        Log.d("Coroutine",msg)
    }
}

fun CoroutineScope.getAllInfo(block : () -> Unit){
    Log.d("Coroutine", "getAllInfo: Cr context : $coroutineContext \n Cr Job : ${coroutineContext.job}")
    block.invoke()
}