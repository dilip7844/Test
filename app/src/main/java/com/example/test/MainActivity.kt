package com.example.test

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.test.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val laptopFragment = ProductFragment()
    val mobileFragment = ProductFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        initTabs()
        getMobiles()
        getLaptops()
        var viewModel = ViewModelProvider(this).get(VModel::class.java)

        viewModel.repo?.getCartItems()?.observe(this) {
            if (it.isEmpty())
                binding.layoutCheckout.visibility = GONE
            else {
                binding.layoutCheckout.visibility = VISIBLE
                binding.tvCartItems.text = "${it.size} items"
            }
        }
        binding.layoutCheckout.setOnClickListener {
            Common.openFragment(this@MainActivity, CartFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuCart -> {
                Common.openFragment(this@MainActivity, CartFragment())
            }

            R.id.menuLogout -> {
                SharedPrefs.setBoolean(SharedPrefs.ISLOGGEDIN, false)
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getLaptops() {
        val data: MutableList<Product> = ArrayList()
        data.add(Product(Common.getDrawable(this, R.drawable.laptop1), "Acer Aspire Z,4 GB, 500Gb SSF", 49315))
        data.add(Product(Common.getDrawable(this, R.drawable.laptop2), "Asus Notebook, 6 GB, 1TB SSD ", 31531))
        data.add(Product(Common.getDrawable(this, R.drawable.laptop3), "HP Laptop, 8 GB, 1TB SSD", 59141))
        data.add(Product(Common.getDrawable(this, R.drawable.laptop1), "Chromebook Pro 3 GB", 23635))
        data.add(Product(Common.getDrawable(this, R.drawable.laptop1), "Acer Aspire Z,4 GB, 500Gb SSF", 49315))
        data.add(Product(Common.getDrawable(this, R.drawable.laptop2), "Asus Notebook, 6 GB, 1TB SSD ", 31531))
        data.add(Product(Common.getDrawable(this, R.drawable.laptop3), "HP Laptop, 8 GB, 1TB SSD", 59141))
        data.add(Product(Common.getDrawable(this, R.drawable.laptop1), "Chromebook Pro 3 GB", 23635))
        laptopFragment.setList(data)
    }

    private fun getMobiles() {
        val data: MutableList<Product> = ArrayList()
        data.add(Product(Common.getDrawable(this, R.drawable.mobile1), "Samsung Galaxy 5,6 GB RAM, 128GB Storage", 52315))
        data.add(Product(Common.getDrawable(this, R.drawable.mobile2), "Asus note pro , 6 GB RAM, 128GB Storage", 30531))
        data.add(Product(Common.getDrawable(this, R.drawable.mobile3), "Apple Iphone X, 6 GB, 256TB Storage", 63141))
        data.add(Product(Common.getDrawable(this, R.drawable.mobile1), "Redmi note 5 , 3 GB, 32 GB Storage", 10235))
        data.add(Product(Common.getDrawable(this, R.drawable.mobile1), "Samsung Galaxy 5,6 GB RAM, 128GB Storage", 52315))
        data.add(Product(Common.getDrawable(this, R.drawable.mobile2), "Asus note pro , 6 GB RAM, 128GB Storage", 30531))
        data.add(Product(Common.getDrawable(this, R.drawable.mobile3), "Apple Iphone X, 6 GB, 256TB Storage", 63141))
        data.add(Product(Common.getDrawable(this, R.drawable.mobile1), "Redmi note 5 , 3 GB, 32 GB Storage", 10235))
        mobileFragment.setList(data)
    }

    private fun initTabs() {
        binding.viewPager.adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun createFragment(position: Int): Fragment {
                return return when (position) {
                    0 -> mobileFragment
                    1 -> laptopFragment
                    else -> Fragment()
                }
            }

            override fun getItemCount(): Int {
                return 2
            }
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager, true, true) { tab, position ->
            when (position) {
                0 -> tab.text = "Mobiles"
                1 -> tab.text = "Laptops"
            }
        }.attach()
    }
}