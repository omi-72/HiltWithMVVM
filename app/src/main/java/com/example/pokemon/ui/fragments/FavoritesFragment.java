package com.example.pokemon.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pokemon.R;
import com.example.pokemon.adapters.PokemonAdapter;
import com.example.pokemon.databinding.FragmentFavoritesBinding;
import com.example.pokemon.model.Pokemon;
import com.example.pokemon.viewmodel.PokemonViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private PokemonViewModel viewModel;
    private PokemonAdapter adapter;
    private ArrayList<Pokemon> pokemonList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PokemonViewModel.class);

        initRecyclerView();
        setUpItemTouchHelper();
        observeData();
        //viewModel.getFavoritePokemon();
    }

    private void observeData() {
        viewModel.getFavoritePokemonList().observe(getViewLifecycleOwner(), new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(List<Pokemon> pokemons) {

                if(pokemons == null || pokemons.size() == 0)
                    binding.noFavoritesText.setVisibility(View.VISIBLE);
                else{
                    ArrayList<Pokemon> list = new ArrayList<>();
                    list.addAll(pokemons);
                    adapter.updateList(list);
                }
            }
        });
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPokemonPosition = viewHolder.getAdapterPosition();
                Pokemon pokemon = adapter.getPokemonAt(swipedPokemonPosition);
                viewModel.deletePokemon(pokemon.getName());
                Toast.makeText(getContext(),"Pokemon removed from favorites.",Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.favoritesRecyclerView);
    }

    private void initRecyclerView() {
        binding.favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PokemonAdapter(getContext(),pokemonList);
        binding.favoritesRecyclerView.setAdapter(adapter);
    }
}