package ru.spbstu.edu.fogezz.moonsoon.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import ru.spbstu.edu.fogezz.moonsoon.databinding.FragmentListBinding

class ListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentListBinding.inflate(inflater)
        binding.listView.addView(RadioButton(context))

        val viewModel: ListViewModel by viewModels()

        viewModel.list.observe(viewLifecycleOwner) {
            it ?: return@observe

            Log.d(javaClass.simpleName, it.joinToString())
        }

        return binding.root
    }
}