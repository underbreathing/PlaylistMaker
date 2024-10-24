package com.example.playlistmaker

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding


class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())

            v.setPadding(0, statusBarInsets.top, 0, 0)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

        val navController = navHostFragment.navController


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragmentCreatePlaylist, R.id.fragmentMediaPlayer, R.id.fragmentPlaylist, R.id.fragmentEditPlaylist -> {
                    binding.bottomNavigationPanel.isVisible = false
                }

                else -> {
                    binding.bottomNavigationPanel.isVisible = true
                }
            }
        }

        binding.bottomNavigationPanel.setupWithNavController(navController)

    }
}