package com.example.storyapp.ui.story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.api.StoryItem
import com.example.storyapp.databinding.ItemStoryBinding
import com.example.storyapp.ui.story_detail.StoryDetailActivity

class StoryListAdapter(private val listStory: List<StoryItem?>):
    RecyclerView.Adapter<StoryListAdapter.StoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return StoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        listStory[position]?.let {
            holder.bind(it)
        }
    }

    inner class StoryViewHolder(private val itemBinding: ItemStoryBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(story: StoryItem) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .centerCrop()
                .into(itemBinding.ivItemPhoto)

            itemBinding.tvItemName.text = story.name

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, StoryDetailActivity::class.java)
                intent.putExtra("story", story)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(itemBinding.ivItemPhoto, "photo"),
                        Pair(itemBinding.tvItemName, "name")
                    )

                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
}