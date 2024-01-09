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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pokemon.R;
import com.example.pokemon.adapters.PokemonAdapter;
import com.example.pokemon.databinding.FragmentHomeBinding;
import com.example.pokemon.model.Pokemon;
import com.example.pokemon.viewmodel.PokemonViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint

public class HomeFragment extends Fragment {

    private static final String TAG = "Home";
    private FragmentHomeBinding binding;
    private PokemonViewModel viewModel;

    private PokemonAdapter adapter;
    private ArrayList<Pokemon> pokemonList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PokemonViewModel.class);

        initRecyclerView();
        observeData();
        setUpItemTouchHelper();
        viewModel.getPokemons();
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPokemonPosition = viewHolder.getAdapterPosition();
                Pokemon pokemon = adapter.getPokemonAt(swipedPokemonPosition);
                viewModel.insertPokemon(pokemon);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(),"Pokemon added to favorites.",Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.pokemonRecyclerView);
    }


    private void observeData() {
        viewModel.getPokemonList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Pokemon>>() {
            @Override
            public void onChanged(ArrayList<Pokemon> pokemons) {
                Log.e(TAG, "onChanged: " + pokemons.size() );
                adapter.updateList(pokemons);
            }
        });
    }

    private void initRecyclerView() {
        binding.pokemonRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PokemonAdapter(getContext(),pokemonList);
        binding.pokemonRecyclerView.setAdapter(adapter);
    }
}