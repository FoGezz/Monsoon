package ru.spbstu.edu.fogezz.moonsoon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.spbstu.edu.fogezz.moonsoon.databinding.FragmentListBinding

class ListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentListBinding.inflate(inflater)

        val id = ListFragmentArgs.fromBundle(requireArguments()).id



        return binding.root
    }
}