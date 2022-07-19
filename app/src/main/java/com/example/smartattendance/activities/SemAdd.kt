package com.example.smartattendance.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.adapters.semDataClass
import com.example.smartattendance.database.Sem.semAdapterClass
import com.google.firebase.database.*
import java.text.FieldPosition

class SemAdd :  AppCompatActivity(),semAdapterClass.semItemCLicked{
    lateinit var email:String
    lateinit var SemName:EditText
    lateinit var SemAddButton:Button
    lateinit var userRecyclerView: RecyclerView
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var ref: DatabaseReference
    lateinit var ref2:DatabaseReference
    lateinit var userArrayList:ArrayList<semDataClass>
    lateinit var adapter: semAdapterClass
    lateinit var StreamName:String
    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sem_add)

        SemName=findViewById(R.id.AddSemText)
        SemAddButton=findViewById(R.id.AddSemButton)
        userRecyclerView=findViewById(R.id.recyclerView)
        progressBar=findViewById(R.id.progressbar3)


        firebaseDatabase= FirebaseDatabase.getInstance()
        ref=firebaseDatabase.getReference("BIMS")
        ref2=firebaseDatabase.getReference("BIMS")
        StreamName= intent.getStringExtra("StrName").toString()

        val sh = getSharedPreferences(getString(R.string.user_id), MODE_PRIVATE);
        email = sh.getString(getString(R.string.id), "")!!

        data()

        SemAddButton.setOnClickListener(){
            if (SemName.text.isNotEmpty()){
                ref.child(email).child(StreamName).child("semID").child(SemName.text.toString())
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
        Log.e("TAGTAG","Okea called: ")
        ref2 = firebaseDatabase.getReference("BIMS").child(email).child(StreamName)
            .child("semID")

        var n=1
        ref2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children){

                        val user=userSnapshot.getValue(semDataClass::class.java)
                        userArrayList.add(user!!)
                    }
                    progressBar.visibility = View.GONE
                    userRecyclerView.adapter= semAdapterClass(userArrayList,this@SemAdd)
                }else{
                    progressBar.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun removeUserData() {
        ref2 = firebaseDatabase.getReference("BIMS").child(email).child(StreamName)
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
    }

    override fun onItemCLicked(item: String) {
        val intent= Intent(this,AddName::class.java)
        intent.putExtra("semName",item)
        intent.putExtra("streamName",StreamName)
        startActivity(intent)

    }

    override fun onDeleteClicked(itemCLicked: String?, position: Int) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this@SemAdd)

        builder.setTitle("Delete sem/sec!")
        builder.setMessage("Are you sure, you want to delete this sem/sec?")

        builder.setPositiveButton(
            "YES"
        ) { dialog, _ -> // Delete data from firebase

            ref.child(email).child(StreamName).child("semID").child(itemCLicked.toString()).removeValue()
            data()
            adapter.notifyItemRemoved(position)
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