package com.example.salo.prductview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.toolbox.NetworkImageView;
import com.example.salo.network.ImageRequester;
import com.example.salo.network.utils.CommonUtils;
import com.example.salo.network.utils.FileUtil;
import com.example.salo.prductview.network.api.ProductDTOService;
import com.example.salo.prductview.network.dto.ProductEditDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.salo.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductEditActivity extends AppCompatActivity {

    public static final int PICKFILE_RESULT_CODE = 1;

    private EditText editTextTitle;
    private EditText editTextPrice;
    private NetworkImageView editImage;
    private Button buttonSave;
    private Button buttonCancel;

    private int id;

    private ImageRequester imageRequester;
    private Button btnSelectImage;
    private String chooseImageBase64;
    private ProductEditDTO productEditDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);
        imageRequester = ImageRequester.getInstance();
        setupViews();
        initProduct();
        setButtonCancelListener();
        setButtonSaveListener();
        setbtnSelectImageListener();
    }

    private void setupViews() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextPrice = findViewById(R.id.editTextPrice);
        editImage = findViewById(R.id.editImage);
        buttonSave = findViewById(R.id.buttonEdit);
        buttonCancel = findViewById(R.id.buttonCancel);
        btnSelectImage = findViewById(R.id.btnSelectImage);
    }

    private void initProduct() {
        Intent intent = getIntent();
        if(intent != null){
            id = intent.getIntExtra(Constants.PRODUCT_INTENT_ID,0);
            //Toast.makeText(this, "Hello"+id, Toast.LENGTH_LONG);
        }
        CommonUtils.showLoading(this);
        ProductDTOService.getInstance()
                .getJSONApi()
                .getEditProduct(id)
                .enqueue(new Callback<ProductEditDTO>() {
                    @Override
                    public void onResponse(Call<ProductEditDTO> call, Response<ProductEditDTO> response) {
                        CommonUtils.hideLoading();
                        if (response.isSuccessful()) {
                            productEditDTO = response.body();
                            editTextTitle.setText(productEditDTO.getTitle());
                            editTextPrice.setText(productEditDTO.getPrice());
                            imageRequester.setImageFromUrl(editImage, productEditDTO.getUrl());
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<ProductEditDTO> call, Throwable t) {
                        CommonUtils.hideLoading();
                        Log.e("ERROR", "*************ERORR request***********");
                        t.printStackTrace();
                    }
                });
    }

    private void setButtonCancelListener() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void setButtonSaveListener() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString().trim();
                String price = editTextPrice.getText().toString().trim();
                String base64 = chooseImageBase64;
                productEditDTO.setTitle(title);
                productEditDTO.setPrice(price);
                productEditDTO.setImageBase64(base64);

                ProductDTOService.getInstance()
                        .getJSONApi()
                        .editProduct(productEditDTO)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                                CommonUtils.hideLoading();
                                if (response.isSuccessful()) {
                                    setResult(RESULT_OK);
                                    finish();
                                    //(NavigationHost) currentFragment.getActivity()).navigateTo(new ProductGridFragment(), false); // Navigate to the products Fragment
                                    //  Log.e(TAG, "*************GOOD Request***********" + tokenDTO.getToken());
                                }


                                //Log.d(TAG,tokenDTO.toString());
                                //CommonUtils.hideLoading();
                            }

                            @Override
                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                //CommonUtils.hideLoading();
                                Log.e("ERROR", "*************ERORR request***********");
                                t.printStackTrace();
                                CommonUtils.hideLoading();
                            }
                        });


            }
        });
    }

    private void setbtnSelectImageListener() {
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/*");
                chooseFile = Intent.createChooser(chooseFile, "Оберіть фото");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    Uri fileUri = data.getData();
                    try {
                        File imgFile = FileUtil.from(this, fileUri);
                        byte[] buffer = new byte[(int) imgFile.length() + 100];
                        int length = new FileInputStream(imgFile).read(buffer);
                        chooseImageBase64 = Base64.encodeToString(buffer, 0, length, Base64.NO_WRAP);
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        editImage.setImageBitmap(myBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }


}
