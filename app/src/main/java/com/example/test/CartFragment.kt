package com.example.test

import android.R.attr.data
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.FragmentCartBinding
import com.example.test.databinding.ItemCartBinding

class CartFragment : DialogFragment() {
    lateinit var binding: FragmentCartBinding

    companion object {
        private var data: MutableList<Product>? = ArrayList()
    }

    private var adapter = object : RecyclerView.Adapter<CartHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
            return CartHolder(ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun getItemCount(): Int {
            return data?.size!!
        }

        override fun onBindViewHolder(holder: CartHolder, position: Int) {
            var model = data?.get(position)
            holder.itemCartBinding.model = model
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        var viewModel = ViewModelProvider(this).get(VModel::class.java)
        binding.recyclerView.adapter = adapter
        viewModel.repo?.getCartItems()?.observe(this) {
            data?.clear()
            data?.addAll(it)
            adapter.notifyDataSetChanged()
            if (it.isEmpty()) {
                binding.layoutDone.visibility = GONE
                Common.makeToast("No items in cart")
            } else {
                binding.layoutDone.visibility = VISIBLE
                binding.tvCartItems.text = "${it.size} items"
            }
        }

        binding.layoutDone.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                // .setTitle("")
                .setMessage("Your order has been placed successfully")
                .setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        Repo.clearCart()
                        p0?.dismiss()
                    }
                })
                .show()
        }
        return binding.root
    }

    inner class CartHolder(var itemCartBinding: ItemCartBinding) : RecyclerView.ViewHolder(itemCartBinding.root) {
        init {
            itemCartBinding.root.clipToOutline = true
            itemCartBinding.btnRemove.setOnClickListener {
                Repo.removeFromCart(data?.get(adapterPosition)!!)
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }
}