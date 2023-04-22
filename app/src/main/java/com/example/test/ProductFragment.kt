package com.example.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.FragmentProductBinding
import com.example.test.databinding.ItemProductBinding

class ProductFragment : Fragment() {
    private var adapter = object : RecyclerView.Adapter<ProductHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
            return ProductHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun getItemCount(): Int {
            return data?.size!!
        }

        override fun onBindViewHolder(holder: ProductHolder, position: Int) {
            var model = data?.get(position)
            holder.itemProductBinding.model = model
        }
    }
    private var data: MutableList<Product>? = ArrayList()
    private lateinit var binding: FragmentProductBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    inner class ProductHolder(var itemProductBinding: ItemProductBinding) : RecyclerView.ViewHolder(itemProductBinding.root) {
        init {
            itemProductBinding.root.clipToOutline = true
            itemProductBinding.btnAddToCart.setOnClickListener {
            }
        }
    }

    fun setList(data: MutableList<Product>) {
        data.let {
            this.data?.addAll(it)
        }

        adapter.notifyDataSetChanged()
    }
}