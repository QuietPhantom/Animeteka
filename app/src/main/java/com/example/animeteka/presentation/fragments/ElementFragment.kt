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
    private var titleId: Int = 1
    private lateinit var TitleName: TextView
    private lateinit var enTitleName: TextView
    private lateinit var Dates: TextView
    private lateinit var Rating: TextView
    private lateinit var AgeRating: TextView
    private lateinit var Count: TextView
    private lateinit var StatusAndType: TextView
    private lateinit var FullDescription: TextView
    private lateinit var TitleImage: ImageView
    private lateinit var AddTitle: Button
    private lateinit var DeleteTitle: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        elementViewModel = ViewModelProvider(this).get(ElementViewModel::class.java)
        titleId = requireArguments().getInt("titleId")
        return inflater.inflate(R.layout.fragment_element, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TitleName = view.findViewById(R.id.TitleName)
        enTitleName = view.findViewById(R.id.enTitleName)
        Dates = view.findViewById(R.id.Dates)
        Rating = view.findViewById(R.id.Rating)
        AgeRating = view.findViewById(R.id.ageRating)
        Count = view.findViewById(R.id.Count)
        StatusAndType = view.findViewById(R.id.StatusAndType)
        FullDescription = view.findViewById(R.id.fullDescription)
        FullDescription.movementMethod = ScrollingMovementMethod()
        TitleImage = view.findViewById(R.id.titleImage)
        AddTitle = view.findViewById(R.id.addTitle)
        DeleteTitle = view.findViewById(R.id.deleteTitle)

        elementViewModel.initApi(requireActivity().application as Application)
        dialog = SpotsDialog.Builder().setCancelable(true).setContext(context).build()
        dialog.show()
        
        elementViewModel.getCountTitleById(titleId).observe(viewLifecycleOwner){
            if(it == 1){
                elementViewModel.getTitleById(titleId).observe(viewLifecycleOwner){ it ->
                    Picasso.get().load(it.posterImage).into(TitleImage)
                    TitleName.text = it.canonicalTitle
                    enTitleName.text = it.enTitle
                    Dates.text = it.startDate + " - " + it.endDate
                    Rating.text = it.averageRating + " / " + it.userCount
                    AgeRating.text = it.ageRating + " (" +  it.ageRatingGuide + ")"
                    Count.text = it.episodeCount + " / " + it.episodeLength + " / " + it.totalLength
                    StatusAndType.text = it.subtype + " | " + it.status
                    FullDescription.text = it.description
                    titleInf = it
                }
                AddTitle.isEnabled = false
            } else {
                elementViewModel.getAnimeTitleById(titleId)
                elementViewModel.livedata.observe(viewLifecycleOwner){ it ->
                    Picasso.get().load(it.data.attributes.posterImage.original).into(TitleImage)
                    TitleName.text = it.data.attributes.canonicalTitle
                    enTitleName.text = it.data.attributes.titles.en
                    Dates.text = it.data.attributes.startDate + " - " + it.data.attributes.endDate
                    Rating.text = it.data.attributes.averageRating + " / " + it.data.attributes.userCount
                    AgeRating.text = it.data.attributes.ageRating + " (" +  it.data.attributes.ageRatingGuide + ")"
                    Count.text = it.data.attributes.episodeCount + " / " + it.data.attributes.episodeLength + " / " + it.data.attributes.totalLength
                    StatusAndType.text = it.data.attributes.subtype + " | " + it.data.attributes.status
                    FullDescription.text = it.data.attributes.description
                    try{
                        titleInf = TitleEntity(titleId,it.data.attributes.canonicalTitle,it.data.attributes.titles.en,it.data.attributes.description,it.data.attributes.posterImage.original,it.data.attributes.averageRating,it.data.attributes.userCount,it.data.attributes.startDate,it.data.attributes.endDate,it.data.attributes.ageRating,it.data.attributes.ageRatingGuide,it.data.attributes.subtype,it.data.attributes.status,it.data.attributes.episodeCount,it.data.attributes.episodeLength,it.data.attributes.totalLength)
                    }catch (e: NullPointerException){
                        dialog.dismiss()
                        view.findNavController().popBackStack()
                    }
                }
                DeleteTitle.isEnabled = false
            }
        }
        dialog.dismiss()

        AddTitle.setOnClickListener {
            elementViewModel.viewModelScope.launch {
                elementViewModel.saveTitle(titleInf)
                Toast.makeText(context, resources.getString(R.string.title_added), Toast.LENGTH_SHORT).show()
                AddTitle.isEnabled = false
                DeleteTitle.isEnabled = true
            }
        }

        DeleteTitle.setOnClickListener {
            elementViewModel.viewModelScope.launch {
                elementViewModel.deleteTitle(titleInf)
                Toast.makeText(context, resources.getString(R.string.title_deleted), Toast.LENGTH_SHORT).show()
                DeleteTitle.isEnabled = false
                AddTitle.isEnabled = true
            }
        }
    }
}