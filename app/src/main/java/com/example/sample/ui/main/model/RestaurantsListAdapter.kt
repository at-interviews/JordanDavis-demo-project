package com.example.sample.ui.main.model

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.text.capitalize
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sample.R
import com.example.sample.ui.main.util.ConverterUtil
import java.util.*
import kotlin.math.roundToInt

class RestaurantsListAdapter(val onItemClicked: (RestaurantListItemUiModel) -> Unit) : RecyclerView.Adapter<RestaurantsListAdapter.ViewHolder>() {
    private var listItems: List<RestaurantListItemUiModel> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateEmployeesList(userItemList: List<RestaurantListItemUiModel>) {
        this.listItems = userItemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]

        holder.nameTextView.text = item.name
        holder.priceLevelTextView.text = buildPriceLevelString(item.priceLevel)
        holder.ratingTextView.text = item.rating.toString()
        holder.businessStatusTextView.text = item.businessStatus.lowercase().capitalize(Locale.getDefault())
        Glide.with(holder.itemView.context).load(item.iconUrl).into(holder.imageView)

        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

    private fun buildPriceLevelString(priceLevel: Int): String {
        return StringBuilder("$").repeat(priceLevel)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return listItems.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name_text)
        val ratingTextView: TextView = itemView.findViewById(R.id.rating_text)
        val priceLevelTextView: TextView = itemView.findViewById(R.id.price_level_text)
        val businessStatusTextView: TextView = itemView.findViewById(R.id.business_status_text)
        val imageView: ImageView = itemView.findViewById(R.id.restaurant_image)
    }
}