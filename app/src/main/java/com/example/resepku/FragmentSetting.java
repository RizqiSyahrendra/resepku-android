package com.example.resepku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class FragmentSetting extends Fragment {
    CardView cardSettingProfile, cardSettingSignOut;
    MainActivity parent;

    public FragmentSetting() {
        // Required empty public constructor
    }

    public static FragmentSetting newInstance(MainActivity tempActivity) {
        FragmentSetting fragment = new FragmentSetting();
        fragment.parent = tempActivity;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardSettingProfile = view.findViewById(R.id.cardSettingProfile);
        cardSettingSignOut = view.findViewById(R.id.cardSettingSignOut);

        cardSettingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intentProfile = new Intent(getContext(), ProfileActivity.class);
                 startActivity(intentProfile);
            }
        });

        cardSettingSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("Sign Out")
                        .setMessage("Are you sure you want to sign out?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new LogoutTask().execute();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert);

                alertDialog.show();
            }
        });
    }

    private class LogoutTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            parent.getDB().userLoginDao().clear();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intentLogin = new Intent(getContext(), LoginActivity.class);
            getActivity().startActivity(intentLogin);
            getActivity().finish();
            super.onPostExecute(aVoid);
        }
    }
}