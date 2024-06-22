package com.test.application.feature_stations.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.application.core.model.Charger
import com.test.application.feature_stations.R
import com.test.application.feature_stations.databinding.ItemChargerStationBinding

class StationsListAdapter :
    ListAdapter<Charger, StationsListAdapter.StationViewHolder>(StationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChargerStationBinding.inflate(inflater, parent, false)
        return StationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StationViewHolder(
        private val binding: ItemChargerStationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: Charger) {
            binding.stationName.text = station.name
            binding.stationAddress.text = station.address
            val color = if (station.busy) {
                itemView.context.getColor(com.test.application.ui_components.R.color.charge_busy)
            } else {
                itemView.context.getColor(com.test.application.ui_components.R.color.charge_free)
            }
            binding.chargeIcon.setColorFilter(color)
        }
    }

    class StationDiffCallback : DiffUtil.ItemCallback<Charger>() {
        override fun areItemsTheSame(oldItem: Charger, newItem: Charger): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Charger, newItem: Charger): Boolean {
            return oldItem == newItem
        }
    }
}
