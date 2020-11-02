package io.victoralbertos.kdirtyandroid.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import io.victor.kdirtyandroid.R
import io.victor.kdirtyandroid.databinding.LoadStateItemBinding

class DefaultLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.binding.apply {
            retryButton.isVisible = loadState is LoadState.Error
            progressBar.isVisible = loadState is LoadState.Loading
            retryButton.setOnClickListener { retry() }
            if (loadState is LoadState.Error) {
                Toast.makeText(retryButton.context, loadState.error.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.load_state_item, parent, false)
        )
    }
}

class LoadStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding: LoadStateItemBinding = LoadStateItemBinding.bind(view)
}


