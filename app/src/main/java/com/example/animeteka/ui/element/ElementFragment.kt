package com.example.animeteka.ui.element

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.animeteka.R

class ElementFragment : Fragment() {

    companion object {
        fun newInstance() = ElementFragment()
    }

    private lateinit var viewModel: ElementViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_element, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ElementViewModel::class.java)
        // TODO: Use the ViewModel
    }

}