package com.example.animeteka.presentation.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.animeteka.R
import com.example.animeteka.presentation.viewmodels.InviteViewModel

class InviteFragment : Fragment() {

    companion object {
        fun newInstance() = InviteFragment()
    }

    private lateinit var inviteViewModel: InviteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inviteViewModel = ViewModelProvider(this).get(InviteViewModel::class.java)
        return inflater.inflate(R.layout.fragment_invite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.invite).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.invite_message))
            val chosenIntent = Intent.createChooser(intent, resources.getString(R.string.invite_message_label))
            startActivity(chosenIntent)
        }
    }

}