package io.victoralbertos.kdirtyandroid.presentation.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import io.victor.kdirtyandroid.R
import io.victor.kdirtyandroid.databinding.UserFragmentBinding

class UserFragment : Fragment(R.layout.user_fragment) {
    private val navArgs by navArgs<UserFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UserFragmentBinding.bind(view).apply {
            toolbar.setupWithNavController(findNavController(), AppBarConfiguration(findNavController().graph))
            toolbar.title = navArgs.user.name

            Glide.with(this@UserFragment)
                .load(navArgs.user.avatar)
                .centerCrop()
                .into(ivAvatar)
        }
    }
}
