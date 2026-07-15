package com.kiro.poc.presentation.characterRows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kiro.poc.R
import com.kiro.poc.databinding.FragmentCharacterRowsBinding
import com.kiro.poc.presentation.MainApp
import javax.inject.Inject

class CharacterRowsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentCharacterRowsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterRowsViewModel by viewModels { viewModelFactory }

    private val characterRowsAdapter: CharacterRowsAdapter by lazy {
        CharacterRowsAdapter { character ->
            val action = CharacterRowsFragmentDirections
                .actionCharacterRowsFragmentToCharacterDetailsFragment(character.id)
            findNavController().navigate(action)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as MainApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_character_rows, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.characterRowsRecyclerView.apply {
            adapter = characterRowsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            itemAnimator = null
        }

        viewModel.rows.observe(viewLifecycleOwner) { rows ->
            characterRowsAdapter.submitList(rows)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingProgressBar.isVisible = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.errorTv.isVisible = error != null
            error?.let { binding.errorTv.text = it.asString(requireContext()) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
