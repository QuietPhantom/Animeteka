package com.example.animeteka.ui.element

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
import androidx.navigation.findNavController
import com.example.animeteka.R
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog


class ElementFragment : Fragment() {

    companion object {
        fun newInstance() = ElementFragment()
    }

    private lateinit var dialog: AlertDialog
    private lateinit var elementViewModel: ElementViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        elementViewModel = ViewModelProvider(this).get(ElementViewModel::class.java)
        return inflater.inflate(R.layout.fragment_element, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.back).setOnClickListener {
            view.findNavController().popBackStack()
        }
        elementViewModel.initApi()
        dialog = SpotsDialog.Builder().setCancelable(true).setContext(context).build()
        dialog.show()
        elementViewModel.getAnimeTitleById(requireArguments().getInt("titleId"))
        elementViewModel.livedata.observe(viewLifecycleOwner){
            Picasso.get().load(it.data.attributes.posterImage.original).into(view.findViewById<ImageView>(R.id.titleImage))
            view.findViewById<TextView>(R.id.TitleName).text = it.data.attributes.canonicalTitle
            view.findViewById<TextView>(R.id.enTitleName).text = it.data.attributes.titles.en
            view.findViewById<TextView>(R.id.Dates).text = it.data.attributes.startDate + " - " + it.data.attributes.endDate
            view.findViewById<TextView>(R.id.Rating).text = it.data.attributes.averageRating
            view.findViewById<TextView>(R.id.ageRating).text = it.data.attributes.ageRating + " (" +  it.data.attributes.ageRatingGuide + ")"
            view.findViewById<TextView>(R.id.Count).text = it.data.attributes.episodeCount + " / " + it.data.attributes.episodeLength + " / " + it.data.attributes.totalLength
            view.findViewById<TextView>(R.id.StatusAndType).text = it.data.attributes.subtype + " | " + it.data.attributes.status
            view.findViewById<TextView>(R.id.fullDescription).text = it.data.attributes.description
            view.findViewById<TextView>(R.id.fullDescription).movementMethod =
                ScrollingMovementMethod()
        }
        dialog.dismiss()
    }
}