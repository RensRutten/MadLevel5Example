package com.example.madlevel5example.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel5example.R
import com.example.madlevel5example.model.Reminder
import kotlinx.android.synthetic.main.fragment_reminders.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RemindersFragment : Fragment() {


    private val reminders = arrayListOf<Reminder>()
    private var reminderAdapter = ReminderAdapter(reminders)

    private lateinit var viewManager: RecyclerView.LayoutManager
    private val viewModel: ReminderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeAddReminderResult()


    }

    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter
        rvReminder.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvReminder.adapter = reminderAdapter
        rvReminder.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        createItemTouchHelper().attachToRecyclerView(rvReminder)
    }


    private fun observeAddReminderResult() {
        viewModel.reminders.observe(viewLifecycleOwner, Observer { reminders ->
            this@RemindersFragment.reminders.clear()
            this@RemindersFragment.reminders.addAll(reminders)
            reminderAdapter.notifyDataSetChanged()
        })
    }


    /**
     * Create a touch helper to recognize when a user swipes an item from a recycler view.
     * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
     * and uses callbacks to signal when a user is performing these actions.
     */
    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val reminderToDelete = reminders[position]

//                CoroutineScope(Dispatchers.Main).launch {
//                    withContext(Dispatchers.IO) {
//                        reminderRepository.deleteReminder(reminderToDelete)
//                    }
//                    getRemindersFromDatabase()
//                }
                viewModel.deleteReminder(reminderToDelete)
            }
        }
        return ItemTouchHelper(callback)
    }

}
