package io.victoralbertos.kdirtyandroid.presentation.home.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.victor.kdirtyandroid.R
import io.victoralbertos.kdirtyandroid.entities.User
import kotlinx.android.synthetic.main.user_item.view.ivAvatar

class UsersAdapter(private val contex: Fragment, private val onUserClick: (User) -> Unit) :
    PagingDataAdapter<User, UsersAdapter.UserViewHolder>(UserComparator) {

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)!!
        holder.itemView.apply {
            setOnClickListener { onUserClick(user) }
            Glide.with(contex)
                .load(user.avatar)
                .centerCrop()
                .into(ivAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
    )

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view)

    object UserComparator : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
    }
}
