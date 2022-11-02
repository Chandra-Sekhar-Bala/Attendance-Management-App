package com.example.smartattendance.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.adapters.AddDBAdapterClass
import com.example.smartattendance.model.addDBDataClass
import com.google.firebase.database.*


class AddDBFragment : Fragment(), AddDBAdapterClass.StreamItemCLicked {

    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    private lateinit var myRef2: DatabaseReference
    private lateinit var myRef3: DatabaseReference
    private lateinit var addStreamData: Button
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<addDBDataClass>
    private lateinit var adapter: AddDBAdapterClass
    private lateinit var email: String
    private lateinit var user_str_item: EditText
    private lateinit var progressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_db, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("BIMS")
        myRef2 = database.getReference("BIMS")
        userRecyclerView = view.findViewById(R.id.rr)
        val sh = requireActivity().getSharedPreferences(getString(R.string.user_id), Context.MODE_PRIVATE)
        email = sh.getString(getString(R.string.id), "")!!

        addStreamData = view.findViewById(R.id.Add_Stream)
        user_str_item = view.findViewById(R.id.user_str_item)
        progressBar = view.findViewById(R.id.progressbar2)
        data()
    }

    override fun onResume() {

        super.onResume()
        addStreamData.setOnClickListener {
            if (user_str_item.text.isNotEmpty()) {

                myRef.child(email).child(user_str_item.text.toString()).child("StreamName")
                    .setValue(user_str_item.text.toString())

                data()
            }
        }

    }


    private fun data() {
        userRecyclerView.layoutManager = LinearLayoutManager(context)
        userRecyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()

        removeUserData()
        getUserData()
    }

    private fun getUserData() {
        myRef2 = database.getReference("BIMS").child(email)

        myRef2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {

                        val user = userSnapshot.getValue(addDBDataClass::class.java)
                        userArrayList.add(user!!)
                    }
                    adapter = AddDBAdapterClass(userArrayList, this@AddDBFragment)
                }
                progressBar.visibility = View.GONE
            }
            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
                TODO("Not yet implemented")
            }

        })
    }

    private fun removeUserData() {
        myRef2 = database.getReference("BIMS").child(email).child("StreamName")

        myRef2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                userArrayList.clear()
                adapter = AddDBAdapterClass(userArrayList, this@AddDBFragment)
                userRecyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onItemCLicked(item: String) {
        val intent = Intent(context, AddSemActivity::class.java)
        intent.putExtra("StrName", item)
        startActivity(intent)
    }

    override fun onDeleteClicked(itemCLicked: String?, position : Int) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Delete Stream!")
        builder.setMessage("Are you sure, you want to delete this stream?")

        builder.setPositiveButton(
            "YES"
        ) { dialog, _ -> // Delete data from firebase
            myRef.child(email).child(itemCLicked.toString()).removeValue()
            adapter.notifyItemRemoved(position)
            data()
            dialog.dismiss()
        }
        builder.setNegativeButton(
            "NO"
        ) { dialog, _ -> // Do nothing
            dialog.dismiss()
        }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

}