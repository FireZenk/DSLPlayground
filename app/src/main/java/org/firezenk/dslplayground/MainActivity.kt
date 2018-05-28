package org.firezenk.dslplayground

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import org.firezenk.dslplayground.solutions.DSLAdapter
import org.firezenk.dslplayground.solutions.adapterDSL
import org.firezenk.dslplayground.solutions.dsl
import org.firezenk.dslplayground.util.dsl

class MainActivity : AppCompatActivity(), RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private val itemDecoration by lazy {
        DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
    }

    private val adapter: DSLAdapter<ListItem> by lazy {
        adapterDSL<ListItem>(list, itemDecoration) {
            item(R.layout.list_item) {
                bindView { view, item ->
                    view.title.text = item.title
                    view.subtitle.text = item.subtitle
                    view.image.dsl { url = item.image }
                }
                click {
                    showToast("Clicked: ${it.title}")
                }
            }
            diff {
                it.id
            }
        }
    }

    private val charactersList = mutableListOf(
            ListItem(1, "Rick Sanchez",
                    "A genius mad scientist who is the father of Beth Smith and the maternal grandfather of Morty",
                    "https://upload.wikimedia.org/wikipedia/en/thumb/e/e9/Rick_and_Morty_characters_-_Rick_Sanchez.jpg/110px-Rick_and_Morty_characters_-_Rick_Sanchez.jpg"),
            ListItem(2, "Morty Smith",
                    "Rick's 14-year-old grandson who is frequently dragged into Rick's misadventures.",
                    "https://upload.wikimedia.org/wikipedia/en/thumb/d/d7/Rick_and_Morty_characters_-_Morty_Smith.jpg/110px-Rick_and_Morty_characters_-_Morty_Smith.jpg"),
            ListItem(3, "Beth Smith",
                    "Rick's daughter, Summer and Morty's mother, and Jerry's wife.",
                    "https://upload.wikimedia.org/wikipedia/en/thumb/2/2f/Rick_and_Morty_characters_-_Beth_Smith.jpg/110px-Rick_and_Morty_characters_-_Beth_Smith.jpg"),
            ListItem(4, "Jerry Smith",
                    "Summer and Morty's insecure father, Beth's husband, and Rick's son-in-law.",
                    "https://upload.wikimedia.org/wikipedia/en/thumb/5/54/Rick_and_Morty_characters_-_Jerry_Smith.jpg/110px-Rick_and_Morty_characters_-_Jerry_Smith.jpg"),
            ListItem(5, "Summer Smith",
                    "Morty's 17-year-old older sister, a more conventional and often superficial teenager.",
                    "https://upload.wikimedia.org/wikipedia/en/thumb/e/ee/Rick_and_Morty_characters_-_Summer_Smith.jpg/110px-Rick_and_Morty_characters_-_Summer_Smith.jpg")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupAdapter()

        setupNavigation()
    }

    private fun setupNavigation() = navigation.dsl {
        menu = R.menu.navigation
        default = R.id.navigation_home
        +item {
            id = R.id.navigation_home
            action = { showToast(R.string.title_home) }
        }
        +item {
            id = R.id.navigation_dashboard
            action = { showToast(R.string.title_dashboard) }
        }
        +item {
            id = R.id.navigation_notifications
            action = { showToast(R.string.title_notifications) }
        }
    }

    private fun setupAdapter() {
        adapter.submitList(charactersList)

        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(list)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is DSLAdapter.ViewHolder<*>) {
            val name = charactersList[viewHolder.adapterPosition].title

            val deletedIndex = viewHolder.adapterPosition
            val deletedItem = charactersList[deletedIndex]

            charactersList.removeAt(position)
            adapter.notifyItemRemoved(position)

            val snackbar = Snackbar.make(container, "$name removed!", Snackbar.LENGTH_LONG)
            with(snackbar) {
                setAction("UNDO",  {
                    charactersList.add(deletedIndex, deletedItem)
                    adapter.notifyItemInserted(deletedIndex)
                })
                setActionTextColor(Color.YELLOW)
                show()
            }
        }
    }

    private fun showToast(id: Int) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(stringId: String) {
        Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
    }
}
