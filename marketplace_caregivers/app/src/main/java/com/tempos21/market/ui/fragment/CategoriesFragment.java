package com.tempos21.market.ui.fragment;

//import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tempos21.market.client.bean.Categories;
import com.tempos21.market.client.bean.Category;
import com.tempos21.market.client.service.impl.GetCategoriesService.OnGetCategoriesServiceResponseListener;
import com.tempos21.market.client.service.impl.ServiceErrorCodes;
import com.tempos21.market.device.Connection;
import com.tempos21.market.ui.StartUpActivity;
import com.tempos21.market.ui.adapter.CategoriesAdapter;
import com.tempos21.market.util.MarketPlaceHelper;
import com.worldline.alfredo.R;

import eu.alfred.api.market.responses.category.CategoryList;
import eu.alfred.api.market.responses.listener.GetCategoryListResponseListener;
import eu.alfred.ui.AppActivity;

//@SuppressLint({ "ValidFragment", "ValidFragment" })
public class CategoriesFragment extends Fragment implements OnItemClickListener {
    private View fragmentView;
    private ListView categoriesList;
    private Categories categories;
    private ProgressBar categoriesProgress;
    private TextView notCategories;
    private AppActivity context;



    public void setContext(AppActivity c) {
        context = c;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.categories, null, true);

        findViews();
        setListeners();

        if (Connection.netConnect(context)) {
            notCategories.setVisibility(View.INVISIBLE);
            getCategories();
        } else {
            categoriesProgress.setVisibility(View.INVISIBLE);
            categoriesList.setVisibility(View.INVISIBLE);
            notCategories.setVisibility(View.VISIBLE);
            notCategories.setText(R.string.not_connection);
        }

        return fragmentView;
    }


    private void findViews() {
        categoriesList = (ListView) fragmentView.findViewById(R.id.categoriesList);
        categoriesProgress = (ProgressBar) fragmentView.findViewById(R.id.categoryProgress);
        notCategories = (TextView) fragmentView.findViewById(R.id.notCategories);
    }


    private void setListeners() {
        categoriesList.setOnItemClickListener(this);
    }


    private void getCategories() {
        MarketPlaceHelper.getInstance().marketPlace.getCategoryList(new GetCategoryListResponseListener() {
            @Override
            public void onSuccess(CategoryList categoryList) {
                Categories categories = new Categories();
                Category category;
                for (eu.alfred.api.market.responses.category.Category category1 : categoryList.categories) {
                    category = new Category();
                    category.setId("" + (category1.id != null ? category1.id : -1));
                    category.setName(category1.name);
                    categories.add(category);
                }
                onGetCategoriesResponse(true, categories);
            }

            @Override
            public void onError(Exception e) {
                onGetCategoriesResponse(false, new Categories());
            }
        });
        // TODO
        /*
        GetCategoriesService service = new GetCategoriesService(Constants.GETCATEGORIES_SERVICE, getActivity());
        service.setOnGetCategoriesServiceResponseListener(this);
        service.runService();
        */
    }


    public void onGetCategoriesResponse(boolean success, Categories categories) {
        if (!success) {
            Intent intent = new Intent(context, StartUpActivity.class);
            intent.putExtra(HomeFragment.LOGIN_ERROR, true);
            startActivity(intent);
            context.finish();
        } else {
            this.categories = categories;
            loadAdapter();
            if (categories.size() < 1) {
                categoriesList.setVisibility(View.INVISIBLE);
                if (success) {
                    notCategories.setVisibility(View.VISIBLE);
                } else {
                    notCategories.setVisibility(View.VISIBLE);
                    notCategories.setText(R.string.server_error);
                }

            } else {
                notCategories.setVisibility(View.GONE);
            }
            categoriesProgress.setVisibility(View.GONE);
        }
    }


    private void loadAdapter() {
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(context, categories);
        categoriesList.setAdapter(categoriesAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Category category = categories.get(position);
        FragmentManager fm = this.getFragmentManager();
        ListAppsFragment listApps = new ListAppsFragment(getActivity());
        listApps.setCategoryId(category.getId());
        listApps.setTitle(category.getName());
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        transaction.addToBackStack(null);
        transaction.replace(R.id.mainFragment, listApps);
        transaction.commit();
    }

}
