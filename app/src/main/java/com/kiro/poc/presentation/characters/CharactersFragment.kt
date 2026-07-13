package com.kiro.poc.presentation.characters

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.kiro.poc.R
import com.kiro.poc.databinding.FragmentCharactersBinding
import com.kiro.poc.presentation.MainApp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharactersFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!

    private val charactersAdapter: CharactersAdapter by lazy {
        CharactersAdapter { character ->
            val action = CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailsFragment(character.id)
            findNavController().navigate(action)
        }
    }

    companion object {
        fun newInstance() = CharactersFragment()
    }

    private val viewModel: CharactersViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as MainApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_characters, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.charactersRecyclerView.adapter = charactersAdapter
        binding.charactersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.characters.collectLatest {
                charactersAdapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.loadingProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.errorTv.isVisible = loadStates.refresh is LoadState.Error
                
                val errorState = loadStates.refresh as? LoadState.Error
                    ?: loadStates.append as? LoadState.Error
                    ?: loadStates.prepend as? LoadState.Error
                
                errorState?.let {
                    binding.errorTv.text = it.error.localizedMessage
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
