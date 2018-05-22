package org.firezenk.dslplayground

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupAdapter()

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                showToast(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                showToast(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                showToast(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun setupAdapter() {
        adapter.setOnItemClickListener {
            showToast("Clicked: $it.title")
        }

        adapter.submitList(listOf(
                ListItem(1, "Rick Sanchez", "A genius mad scientist who is the father of Beth Smith and the maternal grandfather of Morty"),
                ListItem(1, "Morty Smith", "Rick's 14-year-old grandson who is frequently dragged into Rick's misadventures."),
                ListItem(1, "Beth Smith", "Rick's daughter, Summer and Morty's mother, and Jerry's wife."),
                ListItem(1, "Jerry Smith", "Summer and Morty's insecure father, Beth's husband, and Rick's son-in-law."),
                ListItem(1, "Summer Smith", "Morty's 17-year-old older sister, a more conventional and often superficial teenager.")
        ))

        list.adapter = adapter
    }

    private fun showToast(id: Int) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(stringId: String) {
        Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
    }
}
