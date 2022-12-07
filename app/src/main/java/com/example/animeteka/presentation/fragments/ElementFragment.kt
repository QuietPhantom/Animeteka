package com.example.animeteka.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.animeteka.R
import com.example.animeteka.businesslogic.entities.TitleEntity
import com.example.animeteka.data.Application
import com.example.animeteka.presentation.viewmodels.ElementViewModel
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.launch
import android.widget.Toast


class ElementFragment : Fragment() {

    companion object {
        fun newInstance() = ElementFragment()
    }

    private lateinit var dialog: AlertDialog
    private lateinit var elementViewModel: ElementViewModel
    private lateinit var titleInf: TitleEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        elementViewModel = ViewModelProvider(this).get(ElementViewModel::class.java)
        return inflater.inflate(R.layout.fragment_element, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        elementViewModel.initApi(requireActivity().application as Application)
        dialog = SpotsDialog.Builder().setCancelable(true).setContext(context).build()
        dialog.show()
        elementViewModel.getAnimeTitleById(requireArguments().getInt("titleId"))
        elementViewModel.livedata.observe(viewLifecycleOwner){
            Picasso.get().load(it.data.attributes.posterImage.original).into(view.findViewById<ImageView>(R.id.titleImage))
            view.findViewById<TextView>(R.id.TitleName).text = it.data.attributes.canonicalTitle
            view.findViewById<TextView>(R.id.enTitleName).text = it.data.attributes.titles.en
            view.findViewById<TextView>(R.id.Dates).text = it.data.attributes.startDate + " - " + it.data.attributes.endDate
            view.findViewById<TextView>(R.id.Rating).text = it.data.attributes.averageRating + " / " + it.data.attributes.userCount
            view.findViewById<TextView>(R.id.ageRating).text = it.data.attributes.ageRating + " (" +  it.data.attributes.ageRatingGuide + ")"
            view.findViewById<TextView>(R.id.Count).text = it.data.attributes.episodeCount + " / " + it.data.attributes.episodeLength + " / " + it.data.attributes.totalLength
            view.findViewById<TextView>(R.id.StatusAndType).text = it.data.attributes.subtype + " | " + it.data.attributes.status
            view.findViewById<TextView>(R.id.fullDescription).text = it.data.attributes.description
            view.findViewById<TextView>(R.id.fullDescription).movementMethod =
                ScrollingMovementMethod()
            try{
                titleInf = TitleEntity(requireArguments().getInt("titleId"),it.data.attributes.canonicalTitle,it.data.attributes.titles.en,it.data.attributes.description,it.data.attributes.posterImage.original,it.data.attributes.averageRating,it.data.attributes.userCount,it.data.attributes.startDate,it.data.attributes.endDate,it.data.attributes.ageRating,it.data.attributes.ageRatingGuide,it.data.attributes.subtype,it.data.attributes.status,it.data.attributes.episodeCount,it.data.attributes.episodeLength,it.data.attributes.totalLength)
            }catch (e: NullPointerException){
                dialog.dismiss()
                view.findNavController().popBackStack()
            }

        }
        dialog.dismiss()

        view.findViewById<Button>(R.id.addTitle).setOnClickListener {
            elementViewModel.viewModelScope.launch {
                elementViewModel.saveTitle(titleInf)
                Toast.makeText(context, resources.getString(R.string.title_added), Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.deleteTitle).setOnClickListener {
            elementViewModel.viewModelScope.launch {
                elementViewModel.deleteTitle(titleInf)
                Toast.makeText(context, resources.getString(R.string.title_deleted), Toast.LENGTH_SHORT).show()
            }
        }
    }
}