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
import com.example.smartattendance.database.StreamAdapter
import com.example.smartattendance.database.StreamData
import com.google.firebase.database.*

class StreamAdd : AppCompatActivity(), StreamAdapter.StreamItemCLicked{
    lateinit var user_str_item:EditText
    lateinit var Add_Str:Button
    lateinit var firebaseDatabase:FirebaseDatabase
    lateinit var ref:DatabaseReference
    lateinit var Ref2:DatabaseReference
    lateinit var userRecyclerView:RecyclerView
    lateinit var userArrayList:ArrayList<StreamData>
    lateinit var adapter:StreamAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream_add)

        userRecyclerView=findViewById(R.id.Add_RecyclerView_str)
        user_str_item=findViewById(R.id.user_str_item)
        Add_Str=findViewById(R.id.Add_Str)
        firebaseDatabase= FirebaseDatabase.getInstance()
        ref=firebaseDatabase.getReference("BIMS")
        Ref2=firebaseDatabase.getReference("BIMS")
        data()


        Add_Str.setOnClickListener(){
            if (user_str_item.text.isNotEmpty()){

                ref.child("user_Email").child(user_str_item.text.toString()).child("StreamName")
                    .setValue(user_str_item.text.toString())
//                ref.child("user_Email").child(user_str_item.text.toString()).child("StreamName")
//                    .setValue(user_str_item.text.toString())
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
        Ref2 = firebaseDatabase.getReference("BIMS").child("user_Email")

        Ref2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children){

                        //Toast.makeText(this@StreamAdd,"call "+n,Toast.LENGTH_SHORT).show()
                        val user=userSnapshot.getValue(StreamData::class.java)
                        userArrayList.add(user!!)

                    }

                   userRecyclerView.adapter= StreamAdapter(userArrayList,this@StreamAdd)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun removeUserData() {
        Ref2 = firebaseDatabase.getReference("BIMS").child("user_Email")

        Ref2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                userArrayList.clear()
                adapter = StreamAdapter(userArrayList,this@StreamAdd)
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
        val intent=Intent(this,SemAdd::class.java)
        intent.putExtra("StrName",item)
        startActivity(intent)
        Toast.makeText(this,"item click $item",Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteClicked(itemCLicked: String?) {
        ref.child("user_Email").child(itemCLicked.toString()).removeValue()
        Toast.makeText(this," the delete button click $itemCLicked",Toast.LENGTH_SHORT).show()
        data()
    }




}