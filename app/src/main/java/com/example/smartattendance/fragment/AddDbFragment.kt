package com.example.smartattendance.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.database.streamAdapterClass
import com.example.smartattendance.database.streamDataClass
import com.google.firebase.database.*


class AddDbFragment : Fragment(), streamAdapterClass.StreamItemCLicked {

    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    lateinit var myRef2: DatabaseReference
    lateinit var myRef3: DatabaseReference
    lateinit var addStreamData: Button
    lateinit var userStreamName: EditText
    lateinit var userRecyclerView: RecyclerView
    lateinit var userArrayList: ArrayList<streamDataClass>
    lateinit var adapter: streamAdapterClass


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
        addStreamData=view.findViewById(R.id.Add_Stream)
        userStreamName=view.findViewById(R.id.user_stream_item)

        userRecyclerView=view.findViewById(R.id.Add_RecyclerView)

    }
    override fun onResume() {
        data()
        super.onResume()
        addStreamData.setOnClickListener(){
            if(userStreamName.text.isEmpty()){
                Toast.makeText(context, "please enter semester", Toast.LENGTH_SHORT).show()
            }
            else{
                myRef.child("user_Email").child(userStreamName.text.toString())
                    .child("StreamName").setValue(userStreamName.text.toString())
            }
            data()
        }


    }

    private fun data() {
        userRecyclerView.layoutManager= LinearLayoutManager(context)
        userRecyclerView.setHasFixedSize(true)

        userArrayList= arrayListOf()

        removeUserData()
        getUserData()
    }

    private fun getUserData() {
        myRef2 = database.getReference("BIMS").child("user_Email")

        myRef2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children){

                        val user=userSnapshot.getValue(streamDataClass::class.java)
                        userArrayList.add(user!!)
                    }

                    adapter= streamAdapterClass(userArrayList,this@AddDbFragment)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun removeUserData() {
        myRef2 =  database.getReference("BIMS").child("user_Email").child("StreamName")

        myRef2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                userArrayList.clear()
                adapter = streamAdapterClass(userArrayList,this@AddDbFragment)
                userRecyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        fun onClick(position: Int) {
            Toast.makeText(context, "onClick $position", Toast.LENGTH_LONG).show()
        }
    }

    override fun onItemCLicked(item: String) {

//        Toast.makeText(context, "CLicked item $item", Toast.LENGTH_SHORT).show()

        val nextFrag = AddSemFragment()
        val bundle = Bundle()
        bundle.putString("key", item)
        nextFrag.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, nextFrag, "NewFragment")
            .addToBackStack(null)
            .commit()
    }

}