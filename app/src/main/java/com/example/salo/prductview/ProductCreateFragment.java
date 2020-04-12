package com.example.salo.prductview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.salo.NavigationHost;
import com.example.salo.R;
import com.example.salo.network.utils.CommonUtils;
import com.example.salo.network.utils.FileUtil;
import com.example.salo.prductview.network.api.ProductDTOService;
import com.example.salo.prductview.network.dto.ProductCreateDTO;
import com.example.salo.prductview.network.dto.ProductCreateErrorDTO;
import com.example.salo.prductview.network.dto.ProductCreateResultDTO;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductCreateFragment extends Fragment {

    public static final int PICKFILE_RESULT_CODE = 1;

    Button addButton;
    TextInputLayout titleTextInput;
    TextInputEditText titleEditText;
    TextInputLayout priceTextInput;
    TextInputEditText priceEditText;
    TextView errormessage;

    Button btnSelectImage;
    ImageView chooseImage;
    String chooseImageBase64;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_create, container, false);

        //Привязка елементів
        setupViews(view);

        //Додати продукт
        setButtonAddListener();

        //Обрати фото для продукта
        setButtonSelectImageListener();


        return view;
    }

    private void setupViews(View view) {
        addButton = view.findViewById(R.id.add_button);

        titleTextInput = view.findViewById(R.id.product_title_input_text);
        titleEditText = view.findViewById(R.id.product_title_edit_text);
        priceTextInput = view.findViewById(R.id.price_text_input);
        priceEditText  = view.findViewById(R.id.price_edit_text);

        errormessage = view.findViewById(R.id.error_message);

        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        chooseImage = (ImageView) view.findViewById(R.id.chooseImage);
    }

    private void setButtonAddListener() {
        // Set an error if the password is less than 8 characters.
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  ((NavigationHost) getActivity()).navigateTo(new ProductCreateFragment(), false);

                // Toast.makeText(getActivity(), "Add ", Toast.LENGTH_SHORT).show();
                ProductCreateDTO productCreateDTO=new ProductCreateDTO(titleEditText.getText().toString(),
                        priceEditText.getText().toString(), chooseImageBase64);
                CommonUtils.showLoading(getActivity());
                ProductDTOService.getInstance()
                        .getJSONApi()
                        .CreateRequest(productCreateDTO)
                        .enqueue(new Callback<ProductCreateResultDTO>() {
                            @Override
                            public void onResponse(@NonNull Call<ProductCreateResultDTO> call, @NonNull Response<ProductCreateResultDTO> response) {
                                errormessage.setText("");
                                titleTextInput.setError(null);
                                priceTextInput.setError(null);
                                if (response.isSuccessful()) {
                                    ProductCreateResultDTO resultDTO = response.body();
                                    ((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), false); // Navigate to the products Fragment
                                    //  Log.e(TAG, "*************GOOD Request***********" + tokenDTO.getToken());
                                } else {
                                    //  Log.e(TAG, "_______________________" + response.errorBody().charStream());

                                    try {
                                        String json = response.errorBody().string();
                                        Gson gson  = new Gson();
                                        ProductCreateErrorDTO resultBad = gson.fromJson(json, ProductCreateErrorDTO.class);
                                        if(resultBad.getTitle() != null && !resultBad.getTitle().isEmpty()) {
                                            titleTextInput.setError(resultBad.getTitle());
                                        }
                                        if(resultBad.getPrice() != null && !resultBad.getPrice().isEmpty()) {
                                            priceTextInput.setError(resultBad.getPrice());
                                        }
                                        if(resultBad.getImageBase64() != null && !resultBad.getImageBase64().isEmpty()) {
                                            errormessage.setText(resultBad.getImageBase64());
                                        }
                                        if(resultBad.getInvalid() != null && !resultBad.getInvalid().isEmpty()) {
                                            errormessage.setText(resultBad.getInvalid());
                                        }
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
    }

    private void setButtonSelectImageListener() {
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/*");
                chooseFile = Intent.createChooser(chooseFile, "Оберіть малюнок продукта");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    Uri fileUri = data.getData();
                    try {
                        File imgFile = FileUtil.from(this.getActivity(), fileUri);
                        byte[] buffer = new byte[(int) imgFile.length() + 100];
                        int length = new FileInputStream(imgFile).read(buffer);
                        chooseImageBase64 = Base64.encodeToString(buffer, 0, length, Base64.NO_WRAP);
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        chooseImage.setImageBitmap(myBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }
}
