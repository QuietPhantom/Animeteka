package com.example.animeteka.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animeteka.R
import com.example.animeteka.businesslogic.entities.TitleEntity
import com.example.animeteka.data.Application
import com.example.animeteka.databinding.FragmentGalleryBinding
import com.example.animeteka.presentation.viewmodels.GalleryViewModel
import com.squareup.picasso.Picasso

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var gridRecyclerView: RecyclerView
    private lateinit var searchBar: SearchView
    private lateinit var gridAdapter: GridTitlesAdapter
    private var querySearchBar: String = ""
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)
        galleryViewModel.init(requireActivity().application as Application)
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        if(savedInstanceState != null){
            querySearchBar = savedInstanceState.getString("queryGallery")!!
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridRecyclerView = view.findViewById(R.id.grid_titles_list)
        searchBar = view.findViewById(R.id.search_bar_gallery)

        galleryViewModel.getTitles().observe(viewLifecycleOwner) {
            gridRecyclerView.layoutManager = GridLayoutManager(view.context, 2)
            gridAdapter =
                GridTitlesAdapter(it,
                    object : GridTitlesAdapter.OnGridRecycleViewListener {
                        override fun onViewClick(titleId: Int) {
                            val bundle = Bundle()
                            bundle.putInt("titleId", titleId)
                            view.findNavController().navigate(R.id.action_nav_gallery_to_elementFragment, bundle)
                        }
                    })

            gridRecyclerView.adapter = gridAdapter

            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    gridAdapter!!.filter.filter(query)
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    gridAdapter!!.filter.filter(query)
                    return false
                }
            })

            if(querySearchBar != ""){
                searchBar.setQuery(querySearchBar, true)
                searchBar.clearFocus()
            }
        }
    }

    class GridTitlesAdapter(private val titlesList: List<TitleEntity>, private val listener: OnGridRecycleViewListener): RecyclerView.Adapter<GridTitlesAdapter.GridTitlesViewHolder>(), Filterable{

        private var titlesListSearch: List<TitleEntity> = this.titlesList

        class GridTitlesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val titleName: TextView = itemView.findViewById(R.id.titleNameCardView)
            val titleImage: ImageView = itemView.findViewById(R.id.titleImageCardView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridTitlesViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.grid_item,
                parent, false)

            return GridTitlesViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: GridTitlesViewHolder, position: Int) {
            holder.titleName.text = titlesListSearch[position].canonicalTitle
            Picasso.get().load(titlesListSearch[position].posterImage).into(holder.titleImage)
            holder.itemView.setOnClickListener {
                listener.onViewClick(titlesListSearch[position].id)
            }
        }

        override fun getItemCount(): Int {
            return titlesListSearch.size
        }

        interface OnGridRecycleViewListener {
            fun onViewClick(titleId: Int)
        }

        override fun getFilter(): Filter {
            return object : Filter(){
                override fun performFiltering(searchChars: CharSequence?): FilterResults {
                    val searchString = searchChars.toString()
                    if(searchString.isEmpty()){
                        titlesListSearch = titlesList
                    } else {
                        val resultList: MutableList<TitleEntity> = mutableListOf()
                        for (row in titlesList){
                            if (row.canonicalTitle.lowercase().contains(searchString.lowercase())) resultList.add(row)
                        }
                        titlesListSearch = resultList
                    }
                    val filterResults = Filter.FilterResults()
                    filterResults.values = titlesListSearch
                    return filterResults
                }

                override fun publishResults(charSequence: CharSequence?, filterResult: FilterResults?) {
                    titlesListSearch = filterResult!!.values as List<TitleEntity>
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        querySearchBar = searchBar.query.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("queryGallery", querySearchBar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}