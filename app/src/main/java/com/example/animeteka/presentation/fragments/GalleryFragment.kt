package com.example.animeteka.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.animeteka.R
import com.example.animeteka.businesslogic.entities.TitleEntity
import com.example.animeteka.data.Application
import com.example.animeteka.databinding.FragmentGalleryBinding
import com.example.animeteka.presentation.viewmodels.GalleryViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var gridRecyclerView: RecyclerView
    private lateinit var gridRecyclerViewAdapter: GridTitlesAdapter
    private lateinit var gridRecyclerViewLayoutManager: GridLayoutManager
    private lateinit var searchBar: SearchView
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
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridRecyclerView = view.findViewById(R.id.grid_titles_list)
        searchBar = view.findViewById(R.id.search_bar_gallery)

        galleryViewModel.getTitles().observe(viewLifecycleOwner) {
            gridRecyclerViewLayoutManager = GridLayoutManager(view.context, 2)
            gridRecyclerViewAdapter =
                GridTitlesAdapter(it,
                    object : OnGridRecycleViewListener {
                        override fun onViewClick(titleId: Int) {
                            val bundle = Bundle()
                            bundle.putInt("titleId", titleId)
                            view.findNavController().navigate(R.id.action_nav_gallery_to_elementFragment, bundle)
                        }
                    })
            gridRecyclerView.layoutManager = gridRecyclerViewLayoutManager
            gridRecyclerView.adapter = gridRecyclerViewAdapter

            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    gridRecyclerViewAdapter.filter.filter(query)
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    gridRecyclerViewAdapter.filter.filter(query)
                    return false
                }
            })
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // когда движется
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedTitle: TitleEntity = gridRecyclerViewAdapter.getTitlesList()[viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition

                gridRecyclerViewAdapter.getTitlesList().removeAt(position)

                gridRecyclerViewAdapter.notifyItemRemoved(position)

                galleryViewModel.viewModelScope.launch {
                    galleryViewModel.deleteTitle(deletedTitle)
                }

                Snackbar.make(gridRecyclerView, resources.getString(R.string.deleted_title) + ' ' + deletedTitle.canonicalTitle, Snackbar.LENGTH_LONG)
                    .setAction(
                        resources.getString(R.string.cancel_button),
                        View.OnClickListener {
                            gridRecyclerViewAdapter.getTitlesList().add(position, deletedTitle)
                            gridRecyclerViewAdapter.notifyItemInserted(position)
                            galleryViewModel.viewModelScope.launch {
                                galleryViewModel.saveTitle(deletedTitle)
                            }
                        }).show()
            }
        }).attachToRecyclerView(gridRecyclerView)
    }

    override fun onStop() {
        super.onStop()
        searchBar.setQuery("", false);
        searchBar.clearFocus()
        searchBar.isIconified = true;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        galleryViewModel.getTitles().removeObservers(viewLifecycleOwner)
        _binding = null
    }

    class GridTitlesAdapter(private val titlesList: List<TitleEntity>, private val listener: OnGridRecycleViewListener): RecyclerView.Adapter<GridTitlesAdapter.GridTitlesViewHolder>(), Filterable{

        private var titlesListSearch: MutableList<TitleEntity> = this.titlesList as MutableList<TitleEntity>

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

        override fun getFilter(): Filter {
            return object : Filter(){
                override fun performFiltering(searchChars: CharSequence?): FilterResults {
                    val searchString = searchChars.toString()
                    if(searchString.isEmpty()){
                        titlesListSearch = titlesList as MutableList<TitleEntity>
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
                    titlesListSearch = filterResult!!.values as MutableList<TitleEntity>
                    notifyDataSetChanged()
                }
            }
        }

        fun getTitlesList(): MutableList<TitleEntity>{
            return titlesListSearch
        }
    }

    interface OnGridRecycleViewListener {
        fun onViewClick(titleId: Int)
    }
}