package com.example.storyapp.ui.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.api.StoryItem
import com.example.storyapp.databinding.ItemStoryBinding

class StoryListAdapter(private val listStory: List<StoryItem?>):
    RecyclerView.Adapter<StoryListAdapter.StoryViewHolder>() {
    private lateinit var clicked: ItemCLicked

    interface ItemCLicked {
        fun click(position: Int)
    }

    fun setClicked(clicked: ItemCLicked){
        this.clicked = clicked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return StoryViewHolder(binding, clicked)
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        listStory[position]?.let {
            holder.bind(it)
        }
    }

    inner class StoryViewHolder(private val itemBinding: ItemStoryBinding, itemClick: ItemCLicked): RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.root.setOnClickListener {
                itemClick.click(absoluteAdapterPosition)
            }
        }

        fun bind(story: StoryItem) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .centerCrop()
                .into(itemBinding.ivItemPhoto)

            itemBinding.tvItemName.text = story.name
        }
    }
}