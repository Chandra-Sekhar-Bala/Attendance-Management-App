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
import com.example.smartattendance.adapters.semDataClass
import com.example.smartattendance.database.Sem.semAdapterClass
import com.google.firebase.database.*

class SemAdd :  AppCompatActivity(),semAdapterClass.semItemCLicked{
    lateinit var SemName:EditText
    lateinit var SemAddButton:Button
    lateinit var userRecyclerView: RecyclerView
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var ref: DatabaseReference
    lateinit var ref2:DatabaseReference
    lateinit var userArrayList:ArrayList<semDataClass>
    lateinit var adapter: semAdapterClass
    lateinit var StreamName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sem_add)

        SemName=findViewById(R.id.AddSemText)
        SemAddButton=findViewById(R.id.AddSemButton)
        userRecyclerView=findViewById(R.id.recyclerView)
        firebaseDatabase= FirebaseDatabase.getInstance()
        ref=firebaseDatabase.getReference("BIMS")
        ref2=firebaseDatabase.getReference("BIMS")
        StreamName= intent.getStringExtra("StrName").toString()

        data()

        SemAddButton.setOnClickListener(){
            if (SemName.text.isNotEmpty()){
                ref.child("user_Email").child(StreamName).child("semID").child(SemName.text.toString())
                    .child("sem").setValue(SemName.text.toString())
                    data()
            }
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

        ref2 = firebaseDatabase.getReference("BIMS").child("user_Email").child(StreamName)
            .child("semID")

        var n=1
        ref2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children){

                        val user=userSnapshot.getValue(semDataClass::class.java)
                        userArrayList.add(user!!)

                    }

                    userRecyclerView.adapter= semAdapterClass(userArrayList,this@SemAdd)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun removeUserData() {
        ref2 = firebaseDatabase.getReference("BIMS").child("user_Email").child(StreamName)
            .child("semID")

        ref2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                userArrayList.clear()
                adapter = semAdapterClass(userArrayList,this@SemAdd)
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

    override fun onItemCLicked(item: String) {
        val intent= Intent(this,AddName::class.java)
        intent.putExtra("semName",item)
        intent.putExtra("streamName",StreamName)
        startActivity(intent)

        Toast.makeText(this,"item click $item",Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteClicked(itemCLicked: String?) {
        ref.child("user_Email").child(StreamName).child("semID").child(itemCLicked.toString()).removeValue()
        Toast.makeText(this," the delete button click $itemCLicked",Toast.LENGTH_SHORT).show()
        data()
    }
}