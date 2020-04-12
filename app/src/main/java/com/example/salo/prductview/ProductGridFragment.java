package com.example.salo.prductview;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.salo.NavigationHost;
import com.example.salo.R;
import com.example.salo.network.ProductEntry;
import com.example.salo.network.utils.CommonUtils;
import com.example.salo.prductview.click_listeners.OnDeleteListener;
import com.example.salo.prductview.click_listeners.OnEditListener;
import com.example.salo.prductview.network.dto.ProductDTO;
import com.example.salo.prductview.network.api.ProductDTOService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductGridFragment extends Fragment implements OnEditListener, OnDeleteListener {

    private RecyclerView recyclerView;
    Button addButton;

    List<ProductEntry> productEntryList;
    ProductCardRecyclerViewAdapter productAdapter;

    private static final String TAG = ProductGridFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_grid, container, false);

        setupViews(view);
        setRecyclerView();

        setButtonAddListener();
        loadProductEntryList();

        return view;
    }

    private void setupViews(View view) {
        addButton = view.findViewById(R.id.add_button);
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                GridLayoutManager.VERTICAL, true));

        productEntryList=new ArrayList<>();
        productAdapter = new ProductCardRecyclerViewAdapter(productEntryList, this, this);
        recyclerView.setAdapter(productAdapter);

        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);

        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
    }

    private void  setButtonAddListener() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost)getActivity()).navigateTo(new ProductCreateFragment(), false);
            }
        });
    }

    private void loadProductEntryList() {
        CommonUtils.showLoading(getActivity());
        ProductDTOService.getInstance()
                .getJSONApi()
                .getAllProducts()
                .enqueue(new Callback<List<ProductDTO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ProductDTO>> call, @NonNull Response<List<ProductDTO>> response) {
                        CommonUtils.hideLoading();
                        if(response.isSuccessful()) {
                            productEntryList.clear();
                            List<ProductDTO> list = response.body();

                            for (ProductDTO item : list) {
                                ProductEntry pe = new ProductEntry(item.getId(), item.getTitle(), item.getUrl(), item.getUrl(), item.getPrice(), "sdfasd");
                                productEntryList.add(pe);
                            }

                            productAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ProductDTO>> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        //CommonUtils.hideLoading();
                        Log.e("ERROR","*************ERORR request***********");
//                        if(t instanceof NoConnectivityException) {
//                            ((ConnectionInternetError) getActivity()).navigateErrorPage(new ProductGridFragment(), true); // Navigate to the next Fragment
//                            //Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
//                        }
                        t.printStackTrace();
                    }
                });
    }

    private void deleteConfirm(final ProductEntry productEntry) {
        CommonUtils.showLoading(getContext());
        ProductDTOService.getInstance()
                .getJSONApi()
                .DeleteRequest(productEntry.id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        CommonUtils.hideLoading();
                        if (response.isSuccessful()) {
                            productEntryList.remove(productEntry);
                            productAdapter.notifyDataSetChanged();
                        } else {
                            //  Log.e(TAG, "_______________________" + response.errorBody().charStream());

                            try {
//                                                String json = response.errorBody().string();
//                                                Gson gson  = new Gson();
//                                                ProductCreateInvalidDTO resultBad = gson.fromJson(json, ProductCreateInvalidDTO.class);
                                //Log.d(TAG,"++++++++++++++++++++++++++++++++"+response.errorBody().string());
                                //errormessage.setText(resultBad.getInvalid());
                            } catch (Exception e) {
                                //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        Log.e("ERROR", "*************ERORR request***********");
                        t.printStackTrace();

                    }
                });
    }


    @Override
    public void deleteItem(final ProductEntry productEntry) {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Видалення")
                .setMessage("Ви дійсно бажаєте видалити \"" + productEntry.title + "\"?")
                .setNegativeButton("Скасувати", null)
                .setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteConfirm(productEntry);
                        //Toast.makeText(getActivity(), "Delete "+ productEntry.id, Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }


    @Override
    public void editItem(ProductEntry productEntry, int index) {
        Toast.makeText(getActivity(), "Edit"+ productEntry.title, Toast.LENGTH_LONG).show();
    }
}

