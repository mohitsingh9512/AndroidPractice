package com.example.myapplication3.di.scope

import androidx.lifecycle.ViewModel
import dagger.MapKey
import java.lang.annotation.ElementType
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value : KClass<out ViewModel>)
