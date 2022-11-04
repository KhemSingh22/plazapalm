package com.example.plazapalm.recycleradapter

abstract class AbstractModel{
    var adapterPosition: Int = -1
    var mainPosition:Int=-1
    var onItemClick: RecyclerAdapter.OnItemClick? = null
}