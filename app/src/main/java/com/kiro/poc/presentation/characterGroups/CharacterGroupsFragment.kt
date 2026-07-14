package com.kiro.poc.presentation.characterGroups

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
import com.kiro.poc.databinding.FragmentCharacterGroupsBinding
import com.kiro.poc.presentation.MainApp
import javax.inject.Inject

class CharacterGroupsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentCharacterGroupsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterGroupsViewModel by viewModels { viewModelFactory }

    private val characterGroupsAdapter: CharacterGroupsAdapter by lazy {
        CharacterGroupsAdapter { character ->
            val action = CharacterGroupsFragmentDirections
                .actionCharacterGroupsFragmentToCharacterDetailsFragment(character.id)
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_character_groups, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.characterGroupsRecyclerView.adapter = characterGroupsAdapter
        binding.characterGroupsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.items.observe(viewLifecycleOwner) { items ->
            characterGroupsAdapter.submitList(items)
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
