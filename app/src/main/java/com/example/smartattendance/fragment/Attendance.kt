package com.example.smartattendance.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.example.smartattendance.CardStackCallback
import com.example.smartattendance.R
import com.example.smartattendance.adapters.CardStackAdapter
import com.example.smartattendance.model.CardModel
import com.google.firebase.database.*
import com.yuyakaido.android.cardstackview.*

class Attendance(val stream: String?,val sem: String?) : Fragment() {

    private var manager: CardStackLayoutManager? = null
    private var adapter: CardStackAdapter? = null
    private lateinit var  db : DatabaseReference
    private val list = ArrayList<CardModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_attendance__card, container, false)
        init(root)
        return root
    }

    private fun init(root: View) {

        val sh = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val id = sh.getString("id", "")

        db = FirebaseDatabase.getInstance().getReference("BIMS").child("user_Email").child(stream!!).child(sem!!)
        prepareData()


        val cardStackView = root.findViewById<CardStackView>(R.id.card_stack_view)
        manager = CardStackLayoutManager(context, object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {
                Log.d(TAG, "onCardDragging: d=" + direction.name + " ratio=" + ratio)
            }

            override fun onCardSwiped(direction: Direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager!!.topPosition + " d=" + direction)
                if (direction == Direction.Right) {
//                    Toast.makeText(getContext(), "Direction Right", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Top) {
//                    Toast.makeText(getContext(), "Direction Top", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Left) {
//                    Toast.makeText(getContext(), "Direction Left", Toast.LENGTH_SHORT).show();
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
        adapter = CardStackAdapter(addList(), requireContext())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator()
    }

    private fun prepareData() {

        db.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                if(snapshot.exists()){
                    for ( snap in snapshot.children)
                        Log.e("UserDetails", snap.toString())
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
        items.add(
            CardModel(
                "Devil Chandra",
                1,
                "Bca-3rd",
                "data:image/jpeg;base64,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"
            )
        )
        items.add(
            CardModel(
                "Aritri Bose",
                2,
                "Bca-3rd",
                "https://mcdn.wallpapersafari.com/medium/84/90/wjp5FR.jpg"
            )
        )
        items.add(
            CardModel(
                "Bratabitan Banerjee",
                3,
                "BCA-3rd",
                "https://naniwallpaper.com/files/wallpapers/levi-ackerman/3-LEVI%20ACKERMAN-1080x1920.jpg"
            )
        )
        items.add(
            CardModel(
                "Dipanjon Saha",
                4,
                "BCA-3rd",
                "http://m.gettywallpapers.com/wp-content/uploads/2020/04/Iron-Man-4k-Background-709x1536.jpg"
            )
        )
        items.add(
            CardModel(
                "Chandra Sekhar Bala",
                5,
                "Bca-3rd",
                "http://m.gettywallpapers.com/wp-content/uploads/2020/05/Captain-America-Wallpaper-downlod-864x1536.jpg"
            )
        )
        items.add(
            CardModel(
                "Sneha Mukherjee",
                6,
                "BCA-3rd",
                "https://wallpapercave.com/dwp2x/wp2752338.jpg"
            )
        )
        items.add(
            CardModel(
                "Ramen Roy",
                7,
                "BCA-3rd",
                "http://m.gettywallpapers.com/wp-content/uploads/2020/01/Batman-Wallpaper-For-Mobile.jpeg"
            )
        )
        items.add(
            CardModel(
                "Mohim Khan",
                8,
                "Bca-3rd",
                "https://www.nawpic.com/media/2020/goku-4k-nawpic-14-500x900.jpg"
            )
        )
        items.add(
            CardModel(
                "Tanisha Parvin",
                9,
                "Bca-3rd",
                "https://mcdn.wallpapersafari.com/medium/84/90/wjp5FR.jpg"
            )
        )
        items.add(
            CardModel(
                "Modok Roy",
                10,
                "BCA-3rd",
                "https://naniwallpaper.com/files/wallpapers/levi-ackerman/3-LEVI%20ACKERMAN-1080x1920.jpg"
            )
        )
        items.add(
            CardModel(
                "Arindam Gandu",
                11,
                "BCA-3rd",
                "http://m.gettywallpapers.com/wp-content/uploads/2020/04/Iron-Man-4k-Background-709x1536.jpg"
            )
        )
        items.add(
            CardModel(
                "Rames Rana",
                12,
                "Bca-3rd",
                "http://m.gettywallpapers.com/wp-content/uploads/2020/05/Captain-America-Wallpaper-downlod-864x1536.jpg"
            )
        )
        items.add(
            CardModel(
                "Papa ki pari",
                13,
                "BCA-3rd",
                "https://wallpapercave.com/dwp2x/wp2752338.jpg"
            )
        )
        items.add(
            CardModel(
                "Backbencher bholu",
                14,
                "BCA-3rd",
                "https://cdnb.artstation.com/p/assets/images/images/032/398/397/medium/rui-penedos-thor-final-01.jpg"
            )
        )
        return items
    }

    companion object {
        private val TAG = AddDbFragment::class.java.simpleName
    }
}