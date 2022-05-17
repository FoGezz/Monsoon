package ru.spbstu.edu.fogezz.moonsoon.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.spbstu.edu.fogezz.moonsoon.R
import ru.spbstu.edu.fogezz.moonsoon.databinding.FragmentListBinding
import ru.spbstu.edu.fogezz.moonsoon.databinding.ListItemBinding
import ru.spbstu.edu.fogezz.moonsoon.network.User

class ListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val nickname = ListFragmentArgs.fromBundle(requireArguments()).nickname
        val binding = FragmentListBinding.inflate(inflater)
        val viewModel: ListViewModel by viewModels()

        val adapter = CoolAdapter(requireContext())
        binding.listView.adapter = adapter
        binding.listView.setOnItemClickListener { parent, view, position, id ->
            val user = viewModel.list.value?.get(position) ?: error("No users UwU")
            findNavController().navigate(ListFragmentDirections.actionListFragmentToChatFragment(nickname,user))
        }

        viewModel.list.observe(viewLifecycleOwner) {
            it ?: return@observe

            Log.d(javaClass.simpleName, it.joinToString())
            adapter.addAll(it)
        }

        return binding.root
    }
}