package com.example.animeteka.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animeteka.R
import com.example.animeteka.common.Common
import com.example.animeteka.databinding.FragmentHomeBinding
import com.example.animeteka.entities.RetrofitApiCallbackEntity
import com.example.animeteka.retrofit.RetrofitServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var adapter: TitlesAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var retrofitService: RetrofitServices
    private lateinit var dialog: AlertDialog

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrofitService = Common.retrofitService

        recyclerView = view.findViewById(R.id.titles_list)
        recyclerView.layoutManager = LinearLayoutManager(context)

        dialog = SpotsDialog.Builder().setCancelable(true).setContext(context).build()

        getNewAnimeTitlesList()

        view.findViewById<FloatingActionButton>(R.id.updateTitleList).setOnClickListener{
            getNewAnimeTitlesList()
        }
    }

    private fun getNewAnimeTitlesList() {
        dialog.show()
        retrofitService.getAnimeTitlesList((0..10000).random().toString()).enqueue(object: Callback<RetrofitApiCallbackEntity> {

            override fun onFailure(call: Call<RetrofitApiCallbackEntity>, t: Throwable) {
                t.printStackTrace()
                dialog.dismiss()
            }

            override fun onResponse(call: Call<RetrofitApiCallbackEntity>, response: Response<RetrofitApiCallbackEntity>) {

                adapter = TitlesAdapter(response.body()!!)
                recyclerView.adapter = adapter

                dialog.dismiss()
            }
        })
    }

    class TitlesAdapter(private val titles: RetrofitApiCallbackEntity): RecyclerView.Adapter<TitlesAdapter.TitlesViewHolder> (){

        class TitlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.name)
            val description: TextView = itemView.findViewById(R.id.description)
            val image: ImageView = itemView.findViewById(R.id.image)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitlesViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)

            return TitlesViewHolder(view)
        }

        override fun onBindViewHolder(holder: TitlesViewHolder, position: Int) {
            holder.title.text = titles.data[position].attributes.canonicalTitle
            holder.description.text = titles.data[position].attributes.description
            Picasso.get().load(titles.data[position].attributes.posterImage.small).into(holder.image)
        }

        override fun getItemCount(): Int {
            return titles.data.size
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}