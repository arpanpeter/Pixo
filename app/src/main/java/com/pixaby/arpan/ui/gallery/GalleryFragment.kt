
package com.pixaby.arpan.ui.gallery

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.pixaby.arpan.R
import com.pixaby.arpan.arch.mvi.IView
import com.pixaby.arpan.databinding.GalleryFragmentBinding
import com.pixaby.arpan.ui.gallery.action.GalleryActions
import com.pixaby.arpan.ui.gallery.adapter.GalleryAdapter
import com.pixaby.arpan.ui.gallery.state.GalleryState
import com.pixaby.arpan.ui.gallery.viewmodel.GalleryViewModel
import com.pixaby.arpan.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.gallery_fragment), IView<GalleryState> {

    private val viewModel by viewModels<GalleryViewModel>()
    private var _binding: GalleryFragmentBinding? = null
    private val binding get() = _binding!!

    private val galleryAdapter by lazy {
        GalleryAdapter { view, imageUrl ->
            navigateWith(view, imageUrl)
        }
    }

    companion object {
        private const val DEFAULT_QUERY = "nature"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = GalleryFragmentBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        initUi()
        subscribeObservers()
        if (viewModel.fragmentState())
            sendAction(GalleryActions.GetImages(DEFAULT_QUERY))
        viewModel.fragmentState(false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setExitToFullScreenTransition()
        setReturnFromFullScreenTransition()
    }

    private fun initUi() {
        binding.rvGallery.apply {
            setHasFixedSize(true)
            adapter = galleryAdapter

            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }

    private fun subscribeObservers() {
        // Observing the state
        viewModel.state.observe(viewLifecycleOwner, {
            render(it)
        })

        viewModel.images.observe(viewLifecycleOwner) {
            binding.galleryProgressBar.gone()
            galleryAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            waitForTransition(binding.rvGallery)
        }
    }

    private fun sendAction(galleryActions: GalleryActions) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.actions.send(galleryActions)
        }
    }

    override fun render(state: GalleryState) {
        when (state) {
            is GalleryState.GetImages -> {
                binding.galleryProgressBar.isVisible = state.isLoading
                if (state.errorMessage != null) {
                    requireContext().toast("Error: ${state.errorMessage}")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_gallery, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.rvGallery.scrollToPosition(0)
                    sendAction(GalleryActions.GetImages(query))
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun navigateWith(view: View, imageUrl: String) {
        val extras = FragmentNavigatorExtras(
                view to imageUrl
        )
        val direction = GalleryFragmentDirections.actionGalleryFragmentToPreviewFragment(imageUrl)
        navigate(direction, extras)
    }

    private fun navigate(destination: NavDirections, extraInfo: FragmentNavigator.Extras) =
        with(findNavController()) {
            currentDestination?.getAction(destination.actionId)
                ?.let { navigate(destination, extraInfo) }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}