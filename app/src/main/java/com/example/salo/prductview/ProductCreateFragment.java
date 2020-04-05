package com.example.salo.prductview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.salo.NavigationHost;
import com.example.salo.R;
import com.example.salo.network.utils.CommonUtils;
import com.example.salo.prductview.network.api.ProductDTOService;
import com.example.salo.prductview.network.dto.ProductCreateDTO;
import com.example.salo.prductview.network.dto.ProductCreateErrorDTO;
import com.example.salo.prductview.network.dto.ProductCreateResultDTO;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductCreateFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_create, container, false);

        Button addButton = view.findViewById(R.id.add_button);
        final TextInputEditText titleEditText = view.findViewById(R.id.product_title_edit_text);
        final TextInputEditText priceEditText = view.findViewById(R.id.price_edit_text);
        final TextView errormessage = view.findViewById(R.id.error_message);

        // Set an error if the password is less than 8 characters.
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  ((NavigationHost) getActivity()).navigateTo(new ProductCreateFragment(), false);
                errormessage.setText("");
                // Toast.makeText(getActivity(), "Add ", Toast.LENGTH_SHORT).show();
                ProductCreateDTO productCreateDTO=new ProductCreateDTO(titleEditText.getText().toString(),
                        priceEditText.getText().toString());
                CommonUtils.showLoading(getActivity());
                ProductDTOService.getInstance()
                        .getJSONApi()
                        .CreateRequest(productCreateDTO)
                        .enqueue(new Callback<ProductCreateResultDTO>() {
                            @Override
                            public void onResponse(@NonNull Call<ProductCreateResultDTO> call, @NonNull Response<ProductCreateResultDTO> response) {

                                if (response.isSuccessful()) {
                                    ProductCreateResultDTO resultDTO = response.body();
                                    ((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), true); // Navigate to the products Fragment
                                    //  Log.e(TAG, "*************GOOD Request***********" + tokenDTO.getToken());
                                } else {
                                    //  Log.e(TAG, "_______________________" + response.errorBody().charStream());

                                    try {
                                        String json = response.errorBody().string();
                                        Gson gson  = new Gson();
                                        ProductCreateErrorDTO resultBad = gson.fromJson(json, ProductCreateErrorDTO.class);
                                        //Log.d(TAG,"++++++++++++++++++++++++++++++++"+response.errorBody().string());
                                        errormessage.setText(resultBad.getInvalid());
                                    } catch (Exception e) {
                                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                                CommonUtils.hideLoading();

                                //Log.d(TAG,tokenDTO.toString());
                                //CommonUtils.hideLoading();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ProductCreateResultDTO> call, @NonNull Throwable t) {
                                //CommonUtils.hideLoading();
                                Log.e("ERROR","*************ERORR request***********");
                                t.printStackTrace();
                                CommonUtils.hideLoading();
                            }
                        });

            }
        });


        return view;
    }
}
