package com.example.smartattendance.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.database.pre.preAdapterClass
import com.example.smartattendance.database.pre.preDataClass
import com.google.firebase.database.*

class stdDate : AppCompatActivity() {
    lateinit var userRecyclerView: RecyclerView
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var ref: DatabaseReference
    lateinit var ref2: DatabaseReference
//    lateinit var userArrayList: ArrayList<preDataClass>
//    lateinit var adapter:preAdapterClass
    lateinit var userArrayList:ArrayList<preDataClass>
    lateinit var adapter: preAdapterClass
    lateinit var StreamP:String
    lateinit var SemP:String
    lateinit var RollP:String
    var pDate=1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_std_date)

        userRecyclerView=findViewById(R.id.recyclerViewP)
        firebaseDatabase = FirebaseDatabase.getInstance()

        ref = firebaseDatabase.getReference("BIMS")
        ref2 = firebaseDatabase.getReference("BIMS")

        StreamP=intent.getStringExtra("Stream").toString()
        SemP=intent.getStringExtra("sem").toString()
        RollP=intent.getStringExtra("roll").toString()
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
        ref2 = firebaseDatabase.getReference("BIMS").child("user_Email")
            .child(StreamP).child("semID").child(SemP)
            .child("nameId").child(RollP).child("PresentID")

        var n=1
        ref2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children){

                        Toast.makeText(this@stdDate,"call "+n,Toast.LENGTH_SHORT).show()
//                        val user=userSnapshot.getValue(preDataClass::class.java)
//                        userArrayList.add(user!!)
                        n++

                    }

                    userRecyclerView.adapter= preAdapterClass(userArrayList,this@stdDate)
                }
                else{
                    Toast.makeText(this@stdDate,"data not found",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}