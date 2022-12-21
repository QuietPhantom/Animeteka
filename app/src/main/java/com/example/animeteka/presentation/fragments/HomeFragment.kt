package com.example.animeteka.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animeteka.R
import com.example.animeteka.databinding.FragmentHomeBinding
import com.example.animeteka.retrofit.entities.RetrofitApiCallbackEntities
import com.example.animeteka.presentation.viewmodels.HomeViewModel
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: TitlesAdapter
    private lateinit var dialog: AlertDialog
    private lateinit var updateButton: FloatingActionButton
    private var random: Int = -1
    private var state: Parcelable? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        if(savedInstanceState != null){
            random = savedInstanceState.getInt("random")
            state = savedInstanceState.getParcelable("stateHome")
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(random == -1){
            random = (0..10000).random()
        }

        recyclerView = view.findViewById(R.id.titles_list)
        updateButton = view.findViewById(R.id.updateTitleList)
        homeViewModel.initApi()
        dialog = SpotsDialog.Builder().setCancelable(true).setContext(context).build()
        homeViewModel.getNewAnimeTitlesList(random)
        homeViewModel.livedata.observe(viewLifecycleOwner){
            dialog.show()
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerViewAdapter = TitlesAdapter(it,
                object : OnRecycleViewListener {
                    override fun onViewClick(titleId: Int) {
                        val bundle = Bundle()
                        bundle.putInt("titleId", titleId)
                        view.findNavController().navigate(R.id.action_nav_home_to_elementFragment, bundle)
                    }
                })
            recyclerView.adapter = recyclerViewAdapter
            if(state != null){
                recyclerView.layoutManager?.onRestoreInstanceState(state)
            }
            dialog.dismiss()
        }

        updateButton.setOnClickListener{
            dialog.show()
            random = (0..10000).random()
            homeViewModel.getNewAnimeTitlesList(random)
            homeViewModel.livedata.observe(viewLifecycleOwner){
                recyclerViewAdapter.setRetrofitItems(it)
                dialog.dismiss()
            }
        }
    }

    class TitlesAdapter(private val titles: RetrofitApiCallbackEntities, private val listener: OnRecycleViewListener): RecyclerView.Adapter<TitlesAdapter.TitlesViewHolder> (){

        private var adapterTitlesList: RetrofitApiCallbackEntities = this.titles

        class TitlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.name)
            val description: TextView = itemView.findViewById(R.id.description)
            val subDescription: TextView = itemView.findViewById(R.id.subDescription)
            val image: ImageView = itemView.findViewById(R.id.image)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitlesViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)

            return TitlesViewHolder(view)
        }

        override fun onBindViewHolder(holder: TitlesViewHolder, position: Int) {
            holder.title.text = adapterTitlesList.data[position].attributes.canonicalTitle
            holder.description.text = adapterTitlesList.data[position].attributes.description
            holder.subDescription.text = adapterTitlesList.data[position].attributes.startDate.substringBefore('-') + " | " + adapterTitlesList.data[position].attributes.averageRating + " | " + adapterTitlesList.data[position].attributes.status + " | " + adapterTitlesList.data[position].attributes.subtype
            Picasso.get().load(adapterTitlesList.data[position].attributes.posterImage.small).into(holder.image)
            holder.itemView.setOnClickListener {
                listener.onViewClick(adapterTitlesList.data[position].id)
            }
        }

        override fun getItemCount(): Int {
            return adapterTitlesList.data.size
        }

        fun setRetrofitItems(titlesList: RetrofitApiCallbackEntities){
            adapterTitlesList = titlesList
            notifyDataSetChanged()
        }
    }

    interface OnRecycleViewListener {
        fun onViewClick(titleId: Int)
    }

    override fun onPause() {
        super.onPause()
        state = recyclerView.layoutManager?.onSaveInstanceState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("random", random)
        outState.putParcelable("stateHome", state)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}