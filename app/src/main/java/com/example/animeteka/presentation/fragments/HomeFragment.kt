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
import com.example.animeteka.data.retrofit.entities.RetrofitApiCallbackEntities
import com.example.animeteka.presentation.viewmodels.HomeViewModel
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: TitlesAdapter
    private lateinit var recyclerViewLayoutManager: LinearLayoutManager
    private lateinit var updateButton: FloatingActionButton
    private var random: Int = -1
    private var state: Parcelable? = null
    private lateinit var dialog: AlertDialog
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

        dialog = SpotsDialog.Builder().setCancelable(true).setContext(context).build()

        homeViewModel.initApi()

        recyclerView = view.findViewById(R.id.titles_list)
        updateButton = view.findViewById(R.id.updateTitleList)

        homeViewModel.livedata.observe(viewLifecycleOwner){
            if (recyclerView.adapter == null){
                recyclerViewLayoutManager = LinearLayoutManager(context)
                recyclerViewAdapter = TitlesAdapter(it,
                    object : OnRecycleViewListener {
                        override fun onViewClick(titleId: Int) {
                            val bundle = Bundle()
                            bundle.putInt("titleId", titleId)
                            view.findNavController().navigate(R.id.action_nav_home_to_elementFragment, bundle)
                        }
                    })
                recyclerView.layoutManager = recyclerViewLayoutManager
                recyclerView.adapter = recyclerViewAdapter
                if(state != null) recyclerViewLayoutManager.onRestoreInstanceState(state)
            } else {
                recyclerViewAdapter.setRetrofitData(it)
            }
            dialog.dismiss()
        }

        if(random == -1){
            random = (0..10000).random()
            homeViewModel.getNewAnimeTitlesList(random)
            dialog.show()
        }

        updateButton.setOnClickListener{
            random = (0..10000).random()
            homeViewModel.getNewAnimeTitlesList(random)
            dialog.show()
        }
    }

    class TitlesAdapter(private val titles: RetrofitApiCallbackEntities, private val listener: OnRecycleViewListener): RecyclerView.Adapter<TitlesAdapter.TitlesViewHolder> (){

        private var titlesListAdapter: RetrofitApiCallbackEntities = this.titles

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
            holder.title.text = titlesListAdapter.data[position].attributes.canonicalTitle
            holder.description.text = titlesListAdapter.data[position].attributes.description
            holder.subDescription.text = titlesListAdapter.data[position].attributes.startDate.substringBefore('-') + " | " + titlesListAdapter.data[position].attributes.averageRating + " | " + titlesListAdapter.data[position].attributes.status + " | " + titlesListAdapter.data[position].attributes.subtype
            Picasso.get().load(titlesListAdapter.data[position].attributes.posterImage.small).into(holder.image)
            holder.itemView.setOnClickListener {
                listener.onViewClick(titlesListAdapter.data[position].id)
            }
        }

        override fun getItemCount(): Int {
            return titlesListAdapter.data.size
        }

        fun setRetrofitData(titles: RetrofitApiCallbackEntities){
            titlesListAdapter = titles
            notifyDataSetChanged()
        }
    }

    interface OnRecycleViewListener {
        fun onViewClick(titleId: Int)
    }

    override fun onPause() {
        super.onPause()
        state = recyclerView.layoutManager?.onSaveInstanceState()
        homeViewModel.livedata.removeObservers(viewLifecycleOwner)
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