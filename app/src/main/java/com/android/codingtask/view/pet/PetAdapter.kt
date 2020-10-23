package com.android.codingtask.view.pet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.codingtask.databinding.ItemPetBinding
import com.android.codingtask.model.Pet

class PetAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val itemClickCallback: ((Pet) -> Unit)?
) : ListAdapter<Pet, PetViewHolder>(PetDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val binding = ItemPetBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        binding.apply {
            root.setOnClickListener {
                binding.pet?.let { pet ->
                    itemClickCallback?.invoke(pet)
                }
            }
        }
        return PetViewHolder(binding, lifecycleOwner)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class PetViewHolder(
    private val binding: ItemPetBinding,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Pet) {
        binding.pet = item
        binding.lifecycleOwner = lifecycleOwner
        binding.executePendingBindings()
    }
}

object PetDiff : DiffUtil.ItemCallback<Pet>() {
    override fun areItemsTheSame(oldItem: Pet, newItem: Pet): Boolean {
        // Compare the pet title as we have unique name
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Pet, newItem: Pet): Boolean {
        return oldItem == newItem
    }
}
