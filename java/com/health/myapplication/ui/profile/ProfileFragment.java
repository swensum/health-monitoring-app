package com.health.myapplication.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.hdodenhof.circleimageview.CircleImageView;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.health.myapplication.DatabaseHelper;
import com.health.myapplication.R;
import com.health.myapplication.User;



public class ProfileFragment extends Fragment {

    private TextView fullNameTextView, mobileTextView, addressTextView, emailTextView, sexTextView, divisionTextView;
    private EditText editFullName, editMobile, editAddress, editSex, editDivision;
    private Button updateButton, saveButton, changeProfileButton;
    private LinearLayout textViewLayout, editTextLayout;
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseHelper databaseHelper;
    private CircleImageView profileImageView;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewLayout = root.findViewById(R.id.textViewLayout);
        editTextLayout = root.findViewById(R.id.editTextLayout);

        fullNameTextView = root.findViewById(R.id.text_fullName);
        mobileTextView = root.findViewById(R.id.text_mobile);
        addressTextView = root.findViewById(R.id.text_address);
        emailTextView = root.findViewById(R.id.text_email);
        sexTextView = root.findViewById(R.id.text_Sex);
        divisionTextView = root.findViewById(R.id.text_division);

        editFullName = root.findViewById(R.id.edit_fullName);
        editMobile = root.findViewById(R.id.edit_mobile);
        editAddress = root.findViewById(R.id.edit_address);
        editSex = root.findViewById(R.id.edit_sex);
        editDivision = root.findViewById(R.id.edit_division);

        profileImageView = root.findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(v -> openGallery());

        updateButton = root.findViewById(R.id.button_update);
        saveButton = root.findViewById(R.id.button_save);
        changeProfileButton = root.findViewById(R.id.changeProfileButton);

        databaseHelper = new DatabaseHelper(requireContext());
        Bundle args = getArguments();
        if (args != null) {
            int userId = args.getInt("USER_ID", -1);

            if (userId != -1) {
                user = databaseHelper.getUserById(userId);

                if (user != null) {
                    Log.d("ProfileFragment", "User retrieved successfully: " + user.toString());
                    displayUserData(user);
                    setEditMode(false);

                    updateButton.setOnClickListener(v -> setEditMode(true));


                    saveButton.setOnClickListener(v -> {
                        // Get updated data from EditText fields
                        String updatedFullName = editFullName.getText().toString().trim();
                        String updatedMobile = editMobile.getText().toString().trim();
                        String updatedAddress = editAddress.getText().toString().trim();
                        String updatedSex = editSex.getText().toString().trim();
                        String updatedDivision = editDivision.getText().toString().trim();

                        // Update user object
                        user.setFullName(updatedFullName);
                        user.setMobile(updatedMobile);
                        user.setAddress(updatedAddress);
                        user.setSex(updatedSex);
                        user.setDivision(updatedDivision);

                        // Attempt to update the user in the database
                        boolean isUpdateSuccessful = databaseHelper.updateUser(user);

                        if (isUpdateSuccessful) {
                            // Display the updated user data immediately
                            displayUserData(user);

                            setEditMode(false);
                            Toast.makeText(requireContext(), "Save successful", Toast.LENGTH_SHORT).show();
                            Log.i("ProfileFragment", "Save successful: Data updated in the database");
                        } else {
                            Toast.makeText(requireContext(), "Save failed", Toast.LENGTH_SHORT).show();
                            Log.e("ProfileFragment", "Save failed: Database update unsuccessful");
                        }
                    });
                } else {
                    // Handle the case where the user data couldn't be retrieved
                    Log.e("ProfileFragment", "User data not found for ID: " + userId);
                }
            } else {
                // Handle the case where the user ID is not provided
                Log.e("ProfileFragment", "User ID not provided");
            }
        } else {
            // Handle the case where the arguments are null
            Log.e("ProfileFragment", "Arguments are null");
        }

        return root;
    }
    @Override
    public void onResume() {
        super.onResume();

        loadProfileImage();
    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri imageUri = data.getData();
                        // Update the user object with the profile image URI
                        user.setProfileImageUri(imageUri.toString());
                        // Save the updated user object to the database
                        boolean isUpdateSuccessful = databaseHelper.updateUser(user);
                        if (isUpdateSuccessful) {
                            // Display the updated user data immediately
                            displayUserData(user);
                            Toast.makeText(requireContext(), "Profile picture updated", Toast.LENGTH_SHORT).show();
                            Log.i("ProfileFragment", "Profile picture updated: Data updated in the database");
                        } else {
                            Toast.makeText(requireContext(), "Profile picture update failed", Toast.LENGTH_SHORT).show();
                            Log.e("ProfileFragment", "Profile picture update failed: Database update unsuccessful");
                        }
                    }
                }
            });
    private void loadProfileImage() {

        String profileImageUri = user.getProfileImageUri();
        if (profileImageUri != null && !profileImageUri.isEmpty()) {

            Glide.with(this)
                    .load(Uri.parse(profileImageUri))
                    .into(profileImageView);
        }
    }

    private void displayUserData(User user) {
        if (user != null) {
            Log.d("ProfileFragment", "Displaying user data: " + user.toString());
            fullNameTextView.setText("Full Name: " + user.getFullName());
            mobileTextView.setText("Mobile: " + user.getMobile());
            addressTextView.setText("Address: " + user.getAddress());
            emailTextView.setText("Email: " + user.getEmail());
            sexTextView.setText("Sex: " + user.getSex());
            divisionTextView.setText("Division: " + user.getDivision());

            editFullName.setText(user.getFullName());
            editMobile.setText(user.getMobile());
            editAddress.setText(user.getAddress());
            editSex.setText(user.getSex());
            editDivision.setText(user.getDivision());

            // Use getView() to get the root view of the fragment
            View rootView = getView();

            if (rootView != null) {
                CircleImageView profileImageView = rootView.findViewById(R.id.profileImageView);

                // Check if the user has a profile picture URI
                String profileImageUri = user.getProfileImageUri();
                Log.d("ProfileFragment", "Profile Image URI: " + profileImageUri);
                if (profileImageUri != null && !profileImageUri.isEmpty()) {
                    Log.d("ProfileFragment", "Loading profile image with Glide. URI: " + profileImageUri);

                    Glide.with(this)
                            .load(Uri.parse(profileImageUri))
                            .into(profileImageView);
                }
            } else {
                Log.e("ProfileFragment", "Profile image URI is null or empty");
            }


        } else {
            Log.e("ProfileFragment", "User object is null");
        }
    }



    private void setEditMode(boolean isEditMode) {
        int textViewVisibility = isEditMode ? View.GONE : View.VISIBLE;
        int editTextVisibility = isEditMode ? View.VISIBLE : View.GONE;
        int imageViewVisibility = isEditMode ? View.GONE: View.VISIBLE;

        textViewLayout.setVisibility(textViewVisibility);
        editTextLayout.setVisibility(editTextVisibility);
        profileImageView.setVisibility(imageViewVisibility);

        updateButton.setVisibility(isEditMode ? View.GONE : View.VISIBLE);
        saveButton.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
        changeProfileButton.setVisibility(isEditMode ? View.GONE : View.VISIBLE);
    }
}
