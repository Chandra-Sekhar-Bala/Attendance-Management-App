package com.example.smartattendance.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.smartattendance.R
import com.example.smartattendance.adapters.CardStackAdapter
import com.example.smartattendance.model.CardModel
import com.google.firebase.database.*
import com.yuyakaido.android.cardstackview.*
import java.time.LocalDate

class AttendanceFragment(val stream: String?, val sem: String?) : Fragment() {

    var firebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var ref: DatabaseReference
    lateinit var ref2: DatabaseReference
    private var manager: CardStackLayoutManager? = null
    private var adapter: CardStackAdapter? = null
    private lateinit var  db : DatabaseReference
    private val list = ArrayList<CardModel>()
    private lateinit var cardStackView : CardStackView
    private var roll = 0
    lateinit var noData: TextView
    lateinit var noDataArrow: ImageView
    lateinit var email:String
    var total = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_attendance__card, container, false)
        init(root)
        // Taking reference of the firebase
        ref = firebaseDatabase.getReference("BIMS")
        ref2 = firebaseDatabase.getReference("BIMS")
        return root
    }

    private fun init(root: View) {

        noData = root.findViewById(R.id.noData)
        noDataArrow = root.findViewById(R.id.noDataArrow)
        cardStackView = root.findViewById(R.id.card_stack_view)

        // If stream is null  then no data can be shown
        if(stream == null){
            noData.visibility = View.VISIBLE
            noDataArrow.visibility = View.VISIBLE
            cardStackView.visibility = View.GONE
        }else{
            cardStackView.visibility = View.VISIBLE
            noData.visibility = View.GONE
            noDataArrow.visibility = View.GONE
        }
        // access saved user email
        val sh = requireActivity().getSharedPreferences(getString(R.string.user_id), Context.MODE_PRIVATE)
        email = sh.getString(getString(R.string.id), "")!!
        try {
        db = FirebaseDatabase.getInstance().getReference("BIMS").child(email).child(stream!!)
            .child(sem!!).child("nameId")
        prepareData()
        }catch (e : Exception){
            Log.e("AttendanceFragment","Exception call "+e.message)
        }


        // handle card swiping
        manager = CardStackLayoutManager(context, object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {
                Log.d("TAG", "onCardDragging: d=" + direction.name + " ratio=" + ratio)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onCardSwiped(direction: Direction) {

                // To know when all items have been swiped:
                if(total == 0){
                    total = adapter!!.itemCount
                }
                total--

                Log.e("TOTAL", "Adapter: "+ adapter!!.itemCount  + " total is "+total)

                // Accessing the current student
                val current = list[roll]
                ++roll
                val date  = LocalDate.now().toString()

                Log.d("TAG", "onCardSwiped: p=" + manager!!.topPosition + " d=" + direction)
                if (direction == Direction.Right) {
                    var p = current.present
                    p= p!! +1

                    ref.child(email).child(stream!!).child("semID").child(sem!!)
                        .child("nameId").child(current.roll.toString()).child("present")
                        .setValue(p)

                    ref.child(email).child(stream).child("semID").child(sem)
                        .child("nameId").child(current.roll.toString()).child("presentID").child(date).
                        child("date").setValue(date)

                    ref.child(email).child(stream).child("semID").child(sem)
                        .child("nameId").child(current.roll.toString()).child("presentID").child(date).
                        child("att").setValue("Present")

                    Toast.makeText(getContext(), "Preset roll no "+current.roll, Toast.LENGTH_SHORT).show()
                }

                if (direction == Direction.Left) {
                    ref.child(email).child(stream!!).child("semID").child(sem!!)
                        .child("nameId").child(current.roll.toString()).child("presentID").child(date).
                        child("date").setValue(date)
                    ref.child(email).child(stream).child("semID").child(sem)
                        .child("nameId").child(current.roll.toString()).child("presentID").child(date).
                        child("att").setValue("Absent")
                    Toast.makeText(getContext(), "Absent roll no "+current.roll, Toast.LENGTH_SHORT).show()
                }

                // Paginating
//                if (manager!!.topPosition == adapter!!.itemCount - 5) {
//                    paginate()
//                }

                // all cards have been swiped : Show empty view
                if(total == 0){
                    list.clear()
                    setAdapter()
                    noData.visibility = View.VISIBLE
                    noDataArrow.visibility = View.VISIBLE
                    cardStackView.visibility = View.GONE
                }
            }

            override fun onCardRewound() {
                Log.d("TAG", "onCardRewound: " + manager!!.topPosition)
            }

            override fun onCardCanceled() {
                Log.d("TAG", "onCardRewound: " + manager!!.topPosition)
            }

            override fun onCardAppeared(view: View, position: Int) {
                val tv = view.findViewById<TextView>(R.id.item_name)
                Log.d("TAG", "onCardAppeared: " + position + ", nama: " + tv.text)
            }

            override fun onCardDisappeared(view: View, position: Int) {
                val tv = view.findViewById<TextView>(R.id.item_name)
                Log.d("TAG", "onCardAppeared: " + position + ", nama: " + tv.text)
            }

        })

        // some properties for swiping
        manager!!.setVisibleCount(3)
        manager!!.setTranslationInterval(8.0f)
        manager!!.setScaleInterval(0.95f)
        manager!!.setSwipeThreshold(0.3f)
        manager!!.setMaxDegree(20.0f)
        manager!!.setDirections(Direction.HORIZONTAL)
        manager!!.setCanScrollHorizontal(true)
        manager!!.setSwipeableMethod(SwipeableMethod.Manual)
        manager!!.setOverlayInterpolator(LinearInterpolator())
    }

    private fun setAdapter() {
        try {
            adapter = CardStackAdapter(list, requireContext())
            cardStackView.layoutManager = manager
            cardStackView.adapter = adapter
            cardStackView.itemAnimator = DefaultItemAnimator()
        }catch (e : Exception){
            Log.e("TAGTAG",e.message.toString())
        }
    }

    private fun prepareData() {
        ref2 = firebaseDatabase.getReference("BIMS").child(email)
            .child(stream.toString()).child("semID").child(sem.toString())
            .child("nameId")

        ref2.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                if(snapshot.exists()){
                    for ( snap in snapshot.children) {

                        Log.e("neel","call loop")
                        val data = snap.getValue(CardModel::class.java)
                        list.add(data!!)

                    }
                    setAdapter()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Cannot load data", Toast.LENGTH_SHORT).show()
            }

        })
    }

//    private fun paginate() {
//        val old = adapter!!.items
//        val latest: List<CardModel> = ArrayList(list)
//        val callback = CardStackCallback(old, latest)
//        val hasil = DiffUtil.calculateDiff(callback)
//        adapter!!.items = latest
//
//        hasil.dispatchUpdatesTo(adapter!!)
//    }
//
}