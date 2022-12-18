package com.example.animeteka.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animeteka.R
import com.example.animeteka.databinding.FragmentSlideshowBinding
import com.example.animeteka.presentation.viewmodels.SlideshowViewModel
import com.example.animeteka.retrofit.entities.RetrofitApiCallbackEntities
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    private lateinit var slideshowViewModel: SlideshowViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: SearchView
    private lateinit var dialog: AlertDialog
    private var querySearchBar: String = ""
    private var state: Parcelable? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
        if(savedInstanceState != null){
            querySearchBar = savedInstanceState.getString("querySearch")!!
            state = savedInstanceState.getParcelable("stateSearch")
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.search_titles_list)
        searchBar = view.findViewById(R.id.search_bar)

        slideshowViewModel.initApi()
        dialog = SpotsDialog.Builder().setCancelable(true).setContext(context).build()

        if(querySearchBar != ""){
            dialog.show()
            slideshowViewModel.getNewAnimeTitlesListByKeyWords(querySearchBar)
            slideshowViewModel.livedata.observe(viewLifecycleOwner){
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = TitlesAdapter(it,
                    object : OnRecycleViewListener {
                        override fun onViewClick(titleId: Int) {
                            val bundle = Bundle()
                            bundle.putInt("titleId", titleId)
                            view.findNavController()
                                .navigate(R.id.action_nav_slideshow_to_elementFragment, bundle)
                        }
                    })
                if(state != null){
                    recyclerView.layoutManager?.onRestoreInstanceState(state)
                }
                dialog.dismiss()
            }
        }

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    dialog.show()
                    slideshowViewModel.getNewAnimeTitlesListByKeyWords(query)
                    slideshowViewModel.livedata.observe(viewLifecycleOwner){
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerView.adapter = TitlesAdapter(it,
                            object : OnRecycleViewListener {
                                override fun onViewClick(titleId: Int) {
                                    val bundle = Bundle()
                                    bundle.putInt("titleId", titleId)
                                    view.findNavController()
                                        .navigate(R.id.action_nav_slideshow_to_elementFragment, bundle)
                                }
                            })
                        dialog.dismiss()
                        querySearchBar = query
                    }
                } else {
                    Toast.makeText(context, resources.getString(R.string.search_bar_is_empty), Toast.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })
    }

    class TitlesAdapter(private val titles: RetrofitApiCallbackEntities, private val listener: OnRecycleViewListener): RecyclerView.Adapter<TitlesAdapter.TitlesViewHolder> (){

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
            holder.title.text = titles.data[position].attributes.canonicalTitle
            holder.description.text = titles.data[position].attributes.description
            holder.subDescription.text = titles.data[position].attributes.startDate.substringBefore('-') + " | " + titles.data[position].attributes.averageRating + " | " + titles.data[position].attributes.status + " | " + titles.data[position].attributes.subtype
            Picasso.get().load(titles.data[position].attributes.posterImage.small).into(holder.image)
            holder.itemView.setOnClickListener {
                listener.onViewClick(titles.data[position].id)
            }
        }

        override fun getItemCount(): Int {
            return titles.data.size
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
        outState.putString("querySearch", querySearchBar)
        outState.putParcelable("stateSearch", state)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}