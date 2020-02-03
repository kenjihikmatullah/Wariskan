package com.wariskan.ui.addedit

import android.R.layout.simple_spinner_dropdown_item
import android.R.layout.simple_spinner_item
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kenji.waris.database.InheritanceDatabase.Companion.getInstance
import com.kenji.waris.model.Deceased
import com.kenji.waris.model.Gender.FEMALE
import com.kenji.waris.model.Gender.MALE
import com.kenji.waris.model.Heir
import com.kenji.waris.model.Position
import com.kenji.waris.model.Position.*
import com.wariskan.AddEditActivity
import com.wariskan.R.array.*
import com.wariskan.R.layout.fragment_add_edit
import com.wariskan.R.string.*
import com.wariskan.model.person.*
import com.wariskan.model.property.Type.*
import com.wariskan.ui.inheritance.InheritanceActivity
import com.wariskan.util.ID
import com.wariskan.util.ORDER
import com.wariskan.util.POSITION
import android.view.LayoutInflater as Inflater
import android.widget.ArrayAdapter as Adapter
import android.widget.ArrayAdapter.createFromResource as create
import com.wariskan.databinding.FragmentAddEditBinding as Binding
import com.wariskan.ui.addedit.AddEditViewModel as ViewModel
import com.wariskan.ui.addedit.AddEditViewModelFactory as Factory

class AddEditFragment() : Fragment(), OnItemSelectedListener {

    private lateinit var activity: AddEditActivity
    private lateinit var binding: Binding
    private lateinit var viewModel: ViewModel
    private var done = false

    override fun onCreateView(
        inflater: Inflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity = getActivity() as AddEditActivity
        binding = inflate(inflater, fragment_add_edit, container, false)
        setUpViewModel()
        handleArguments()
        setUpSpinner()
        adjustLayout()
        setOnSave()
        setOnDelete()

        return binding.root
    }

    private fun setUpViewModel() {
        val database = getInstance(activity)
        val factory = Factory(database)
        viewModel = ViewModelProvider(this, factory).get(ViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun handleArguments() {
        activity.intent?.let {
            viewModel.handleArguments(
                it.getIntExtra(ID, -1),
                it.getSerializableExtra(POSITION) as Position,
                it.getIntExtra(ORDER, -1)
            )
        }
    }

    private fun setUpSpinner() {

        fun setUp(
            spinner: Spinner,
            adapter: Adapter<CharSequence>
        ) {
            adapter.setDropDownViewResource(simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = this
        }

        /*
         * Status
         */
        var adapter = create(activity, status_array, simple_spinner_item)
        setUp(binding.statusSpinner, adapter)

        /*
         * Faith
         */
        adapter = create(activity, faith_array, simple_spinner_item)
        setUp(binding.faithSpinner, adapter)

        /*
         * Gender
         */
        adapter = create(activity, gender_array, simple_spinner_item)
        setUp(binding.genderSpinner, adapter)

        /*
         * Query
         */
        when (viewModel.position) {
            SIBLING -> {
                binding.queryOne.setText(query_sibling)
                adapter = create(activity, sibling_array, simple_spinner_item)
                setUp(binding.oneSpinner, adapter)
                binding.one.visibility = VISIBLE
            }
            GRANDPA -> {
                binding.queryOne.setText(query_one_grandpa)
                adapter = create(activity, boolean_array, simple_spinner_item)
                setUp(binding.oneSpinner, adapter)
                binding.one.visibility = VISIBLE
            }
            GRANDMA -> {
                binding.queryOne.setText(query_one_grandma)
                adapter = create(activity, boolean_array, simple_spinner_item)
                setUp(binding.oneSpinner, adapter)
                binding.one.visibility = VISIBLE
            }
            GRANDCHILD -> {
                binding.queryOne.setText(query_one_grandchild)
                adapter = create(activity, boolean_array, simple_spinner_item)
                setUp(binding.oneSpinner, adapter)
                binding.one.visibility = VISIBLE
            }
            UNCLE -> {
                binding.queryOne.apply {
                    setText(query_one_uncle)
                }
                adapter = create(activity, boolean_array, simple_spinner_item)
                setUp(binding.oneSpinner, adapter)
                binding.one.visibility = VISIBLE

                binding.queryTwo.apply {
                    setText(query_brother)
                }
                adapter = create(activity, brother_array, simple_spinner_item)
                setUp(binding.twoSpinner, adapter)
            }
            MALE_COUSIN -> {
                binding.queryOne.setText(query_one_male_cousin)
                adapter = create(activity, boolean_array, simple_spinner_item)
                setUp(binding.oneSpinner, adapter)
                binding.one.visibility = VISIBLE

                binding.queryTwo.apply {
                    setText(query_brother)
                }
                adapter = create(activity, brother_array, simple_spinner_item)
                setUp(binding.twoSpinner, adapter)

            }
            NEPHEW -> {
                binding.queryOne.setText(query_one_nephew)
                adapter = create(activity, boolean_array, simple_spinner_item)
                setUp(binding.oneSpinner, adapter)
                binding.one.visibility = VISIBLE

                binding.queryTwo.apply {
                    setText(query_brother)
                }
                adapter = create(activity, brother_array, simple_spinner_item)
                setUp(binding.twoSpinner, adapter)
            }
            else -> 0.inc()
        }
    }

    private fun adjustLayout() {
        viewModel.apply {
            binding.apply {

                /*
                 * Header
                 */
                headerText.let {
                    val first = activity.getString(if (!isEditing) add else edit)
                    val second = position.getName(activity)
                    it.text = resources.getString(add_edit_title, first, second)
                }

                /*
                 * Status
                 */
                if (!position.isHavingStatus) {
                    statusLayout.visibility = GONE
                }

                /*
             * Faith
             */
                if (!position.isHavingFaith) {
                    faithLayout.visibility = GONE
                }

                /*
                 * Gender
                 */
                if (!position.isHavingGender) {
                    genderLayout.visibility = GONE
                }

                /*
                 * Delete
                 */
                if (!isEditing || !position.isHavingDelete) {
                    deleteButton.visibility = GONE
                }

                /*
                 * Observe
                 */
                if (isEditing) {
                    inheritance.observe(viewLifecycleOwner, Observer { inheritance ->
                        inheritance ?: return@Observer

                        /*
                     * Deceased
                     */
                        if (position == DECEASED) {
                            val deceased = inheritance.deceased
                            nameEt.setText(deceased.name)
                            faithSpinner.setSelection(if (deceased.muslim) 0 else 1)
                            genderSpinner.setSelection(if (deceased.gender == MALE) 0 else 1)
                        }

                        /*
                     * Heir
                     */
                        else {
                            val heir = inheritance.getHeir(position, order)
                            nameEt.setText(heir.name)
                            faithSpinner.setSelection(if (heir.muslim) 0 else 1)
                            if (heir.gender == MALE) {
                                genderSpinner.setSelection(0)
                            } else {
                                genderSpinner.setSelection(1)
                            }
                        }
                    })
                }
            }
        }

    }

    private fun setOnSave() {
        viewModel.onSave.observe(viewLifecycleOwner, Observer { onSave ->
            if (!onSave) return@Observer

            viewModel.apply {
                binding.apply {


                    /*
                     * Get data from layout
                     */
                    if (position == DECEASED) {
                        val deceased = Deceased().also {
                            if (nameEt.text.isNullOrBlank()) {
                                nameLayout.error = activity.getString(et_blank)
                                return@Observer
                            } else {
                                it.name = "${nameEt.text}"
                            }
                            it.alive = false
                            it.muslim = if (spinnerFaith == 0) true else false
                            it.gender = if (spinnerGender == 0) MALE else FEMALE
                        }
                        addEditDeceased(deceased)

                    } else {

                        if (nameEt.text.isNullOrBlank()) {
                            nameEt.setError("")
                            return@Observer
                        }

                        fun Heir.hehehe() {
                            name = "${nameEt.text}"
                            alive = if (spinnerStatus == 0) true else false
                            muslim = if (spinnerFaith == 0) true else false
                            gender = if (spinnerGender == 0) MALE else FEMALE
                        }

                        var heir = Heir()
                        heir.hehehe()

                        when (position) {
                            SIBLING -> {
                                val sibling = Sibling()
                                sibling.hehehe()
                                sibling.type = if (spinnerOne == 0) FULL else PATERNAL
                                heir = sibling
                            }
                            GRANDPA -> {
                                val grandpa = Grandpa()
                                grandpa.hehehe()
                                grandpa.boolOne = if (spinnerOne == 0) true else false
                                heir = grandpa
                            }
                            GRANDMA -> {
                                val grandma = Grandma()
                                grandma.hehehe()
                                grandma.boolOne = if (spinnerOne == 0) true else false
                                heir = grandma
                            }
                            GRANDCHILD -> {
                                val grandchild = Grandchild()
                                grandchild.hehehe()
                                grandchild.boolOne = if (spinnerOne == 0) true else false
                                heir = grandchild
                            }
                            UNCLE -> {
                                val uncle = Uncle()
                                uncle.hehehe()
                                uncle.boolOne = if (spinnerOne == 0) true else false
                                uncle.type = when (spinnerTwo) {
                                    0 -> FULL
                                    1 -> PATERNAL
                                    else -> MATERNAL
                                }
                                heir = uncle
                            }
                            MALE_COUSIN -> {
                                val maleCousin = MaleCousin()
                                maleCousin.hehehe()
                                maleCousin.boolOne = if (spinnerOne == 0) true else false
                                maleCousin.type = when (spinnerTwo) {
                                    0 -> FULL
                                    1 -> PATERNAL
                                    else -> MATERNAL
                                }
                                heir = maleCousin
                            }
                            NEPHEW -> {
                                val nephew = Nephew()
                                nephew.hehehe()
                                nephew.boolOne = if (spinnerOne == 0) true else false
                                nephew.type = when (spinnerTwo) {
                                    0 -> FULL
                                    1 -> PATERNAL
                                    else -> MATERNAL
                                }
                                heir = nephew
                            }
                            else -> 0.inc()
                        }

                        addEditHeir(heir, activity)
                    }

                    /*
                     * Insert or update
                     */
                    if (id == -1) {
                        insert()

                    } else {
                        update()
                    }

                    /*
                     * Navigate
                     */
                    if (position == DECEASED) {
                        toDeceased()
                    } else {
                        toHeir()
                    }

                    saved()
                }
            }
        })
    }

    private fun setOnDelete() {
        viewModel.onDelete.observe(viewLifecycleOwner, Observer {
            if (!it) return@Observer

            viewModel.apply {
                inheritance.value?.deleteHeir(position, order)
                update()
                toHeir()
                deleted()
            }
        })
    }

    private fun toDeceased() {
        val intent = Intent(activity, InheritanceActivity::class.java)
        intent.putExtra(ID, viewModel.id)
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun toHeir() {
        val intent = Intent(activity, InheritanceActivity::class.java)
        intent.putExtra(ID, viewModel.id)
        intent.putExtra(POSITION, viewModel.position)
        startActivity(intent)
    }

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        order: Int,
        id: Long
    ) {

        viewModel.apply {
            binding.apply {
                when (parent) {
                    statusSpinner -> spinnerStatus = order
                    faithSpinner -> spinnerFaith = order
                    genderSpinner -> spinnerGender = order
                    oneSpinner -> {
                        spinnerOne = order
                        if (position in listOf(UNCLE, MALE_COUSIN, NEPHEW)) {
                            two.visibility = if (order == 0) VISIBLE else GONE
                        }

                    }
                    else -> 0.inc()
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO()
    }
}
