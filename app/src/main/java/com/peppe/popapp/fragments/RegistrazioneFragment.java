package com.peppe.popapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.peppe.popapp.R;
import com.peppe.popapp.api.APIService;
import com.peppe.popapp.api.APIUrl;
import com.peppe.popapp.models.Utente;
import com.peppe.popapp.results.ResultRegistrazione;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrazioneFragment extends Fragment {

    EditText editTextUsername, editTextEmail, editTextPassword, editTextConfermaPassword;
    Button buttonRegistrazione;

    String username;
    String email;
    String password;
    String confermaPassword;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Registrazione");

        editTextUsername = (EditText) getView().findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) getView().findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) getView().findViewById(R.id.editTextPassword);
        editTextConfermaPassword = (EditText) getView().findViewById(R.id.editTextConfermaPassword);
        buttonRegistrazione = (Button) getView().findViewById(R.id.buttonRegistrazione);

        buttonRegistrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlloValidità() == true) {
                    loadRegistrazioneUtente();
                }
            }
        });
    }

    private boolean controlloValidità() {
        username = editTextUsername.getText().toString();
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        confermaPassword = editTextConfermaPassword.getText().toString();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confermaPassword.isEmpty()) {
            Toast.makeText(getContext(), "Inserisci valori in tutti i campi", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(confermaPassword)) {
            Toast.makeText(getContext(), "Password e conferma password diversi", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private void loadRegistrazioneUtente() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);

        Utente utente = new Utente(username, email, password);

        Call<ResultRegistrazione> call = service.registrazioneUtente(
                utente.getUsername(),
                utente.getEmail(),
                utente.getPassword()
        );

        call.enqueue(new Callback<ResultRegistrazione>() {
            @Override
            public void onResponse(Call<ResultRegistrazione> call, Response<ResultRegistrazione> response) {
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResultRegistrazione> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registrazione, container, false);
    }
}
