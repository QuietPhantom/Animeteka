package com.example.animeteka.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animeteka.R
import com.example.animeteka.businesslogic.entities.TitleEntity
import com.example.animeteka.databinding.FragmentGalleryBinding
import com.example.animeteka.presentation.viewmodels.GalleryViewModel
import com.squareup.picasso.Picasso

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var gridRecyclerView: RecyclerView

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridRecyclerView = view.findViewById<RecyclerView>(R.id.grid_titles_list)
        //gridRecyclerView.layoutManager = GridLayoutManager(view.context, 2)
    }

    class GridTitlesAdapter(private val titlesList: List<TitleEntity>): RecyclerView.Adapter<GridTitlesAdapter.GridTitlesViewHolder> (){

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
            holder.titleName.text = titlesList[position].canonicalTitle
            Picasso.get().load(titlesList[position].posterImage).into(holder.titleImage)
        }

        override fun getItemCount(): Int {
            return titlesList.size
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}