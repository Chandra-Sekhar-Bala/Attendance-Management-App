package com.example.smartattendance.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.adapters.semDataClass
import com.example.smartattendance.database.Sem.semAdapterClass
import com.example.smartattendance.database.pre.presentAdapterClass
import com.example.smartattendance.database.pre.presentDataClass
import com.google.android.material.color.ColorRoles
import com.google.firebase.database.*

class presentStd : AppCompatActivity() {
    lateinit var userRecyclerView: RecyclerView
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var ref: DatabaseReference
    lateinit var ref2: DatabaseReference
    lateinit var userArrayList:ArrayList<presentDataClass>
    lateinit var adapter: presentAdapterClass
    lateinit var email:String
    lateinit var streamP:String
    lateinit var semP:String
    lateinit var rollP:String
    lateinit var preRoll:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_present_std2)

        preRoll=findViewById(R.id.preRoll)

        userRecyclerView=findViewById(R.id.pRecyclerView)
        firebaseDatabase= FirebaseDatabase.getInstance()
        ref=firebaseDatabase.getReference("BIMS")
        ref2=firebaseDatabase.getReference("BIMS")

        streamP=intent.getStringExtra("stream")!!
        semP=intent.getStringExtra("sem")!!
        rollP=intent.getStringExtra("roll")!!

        preRoll.text=rollP

        val sh = getSharedPreferences("UserID", MODE_PRIVATE);
        email = sh.getString("id", "")!!
        data()


    }
    private fun data() {
        userRecyclerView.layoutManager= LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        userArrayList= arrayListOf()

        //removeUserData()
        getUserData()
    }
    private fun getUserData() {
        ref2 = firebaseDatabase.getReference("BIMS").child(email)
            .child(streamP).child("semID").child(semP)
            .child("nameId").child(rollP).child("presentID")
        var n=1
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    for(userSnapshot in snapshot.children){
                        Log.e("cc",n.toString())
                        val user=userSnapshot.getValue(presentDataClass::class.java)
                        userArrayList.add(user!!)
                        n++
                    }
                    userRecyclerView.adapter= presentAdapterClass(userArrayList,this@presentStd)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}