package com.example.salo.prductview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.salo.R;
import com.example.salo.network.ProductEntry;
import com.example.salo.retrofitProduct.ProductDTO;
import com.example.salo.retrofitProduct.ProductDTOService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductGridFragment extends Fragment {

    private RecyclerView recyclerView;
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
        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                GridLayoutManager.VERTICAL, false));

//        List<ProductEntry> list = ProductEntry.initProductEntryList(getResources());
//        ProductCardRecyclerViewAdapter adapter = new ProductCardRecyclerViewAdapter(list);
//
//        recyclerView.setAdapter(adapter);

        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);

        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

        ProductDTOService.getInstance()
                .getJSONApi()
                .getAllProducts()
                .enqueue(new Callback<List<ProductDTO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ProductDTO>> call, @NonNull Response<List<ProductDTO>> response) {
                        List<ProductDTO> list = response.body();
                        //int size = list.size();
                        //String res= list.get(0).toString();
                        //Log.d(TAG, "--------result server-------"+res);

                        List<ProductEntry> newlist = new ArrayList<ProductEntry>();//ProductEntry.initProductEntryList(getResources());
                        for (ProductDTO item : list) {
                            ProductEntry pe=new ProductEntry(item.getTitle(),item.getUrl(),item.getUrl(), item.getPrice(),"sdfasd");
                            newlist.add(pe);
                        }
                        ProductCardRecyclerViewAdapter newAdapter = new ProductCardRecyclerViewAdapter(newlist);
                        recyclerView.swapAdapter(newAdapter, false);
                        //CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ProductDTO>> call, @NonNull Throwable t) {
                        //CommonUtils.hideLoading();
                        Log.e("ERROR","*************ERORR request***********");
//                        if(t instanceof NoConnectivityException) {
//                            ((ConnectionInternetError) getActivity()).navigateErrorPage(new ProductGridFragment(), true); // Navigate to the next Fragment
//                            //Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
//                        }
                        t.printStackTrace();
                    }
                });


        return view;
    }
}
