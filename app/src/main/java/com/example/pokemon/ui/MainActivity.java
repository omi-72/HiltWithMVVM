package com.example.pokemon.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.pokemon.R;
import com.example.pokemon.databinding.ActivityMainBinding;
import com.example.pokemon.ui.fragments.FavoritesFragment;
import com.example.pokemon.ui.fragments.HomeFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private boolean isFavoriteListVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new HomeFragment())
                .commit();

        binding.changeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavoriteListVisible){
                    isFavoriteListVisible = false;
                    binding.changeFragment.setText("Favorites");
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new HomeFragment())
                            .commit();
                }
                else {
                    isFavoriteListVisible = true;
                    binding.changeFragment.setText("Home");
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FavoritesFragment())
                            .commit();
                }
            }
        });
    }
}