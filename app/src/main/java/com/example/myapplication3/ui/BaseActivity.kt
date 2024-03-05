package com.example.myapplication3.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication3.extensions.log

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            supportFragmentManager.registerFragmentLifecycleCallbacks(object :
                FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentPreAttached(
                    fm: FragmentManager,
                    f: Fragment,
                    context: Context
                ) {
                    super.onFragmentPreAttached(fm, f, context)
                    f.log(object {}.javaClass.enclosingMethod?.name ?: "")
                }

                override fun onFragmentAttached(
                    fm: FragmentManager,
                    f: Fragment,
                    context: Context
                ) {
                    super.onFragmentAttached(fm, f, context)
                    f.log(object {}.javaClass.enclosingMethod?.name ?: "")
                }

                override fun onFragmentPreCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    savedInstanceState: Bundle?
                ) {
                    super.onFragmentPreCreated(fm, f, savedInstanceState)
                    f.log(object {}.javaClass.enclosingMethod?.name ?: "")
                }

                override fun onFragmentCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    savedInstanceState: Bundle?
                ) {
                    super.onFragmentCreated(fm, f, savedInstanceState)
                    f.log(object {}.javaClass.enclosingMethod?.name ?: "")
                }

                override fun onFragmentActivityCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    savedInstanceState: Bundle?
                ) {
                    super.onFragmentActivityCreated(fm, f, savedInstanceState)
                    f.log(object {}.javaClass.enclosingMethod?.name ?: "")
                }

                override fun onFragmentViewCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    v: View,
                    savedInstanceState: Bundle?
                ) {
                    super.onFragmentViewCreated(fm, f, v, savedInstanceState)
                    f.log(object {}.javaClass.enclosingMethod?.name ?: "")
                }

                override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                    super.onFragmentStarted(fm, f)
                    f.log(object {}.javaClass.enclosingMethod?.name ?: "")
                }

                override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentResumed(fm, f)
                    f.log(object {}.javaClass.enclosingMethod?.name ?: "")
                }

                override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
                    super.onFragmentPaused(fm, f)
                    f.log(object {}.javaClass.enclosingMethod?.name ?: "")
                }

                override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
                    super.onFragmentStopped(fm, f)
                    f.log(object {}.javaClass.enclosingMethod?.name ?: "")
                }

                override fun onFragmentSaveInstanceState(
                    fm: FragmentManager,
                    f: Fragment,
                    outState: Bundle
                ) {
                    super.onFragmentSaveInstanceState(fm, f, outState)
                    f.log(object {}.javaClass.enclosingMethod?.name ?: "")
                }

                override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentViewDestroyed(fm, f)
                    f.log(object {}.javaClass.enclosingMethod?.name ?: "")
                }

                override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentDestroyed(fm, f)
                    f.log(object {}.javaClass.enclosingMethod?.name ?: "")
                }

                override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                    super.onFragmentDetached(fm, f)
                    f.log(object {}.javaClass.enclosingMethod?.name ?: "")
                }
            }, false)
        }
    }
}