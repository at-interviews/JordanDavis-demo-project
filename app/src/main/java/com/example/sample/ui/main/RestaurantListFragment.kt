package com.example.sample.ui.main

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sample.R
import com.example.sample.databinding.FragmentFirstBinding
import com.example.sample.ui.main.model.RestaurantListItemUiModel
import com.example.sample.ui.main.model.RestaurantsListAdapter
import com.google.accompanist.appcompattheme.AppCompatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantListFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val viewModel: RestaurantSearchViewModel by activityViewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.employeesRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = RestaurantsListAdapter(::onItemClicked)
        }

        viewModel.employeesListUiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is RestaurantListUiState.Loading -> setProgressBarVisible(true)
                is RestaurantListUiState.Data -> updateDataState(uiState.restaurants)
                is RestaurantListUiState.Empty -> updateEmptyState()
                is RestaurantListUiState.Error -> updateErrorState(uiState.errorMessage)
            }
        }
    }

    // Super rough, but just to demonstrate an item being clicked and showing information
    private fun onItemClicked(item: RestaurantListItemUiModel) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(item.name)
        builder.create().show()
    }

    private fun updateDataState(employees: List<RestaurantListItemUiModel>) {
        setProgressBarVisible(false)
        (binding.employeesRecyclerView.adapter as RestaurantsListAdapter).updateEmployeesList(employees)
    }

    private fun updateEmptyState() {
        setProgressBarVisible(false)
        binding.errorMessage.isVisible = true
        binding.errorMessage.setContent {
            AppCompatTheme {
                ErrorMessage(requireContext().getString(R.string.no_employees))
            }
        }
    }

    private fun updateErrorState(errorMessage: String) {
        setProgressBarVisible(false)
        binding.errorMessage.isVisible = true
        binding.errorMessage.setContent {
            AppCompatTheme {
                ErrorMessage(errorMessage)
            }
        }
    }

    private fun setProgressBarVisible(isVisible: Boolean) {
        binding.listProgressBar.isVisible = isVisible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Little compose demonstration
    @Composable
    private fun ErrorMessage(errorMessage: String) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}