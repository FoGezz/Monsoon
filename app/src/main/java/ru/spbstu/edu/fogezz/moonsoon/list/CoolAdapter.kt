package ru.spbstu.edu.fogezz.moonsoon.list


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import ru.spbstu.edu.fogezz.moonsoon.R
import ru.spbstu.edu.fogezz.moonsoon.databinding.ListItemBinding
import ru.spbstu.edu.fogezz.moonsoon.network.User

class CoolAdapter(context: Context) : ArrayAdapter<User>(context, R.layout.list_item) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemBinding = if (convertView == null)
            ListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        else
            ListItemBinding.bind(convertView)

        val user = getItem(position)

        user?.let { itemBinding.textView.text = it.nickname }

        return itemBinding.root
    }
}

//        val a = object : BaseAdapter {
//            override fun getCount(): Int {
//                TODO("Not yet implemented")
//            }
//
//            override fun getItem(position: Int): Any {
//                TODO("Not yet implemented")
//            }
//
//            override fun getItemId(position: Int): Long {
//                TODO("Not yet implemented")
//            }
//
//            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//                TODO("Not yet implemented")
//            }
//        }
