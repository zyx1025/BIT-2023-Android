package com.hikariz.myapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DistanceViewModel : ViewModel() {
    //文萃楼B
    private val _distanceB = MutableLiveData<Double>()
    val distanceB: LiveData<Double> = _distanceB

    fun updateDistanceB(value: Double) {
        _distanceB.postValue(value)
    }

    //文萃楼F
    private val _distanceF = MutableLiveData<Double>()
    val distanceF: LiveData<Double> = _distanceF

    fun updateDistanceF(value: Double) {
        _distanceF.postValue(value)
    }

    //文萃楼G
    private val _distanceG = MutableLiveData<Double>()
    val distanceG: LiveData<Double> = _distanceG

    fun updateDistanceG(value: Double) {
        _distanceG.postValue(value)
    }

    //文萃楼H
    private val _distanceH = MutableLiveData<Double>()
    val distanceH: LiveData<Double> = _distanceH

    fun updateDistanceH(value: Double) {
        _distanceH.postValue(value)
    }

    //文萃楼I
    private val _distanceI = MutableLiveData<Double>()
    val distanceI: LiveData<Double> = _distanceI

    fun updateDistanceI(value: Double) {
        _distanceI.postValue(value)
    }

    //文萃楼M
    private val _distanceM = MutableLiveData<Double>()
    val distanceM: LiveData<Double> = _distanceM

    fun updateDistanceM(value: Double) {
        _distanceM.postValue(value)
    }
}

