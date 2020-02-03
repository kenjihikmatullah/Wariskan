package com.wariskan.ui.deceased

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kenji.waris.model.Gender.MALE
import com.kenji.waris.model.Position.DECEASED
import com.wariskan.AddEditActivity
import com.wariskan.R.drawable.*
import com.wariskan.R.layout.fragment_deceased
import com.wariskan.R.string.deceased
import com.wariskan.ui.inheritance.InheritanceActivity
import com.wariskan.ui.inheritance.InheritanceViewModel
import com.wariskan.util.ID
import com.wariskan.util.ORDER
import com.wariskan.util.POSITION
import com.wariskan.databinding.FragmentDeceasedBinding as Binding
import com.wariskan.ui.deceased.DeceasedViewModel as ViewModel

class DeceasedFragment : Fragment() {

    private lateinit var binding: Binding
    private lateinit var inheritanceViewModel: InheritanceViewModel
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = inflate(inflater, fragment_deceased, container, false)
        setUpViewModel()
        setOnEdit()
        return binding.root
    }

    private fun setUpViewModel() {
        activity?.let {
            binding.lifecycleOwner = this
            inheritanceViewModel = ViewModelProvider(it).get(InheritanceViewModel::class.java)
            viewModel = ViewModelProvider(this).get(ViewModel::class.java)
            binding.viewModel = viewModel
        }
    }

    private fun adjustLayout() {
        inheritanceViewModel.repository.inheritance.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer

            binding.apply {

                /*
                 * Photo
                 */
                photoIv.apply {
                    setImageResource(
                        if (it.deceased.gender == MALE) ic_muslim
                        else ic_muslimah
                    )
                }

                /*
                 * Name
                 */
                nameTv.apply {
                    text = it.deceased.name
                }
            }
        })
    }

    private fun setOnEdit() {
        viewModel.onEdit.observe(viewLifecycleOwner, Observer { onEdit ->
            if (!onEdit) return@Observer

            activity?.let {
                val intent = Intent(it, AddEditActivity::class.java)
                intent.putExtra(ID, inheritanceViewModel.id)
                intent.putExtra(POSITION, DECEASED)
                intent.putExtra(ORDER, -1)
                startActivity(intent)
            }

            viewModel.edited()
        })
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            if (it is InheritanceActivity) {
                it.binding.toolbarTitle.text = it.getString(deceased)
            }
        }
        adjustLayout()
    }
}