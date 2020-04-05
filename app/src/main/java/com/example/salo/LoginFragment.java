package com.example.salo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.salo.account.AccountService;
import com.example.salo.account.JwtServiceHolder;
import com.example.salo.account.LoginDTO;
import com.example.salo.account.LoginDTOBadRequest;
import com.example.salo.account.TokenDTO;
import com.example.salo.prductview.ProductGridFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
        final TextInputEditText phoneEditText = view.findViewById(R.id.phone_edit_text);
        final TextView errorMessage = view.findViewById(R.id.error_message);
        MaterialButton btnLogin = view.findViewById(R.id.btnLogin);
        MaterialButton btnRegister = view.findViewById(R.id.btnRegister);

        // Set an error if the password is less than 8 characters.
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorMessage.setText("");
                if (!isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError("Пароль має бути мін 8 символів");
                } else {
                    passwordTextInput.setError(null); // Clear the error
                    String password = passwordEditText.getText().toString();

                    String login = phoneEditText.getText().toString();
                    LoginDTO loginDTO=new LoginDTO(login, password);
                    AccountService.getInstance()
                            .getJSONApi()
                            .loginRequest(loginDTO)
                            .enqueue(new Callback<TokenDTO>() {
                                @Override
                                public void onResponse(@NonNull Call<TokenDTO> call, @NonNull Response<TokenDTO> response) {
                                    if(response.isSuccessful()) {
                                        TokenDTO tokenDTO = response.body();
                                        ((JwtServiceHolder) getActivity()).SaveJWTToken(tokenDTO.getToken()); // Navigate to the register Fragment
                                        ((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), false); // Navigate to the products Fragment
                                        //Log.e(TAG,"*************GOOD Request***********"+ tokenDTO.getToken());
                                    }
                                    else {
                                        try {
                                            String json = response.errorBody().string();
                                            Gson gson = new Gson();
                                            LoginDTOBadRequest result = gson.fromJson(json, LoginDTOBadRequest.class);
                                            errorMessage.setText(result.getInvalid());
                                        }
                                        catch (Exception ex) {
                                            errorMessage.setText(ex.getMessage());
                                        }

                                        //Log.e(TAG,"_______________________"+response.errorBody().charStream());
                                    }


                                    //Log.d(TAG,tokenDTO.toString());
                                    //CommonUtils.hideLoading();
                                }

                                @Override
                                public void onFailure(@NonNull Call<TokenDTO> call, @NonNull Throwable t) {
                                    //CommonUtils.hideLoading();
                                    errorMessage.setText("У нас проблеми Хюстон");
                                    Log.e("ERROR","*************ERORR request***********");

                                    t.printStackTrace();
                                }
                            });




                    //((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), false); // Navigate to the products Fragment
                }
            }
        });

        // Set an error if the password is less than 8 characters.
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationHost) getActivity()).navigateTo(new RegisterFragment(), false); // Navigate to the register Fragment

            }
        });
        return view;
    }
    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }
}
