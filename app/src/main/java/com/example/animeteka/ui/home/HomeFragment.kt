package com.example.animeteka.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animeteka.R
import com.example.animeteka.databinding.FragmentHomeBinding
import com.example.animeteka.entities.RetrofitApiCallbackEntities
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var dialog: AlertDialog

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.initApi()
        recyclerView = view.findViewById(R.id.titles_list)
        dialog = SpotsDialog.Builder().setCancelable(true).setContext(context).build()
        dialog.show()
        homeViewModel.getNewAnimeTitlesList()
        homeViewModel.livedata.observe(viewLifecycleOwner){
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = TitlesAdapter(it,
                object : OnRecycleViewListener {
                    override fun onViewClick(titleId: Int) {
                        val bundle = Bundle()
                        bundle.putInt("titleId", titleId)
                        view.findNavController().navigate(R.id.action_nav_home_to_elementFragment, bundle)
                    }
                })
        }
        dialog.dismiss()

        view.findViewById<FloatingActionButton>(R.id.updateTitleList).setOnClickListener{
            dialog.show()
            homeViewModel.getNewAnimeTitlesList()
            homeViewModel.livedata.observe(viewLifecycleOwner){
                recyclerView.adapter = TitlesAdapter(it,
                    object : OnRecycleViewListener {
                        override fun onViewClick(titleId: Int) {
                            val bundle = Bundle()
                            bundle.putInt("titleId", titleId)
                            view.findNavController().navigate(R.id.action_nav_home_to_elementFragment, bundle)
                        }
                    })
            }
            dialog.dismiss()
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}