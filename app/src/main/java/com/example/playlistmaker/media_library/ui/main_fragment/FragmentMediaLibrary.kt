package com.example.playlistmaker.media_library.ui.main_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaLibraryBinding
import com.example.playlistmaker.media_library.ui.MediaLibraryViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class FragmentMediaLibrary : Fragment() {

    private var _binding: FragmentMediaLibraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mediaViewPager.adapter =
            MediaLibraryViewPagerAdapter(childFragmentManager, lifecycle)
        tabMediator =
            TabLayoutMediator(binding.mediaTabs, binding.mediaViewPager) { tabItem, position ->
                when (position) {
                    0 -> tabItem.text = getString(R.string.tab_item1)
                    1 -> tabItem.text = getString(R.string.tab_item2)
                }
            }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tabMediator.detach()
    }
}