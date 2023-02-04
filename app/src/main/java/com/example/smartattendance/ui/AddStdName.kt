package com.example.smartattendance.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.adapters.stdAdapterClass
import com.example.smartattendance.model.CardModel
import com.google.firebase.database.*

class AddStdName : AppCompatActivity(), stdAdapterClass.stdItemCLicked{
    private lateinit var email:String
    private lateinit var stdName: EditText
    private lateinit var stdRoll: TextView
    private lateinit var addSdt: Button
    private lateinit var startAttend: Button
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private lateinit var ref2: DatabaseReference
    private lateinit var userArrayList: ArrayList<CardModel>
    private lateinit var adapter: stdAdapterClass
    private lateinit var semName: String
    private lateinit var streamName: String
    var roll = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_std_name)

        stdName = findViewById(R.id.sdtName)
        stdRoll = findViewById(R.id.sdtRoll)
        startAttend = findViewById(R.id.startAttendance)
        addSdt = findViewById(R.id.addStudent)
        userRecyclerView=findViewById(R.id.recyclerView)
        firebaseDatabase = FirebaseDatabase.getInstance()
        ref = firebaseDatabase.getReference("BIMS")
        ref2 = firebaseDatabase.getReference("BIMS")
        semName = intent.getStringExtra("semName").toString()
        streamName = intent.getStringExtra("streamName").toString()
        val sh = getSharedPreferences(getString(R.string.user_id), MODE_PRIVATE);
        email = sh.getString(getString(R.string.id), "")!!
        data()
        addSdt.setOnClickListener() {


            if (stdName.text.isNotEmpty() ) {


                val cardObj = CardModel(stdName.text.toString(),stdRoll.text.toString(),semName,null,0)

                ref.child(email).child(streamName).child("semID").child(semName)
                    .child("nameId").child(stdRoll.text.toString()).child("roll")
                    .setValue(stdRoll.text.toString())
                ref.child(email).child(streamName).child("semID").child(semName)
                    .child("nameId").child(stdRoll.text.toString()).child("name")
                    .setValue(stdName.text.toString())
                ref.child(email).child(streamName).child("semID").child(semName)
                    .child("nameId").child(stdRoll.text.toString()).child("present")
                    .setValue(0)

                ref.child(email).child(streamName).child("semID").child(semName)
                    .child("nameId").child(stdRoll.text.toString()).setValue(cardObj)

            } else {
                Toast.makeText(this, "enter the name and roll", Toast.LENGTH_SHORT).show()
            }
            data()
            stdName.text.clear()
        }


        startAttend.setOnClickListener {
            val intent=Intent(this, MainActivity::class.java)
            intent.putExtra("Stream",streamName)
            intent.putExtra("Sem",semName)
            startActivity(intent)
            finish()
        }

    }

    private fun validteData(): Boolean {
        if (stdName.text.isNotEmpty() && stdRoll.text.isNotEmpty()) {

        }

        return false

    }

    private fun data() {
        userRecyclerView.layoutManager= LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        userArrayList= arrayListOf()

        removeUserData()
        getUserData()
    }
    private fun getUserData() {

            ref2 = firebaseDatabase.getReference("BIMS").child(email)
                .child(streamName).child("semID").child(semName)
                .child("nameId")

            ref2.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        for(userSnapshot in snapshot.children){

                            val user=userSnapshot.getValue(CardModel::class.java)
                            userArrayList.add(user!!)
                            roll = user.roll!!.toInt()
                        }
                        roll++
                        stdRoll.text = roll.toString()

                        userRecyclerView.adapter= stdAdapterClass(userArrayList,this@AddStdName)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    private fun removeUserData() {
        ref2 = firebaseDatabase.getReference("BIMS").child(email)
            .child(streamName).child("semID").child(semName)
            .child("nameId")

        ref2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                userArrayList.clear()
                adapter = stdAdapterClass(userArrayList,this@AddStdName)
                userRecyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onItemCLickedStd(itemCLicked: String?) {

        val intent=Intent(this, StdPresentActivity::class.java)
        intent.putExtra("stream",streamName)
        intent.putExtra("sem",semName)
        intent.putExtra("roll",itemCLicked.toString())
        startActivity(intent)

    }


}