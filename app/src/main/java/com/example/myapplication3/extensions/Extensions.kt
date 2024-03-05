package com.example.myapplication3.extensions

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment

fun Activity.log(msg: String) {
    Log.d("My LifeCycle", "Activity $this : $msg" )
}

fun Fragment.log(msg: String) {
    Log.d("My LifeCycle", "Fragment $this : $msg")

}


//val sharedPreferences = context?.getSharedPreferences("", MODE_PRIVATE)
//sharedPreferences?.editSP {
//    putString("","")
//}
fun SharedPreferences.editSP(func : SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}