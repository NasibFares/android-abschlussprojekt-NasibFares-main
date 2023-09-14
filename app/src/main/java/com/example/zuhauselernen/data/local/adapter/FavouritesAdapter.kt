package com.example.zuhauselernen.data.local.adapter

import android.content.Context
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.zuhauselernen.data.remote.GameViewModel
import com.example.zuhauselernen.data.remote.model.Game
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FavouritesItemListBinding

class FavouritesAdapter(
    private val context: Context,
    private val dataset: List<Game>,
    private val viewModel: GameViewModel
) : RecyclerView.Adapter<FavouritesAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: FavouritesItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            FavouritesItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val game = dataset[position]
        holder.binding.tvGameTitle.text = game.title
        holder.binding.tvGameUrl.text = game.gameUrl
        val imgUri = game.thumbnail.toUri().buildUpon().scheme("https").build()
        /**
        lade das Bild mithilfe der URI in die ImageView und runde die Ecken ab.
         */
        holder.binding.ivGame.load(imgUri) {
            error(R.drawable.ic_broken_image)
            transformations(RoundedCornersTransformation(10f))
        }
        Linkify.addLinks(holder.binding.tvGameUrl, Linkify.WEB_URLS)


    }
}