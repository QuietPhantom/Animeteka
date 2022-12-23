package com.example.animeteka.presentation.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
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
import kotlinx.coroutines.launch
import android.widget.Toast
import com.example.animeteka.presentation.activities.YoutubeActivity
import com.ms.square.android.expandabletextview.ExpandableTextView
import dmax.dialog.SpotsDialog


class ElementFragment : Fragment() {

    companion object {
        fun newInstance() = ElementFragment()
    }

    private lateinit var elementViewModel: ElementViewModel
    private lateinit var titleInf: TitleEntity
    private var titleId: Int = 1
    private var titleYTVideoId: String = ""
    private lateinit var TitleName: TextView
    private lateinit var enTitleName: TextView
    private lateinit var Dates: TextView
    private lateinit var Rating: TextView
    private lateinit var AgeRating: TextView
    private lateinit var Count: TextView
    private lateinit var StatusAndType: TextView
    private lateinit var FullDescription: ExpandableTextView
    private lateinit var TitleImage: ImageView
    private lateinit var AddTitle: Button
    private lateinit var DeleteTitle: Button
    private lateinit var VideoButton: ImageButton
    private lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        elementViewModel = ViewModelProvider(this).get(ElementViewModel::class.java)
        titleId = requireArguments().getInt("titleId")
        return inflater.inflate(R.layout.fragment_element, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = SpotsDialog.Builder().setCancelable(true).setContext(context).build()

        TitleName = view.findViewById(R.id.TitleName)
        enTitleName = view.findViewById(R.id.enTitleName)
        Dates = view.findViewById(R.id.Dates)
        Rating = view.findViewById(R.id.Rating)
        AgeRating = view.findViewById(R.id.ageRating)
        Count = view.findViewById(R.id.Count)
        StatusAndType = view.findViewById(R.id.StatusAndType)
        FullDescription = view.findViewById(R.id.expand_text_view)
        TitleImage = view.findViewById(R.id.titleImage)
        AddTitle = view.findViewById(R.id.addTitle)
        DeleteTitle = view.findViewById(R.id.deleteTitle)
        VideoButton = view.findViewById(R.id.videoButton)

        elementViewModel.initApi(requireActivity().application as Application)
        
        elementViewModel.getCountTitleById(titleId).observe(viewLifecycleOwner){
            if(it == 1){
                elementViewModel.getTitleById(titleId).observe(viewLifecycleOwner){ it ->
                    Picasso.get().load(it.posterImage).into(TitleImage)
                    TitleName.text = it.canonicalTitle
                    enTitleName.text = it.enTitle
                    Dates.text = it.startDate + " - " + it.endDate
                    Rating.text = it.averageRating + ' ' + resources.getString(R.string.rating_title) + " / " + it.userCount + ' ' + resources.getString(R.string.voice_count)
                    AgeRating.text = it.ageRating + " (" +  it.ageRatingGuide + ")"
                    Count.text = it.episodeCount + ' ' + resources.getString(R.string.episode_count) + " / " + it.episodeLength + ' ' + resources.getString(R.string.episode_length) + " / " + it.totalLength + ' ' + resources.getString(R.string.all_length)
                    StatusAndType.text = it.subtype + " | " + it.status
                    FullDescription.text = it.description
                    titleYTVideoId = it.youtubeVideoId
                    titleInf = it
                }
                AddTitle.isEnabled = false
            } else {
                elementViewModel.livedata.observe(viewLifecycleOwner){ it ->
                    Picasso.get().load(it.data.attributes.posterImage.original).into(TitleImage)
                    TitleName.text = it.data.attributes.canonicalTitle
                    enTitleName.text = it.data.attributes.titles.en
                    Dates.text = it.data.attributes.startDate + " - " + it.data.attributes.endDate
                    Rating.text = it.data.attributes.averageRating + ' ' + resources.getString(R.string.rating_title) + " / " + it.data.attributes.userCount + ' ' + resources.getString(R.string.voice_count)
                    AgeRating.text = it.data.attributes.ageRating + " (" +  it.data.attributes.ageRatingGuide + ")"
                    Count.text = it.data.attributes.episodeCount + ' ' + resources.getString(R.string.episode_count) + " / " + it.data.attributes.episodeLength + ' ' + resources.getString(R.string.episode_length) + " / " + it.data.attributes.totalLength + ' ' + resources.getString(R.string.all_length)
                    StatusAndType.text = it.data.attributes.subtype + " | " + it.data.attributes.status
                    FullDescription.text = it.data.attributes.description
                    titleYTVideoId = it.data.attributes.youtubeVideoId
                    try{
                        titleInf = TitleEntity(titleId,it.data.attributes.canonicalTitle,it.data.attributes.titles.en,it.data.attributes.description,it.data.attributes.posterImage.original,it.data.attributes.averageRating,it.data.attributes.userCount,it.data.attributes.startDate,it.data.attributes.endDate,it.data.attributes.ageRating,it.data.attributes.ageRatingGuide,it.data.attributes.subtype,it.data.attributes.status,it.data.attributes.episodeCount,it.data.attributes.episodeLength,it.data.attributes.totalLength, it.data.attributes.youtubeVideoId)
                    }catch (e: NullPointerException){
                        view.findNavController().popBackStack()
                    }
                    dialog.dismiss()
                }
                elementViewModel.getAnimeTitleById(titleId, requireContext())
                dialog.show()
                DeleteTitle.isEnabled = false
            }
        }

        AddTitle.setOnClickListener {
            elementViewModel.viewModelScope.launch {
                elementViewModel.saveTitle(titleInf)
                Toast.makeText(context, resources.getString(R.string.title_added), Toast.LENGTH_SHORT).show()
                AddTitle.isEnabled = false
                DeleteTitle.isEnabled = true
                AddTitle.text = resources.getString(R.string.button_title_added)
                DeleteTitle.text = resources.getString(R.string.button_title_delete)
            }
        }

        DeleteTitle.setOnClickListener {
            elementViewModel.viewModelScope.launch {
                elementViewModel.deleteTitle(titleInf)
                Toast.makeText(context, resources.getString(R.string.title_deleted), Toast.LENGTH_SHORT).show()
                DeleteTitle.isEnabled = false
                AddTitle.isEnabled = true
                DeleteTitle.text = resources.getString(R.string.button_title_deleted)
                AddTitle.text = resources.getString(R.string.button_title_add)
            }
        }

        VideoButton.setOnClickListener{
            val intent = Intent(context, YoutubeActivity::class.java)
            intent.putExtra("youtubeVideoId", titleYTVideoId)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        elementViewModel.livedata.removeObservers(viewLifecycleOwner)
        elementViewModel.getCountTitleById(titleId).removeObservers(viewLifecycleOwner)
        elementViewModel.getTitleById(titleId).removeObservers(viewLifecycleOwner)
    }
}