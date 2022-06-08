package com.example.smartattendance.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.example.smartattendance.CardStackCallback
import com.example.smartattendance.R
import com.example.smartattendance.adapters.CardStackAdapter
import com.example.smartattendance.model.CardModel
import com.google.firebase.database.*
import com.yuyakaido.android.cardstackview.*
import java.text.SimpleDateFormat
import java.time.LocalDate

class Attendance(val stream: String?,val sem: String?) : Fragment() {

    var firebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var ref: DatabaseReference
    lateinit var ref2: DatabaseReference
    lateinit var StreamName:String
    private var manager: CardStackLayoutManager? = null
    private var adapter: CardStackAdapter? = null
    private lateinit var  db : DatabaseReference
    private val list = ArrayList<CardModel>()
    private lateinit var cardStackView : CardStackView
    private var roll = 0
    var total = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_attendance__card, container, false)
        init(root)
        ref = firebaseDatabase.getReference("BIMS")
        ref2 = firebaseDatabase.getReference("BIMS")
        return root
    }

    private fun init(root: View) {

        val sh = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val id = sh.getString("id", "")

        db = FirebaseDatabase.getInstance().getReference("BIMS").child("user_Email").child(stream!!)
            .child(sem!!).child("nameId")
        prepareData()


        cardStackView = root.findViewById<CardStackView>(R.id.card_stack_view)
        manager = CardStackLayoutManager(context, object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {
                Log.d(TAG, "onCardDragging: d=" + direction.name + " ratio=" + ratio)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onCardSwiped(direction: Direction) {
                val curent = list[roll]
                ++roll
                var dateb=LocalDate.now().toString()
                Log.d(TAG, "onCardSwiped: p=" + manager!!.topPosition + " d=" + direction)
                if (direction == Direction.Right) {
                    var p = curent.present?.toInt()
                    p= p!! +1
                    ref.child("user_Email").child(stream).child("semID").child(sem)
                        .child("nameId").child(curent.roll.toString()).child("present")
                        .setValue(p)
                    ref.child("user_Email").child(stream).child("semID").child(sem)
                        .child("nameId").child(curent.roll.toString()).child("PresentID").child(dateb)
                        .child("date").setValue(dateb)
                    ref.child("user_Email").child(stream).child("semID").child(sem)
                        .child("nameId").child(curent.roll.toString()).child("PresentID").child(dateb)
                        .child("AOrP").setValue("Present")

                    Toast.makeText(getContext(), "Preset roll no "+curent.roll, Toast.LENGTH_SHORT).show()
                }
                if (direction == Direction.Top) {

//                    Toast.makeText(getContext(), "Direction Top", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Left) {
                    ref.child("user_Email").child(stream).child("semID").child(sem)
                        .child("nameId").child(curent.roll.toString()).child("PresentID").child(dateb)
                        .child("date").setValue(dateb)
                    ref.child("user_Email").child(stream).child("semID").child(sem)
                        .child("nameId").child(curent.roll.toString()).child("PresentID").child(dateb)
                        .child("AOrP").setValue("Absent")
                    Toast.makeText(getContext(), "Absent roll no "+curent.roll, Toast.LENGTH_SHORT).show()
                }
                if (direction == Direction.Bottom) {
//                    Toast.makeText(getContext(), "Direction Bottom", Toast.LENGTH_SHORT).show();
                }

                // Paginating
                if (manager!!.topPosition == adapter!!.itemCount - 5) {
                    paginate()
                }
            }

            override fun onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager!!.topPosition)
            }

            override fun onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager!!.topPosition)
            }

            override fun onCardAppeared(view: View, position: Int) {
                val tv = view.findViewById<TextView>(R.id.item_name)
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.text)
            }

            override fun onCardDisappeared(view: View, position: Int) {
                val tv = view.findViewById<TextView>(R.id.item_name)
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.text)
            }
        })
        manager!!.setStackFrom(StackFrom.None)
        manager!!.setVisibleCount(3)
        manager!!.setTranslationInterval(8.0f)
        manager!!.setScaleInterval(0.95f)
        manager!!.setSwipeThreshold(0.3f)
        manager!!.setMaxDegree(20.0f)
        manager!!.setDirections(Direction.FREEDOM)
        manager!!.setCanScrollHorizontal(true)
        manager!!.setSwipeableMethod(SwipeableMethod.Manual)
        manager!!.setOverlayInterpolator(LinearInterpolator())

    }

    private fun prepareData() {
        ref2 = firebaseDatabase.getReference("BIMS").child("user_Email")
            .child(stream.toString()).child("semID").child(sem.toString())
            .child("nameId")
        ref2.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                if(snapshot.exists()){
                    for ( snap in snapshot.children) {
                        val data = snap.getValue(CardModel::class.java)
                        list.add(data!!)
                        ++total
                    }
                    adapter = CardStackAdapter(list, requireContext())
                    cardStackView.layoutManager = manager
                    cardStackView.adapter = adapter
                    cardStackView.itemAnimator = DefaultItemAnimator()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Cannot load data", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun paginate() {
        val old = adapter!!.items
        val latest: List<CardModel> = ArrayList(addList())
        val callback = CardStackCallback(old, latest)
        val hasil = DiffUtil.calculateDiff(callback)
        adapter!!.items = latest
        hasil.dispatchUpdatesTo(adapter!!)
    }

    private fun addList(): List<CardModel> {



        val items: MutableList<CardModel> = ArrayList()



        return items
    }

    companion object {
        private val TAG = AddDbFragment::class.java.simpleName
    }
}