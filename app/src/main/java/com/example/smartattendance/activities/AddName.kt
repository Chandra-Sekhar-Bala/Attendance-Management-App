package com.example.smartattendance.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.database.student_name.stdAdapterClass
import com.example.smartattendance.database.student_name.stdDataClass
import com.google.firebase.database.*

class AddName : AppCompatActivity(),stdAdapterClass.stdItemCLicked{
    lateinit var stdName: EditText
    lateinit var stdRoll: EditText
    lateinit var stdPresent: EditText
    lateinit var addSdt: Button
    lateinit var startAttend: Button
    lateinit var userRecyclerView: RecyclerView
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var ref: DatabaseReference
    lateinit var ref2: DatabaseReference
    lateinit var userArrayList: ArrayList<stdDataClass>
    lateinit var adapter: stdAdapterClass
    lateinit var semName: String
    lateinit var streamName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_name)

        stdName = findViewById(R.id.sdtName)
        stdRoll = findViewById(R.id.sdtRoll)
        stdPresent = findViewById(R.id.sdtPresent)
        startAttend = findViewById(R.id.startAttendance)
        addSdt = findViewById(R.id.addStudent)
        userRecyclerView=findViewById(R.id.recyclerView)
        firebaseDatabase = FirebaseDatabase.getInstance()
        ref = firebaseDatabase.getReference("BIMS")
        ref2 = firebaseDatabase.getReference("BIMS")
        semName = intent.getStringExtra("semName").toString()
        streamName = intent.getStringExtra("streamName").toString()
        data()
        addSdt.setOnClickListener() {
            if (stdName.text.isNotEmpty() && stdRoll.text.isNotEmpty()) {
                ref.child("user_Email").child(streamName).child("semID").child(semName)
                    .child("nameId").child(stdRoll.text.toString()).child("roll")
                    .setValue(stdRoll.text.toString())
                ref.child("user_Email").child(streamName).child("semID").child(semName)
                    .child("nameId").child(stdRoll.text.toString()).child("name")
                    .setValue(stdName.text.toString())

                if (stdPresent.text.isEmpty()) {
                    ref.child("user_Email").child(streamName).child("semID").child(semName)
                        .child("nameId").child(stdRoll.text.toString()).child("Present")
                        .setValue("0")
                }
                else {
                    ref.child("user_Email").child(streamName).child("semID").child(semName)
                        .child("nameId").child(stdRoll.text.toString()).child("Present")
                        .setValue(stdPresent.text.toString())
                }
            } else {
                Toast.makeText(this, "enter the name and roll", Toast.LENGTH_SHORT).show()
            }
            data()
        }
        startAttend.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            intent.putExtra("Stream",streamName)
            intent.putExtra("Sem",semName)
            startActivity(intent)
        }

    }
    private fun data() {
        userRecyclerView.layoutManager= LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        userArrayList= arrayListOf()

        removeUserData()
        getUserData()
    }
    private fun getUserData() {

            ref2 = firebaseDatabase.getReference("BIMS").child("user_Email")
                .child(streamName).child("semID").child(semName)
                .child("nameId")

            ref2.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        for(userSnapshot in snapshot.children){

                            val user=userSnapshot.getValue(stdDataClass::class.java)
                            userArrayList.add(user!!)

                        }

                        userRecyclerView.adapter= stdAdapterClass(userArrayList,this@AddName)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    private fun removeUserData() {
        ref2 = firebaseDatabase.getReference("BIMS").child("user_Email")
            .child(streamName).child("semID").child(semName)
            .child("nameId")

        ref2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                userArrayList.clear()
                adapter = stdAdapterClass(userArrayList,this@AddName)
                userRecyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
//        fun onClick(position: Int) {
//            Toast.makeText(this, "onClick $position", Toast.LENGTH_LONG).show()
//        }
    }

    override fun onDeleteClicked(itemCLicked: String?) {
        ref.child("user_Email").child(streamName).child("semID")
            .child(semName).child("nameId").child(itemCLicked!!).removeValue()

        Toast.makeText(this," the delete button click $itemCLicked",Toast.LENGTH_SHORT).show()
        data()
    }

}