package edu.neu.final_project_group_4.ui.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;

import edu.neu.final_project_group_4.R;
import edu.neu.final_project_group_4.databinding.FragmentDashboardBinding;
import edu.neu.final_project_group_4.utils.Auth;
import edu.neu.final_project_group_4.utils.User;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Set up click listener for the add_task_button, would jump to add task fragment
        binding.addTaskButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.addTaskFragment); // Navigate to AddTaskFragment directly by ID
        });

        //sign out
        binding.signOutButton.setOnClickListener(v -> {
            Auth.signOut(getActivity());
        });

        // Set a click listener on the Edit Profile button
        binding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the ProfileEditActivity
                Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
                startActivity(intent);
            }
        });
        
        // User full name
        final TextView name = binding.profileUserFullName;
        dashboardViewModel.getUserFullName().observe(getViewLifecycleOwner(),
                name::setText);
        dashboardViewModel.loadUserFullName();

        // Photo
        final ImageView photo = binding.profileImage;
        Uri photoUrl = User.getInstance().getPhotoUrl();
        loadProfilePhoto(photoUrl, photo);

        // User description
        final TextView description = binding.descriptionText;
        dashboardViewModel.getUserDescription().observe(getViewLifecycleOwner(),
                description::setText);
        loadDescription(true);

        return root;
    }

    private void loadProfilePhoto(Uri photoUrl, ImageView imageView) {
        if (photoUrl != null) {
            Glide.with(this).load(photoUrl).into(imageView);
        }
    }

    public void loadDescription(boolean needFetch) {
        dashboardViewModel.loadUserDescription(needFetch);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}