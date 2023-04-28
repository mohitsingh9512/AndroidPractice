package com.example.myapplication3.data


class ContainerDataModel(
    val list : ArrayList<BaseDataModel>
) : BaseDataModel {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

}