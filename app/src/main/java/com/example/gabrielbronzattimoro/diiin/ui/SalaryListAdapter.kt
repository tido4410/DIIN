package com.example.gabrielbronzattimoro.diiin.ui

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup


class SalaryListAdapter : RecyclerView.Adapter<SalaryListAdapter.SalaryListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SalaryListItemViewHolder? {
        return null
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: SalaryListItemViewHolder?, position: Int) {
    }


    class SalaryListItemViewHolder(avwView : ViewGroup) : RecyclerView.ViewHolder(avwView) {

    }
}