package com.tempos21.market.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import com.tempos21.market.Constants;
import com.tempos21.market.db.CategoryModel;
import com.tempos21.market.util.Util;
import com.tempos21.market.ui.LoginActivity;
import com.tempos21.market.ui.presenter.CategoryListPresenterImpl;
import com.tempos21.market.ui.view.GenericView;
import com.tempos21.market.ui.view.CategoriesRowView;
import com.tempos21.mymarket.sdk.model.Category;
import com.worldline.alfredo.R;

import java.util.List;

/**
 * Created by a576023 on 06/03/2015.
 */
public class CategoryFragment extends Fragment implements View.OnClickListener, GenericView<List<Category>> {

    private boolean isOdd = false;
    private LayoutInflater inflater;
    private ScrollView scrollViewCategories;
    private TextView notCategories;
    private ProgressBar categoriesProgress;
    private View v;
    private int numCategories = 0;
    private String[] categoriesIcon;
    private String[] categoriesName;
    private CategoryListPresenterImpl presenter;
    private List<Category> categoriesList;
    private CategoryModel categoryModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.categories_fragment_view, null, true);

        this.inflater = inflater;

        categoryModel = new CategoryModel(getActivity());
        categoriesIcon = getResources().getStringArray(R.array.categories);
        categoriesName = getResources().getStringArray(R.array.categories_names);

        findViews();

        if (Util.netConnect(getActivity())) {
            notCategories.setVisibility(View.INVISIBLE);
            scrollViewCategories.setVisibility(View.VISIBLE);
            getCategories();
        } else {
            if(categoryModel.isEmpty()){
                notCategories.setVisibility(View.VISIBLE);
                scrollViewCategories.setVisibility(View.INVISIBLE);
                hideProgress();
            }else {
                categoriesList = categoryModel.getCategories();

                onViewSuccess(categoriesList);

                notCategories.setVisibility(View.INVISIBLE);
                scrollViewCategories.setVisibility(View.VISIBLE);
                hideProgress();
            }
        }

        return v;
    }


    private void findViews(){
        scrollViewCategories = (ScrollView) v.findViewById(R.id.scrollViewCategories);
        notCategories = (TextView)v.findViewById(R.id.notCategories);
        categoriesProgress = (ProgressBar) v.findViewById(R.id.categoryProgress);
    }


    private void getCategories(){
        presenter = new CategoryListPresenterImpl(this);
        presenter.getCategoryList();
    }


    @Override
    public void showProgress() {
        categoriesProgress.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideProgress() {
        categoriesProgress.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onViewSuccess(List<Category> list) {
        numCategories = 0;
        categoriesList = list;

        if(list.size() < 1){
            scrollViewCategories.setVisibility(View.INVISIBLE);
            notCategories.setVisibility(View.VISIBLE);
        }else{
            notCategories.setVisibility(View.GONE);
            if(getActivity()!=null) {
                createCategoriesList(list.size());
                saveInBD(list);
            }
        }
        categoriesProgress.setVisibility(View.GONE);
    }


    private void saveInBD(List<Category> list){
        for(Category category: list) {
            if(categoryModel.isOnDB(category)) {
                categoryModel.deleteCategory(category);
                categoryModel.insertCategory(category);
            }else{
                categoryModel.insertCategory(category);
            }
        }
    }


    @Override
    public void onViewError(long id, Exception e) {
        if(e.getMessage() != null && e.getMessage().equalsIgnoreCase(Constants.WRONG_CREDENTIALS)){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }else {
            notCategories.setVisibility(View.VISIBLE);
            notCategories.setText(R.string.server_error);
        }
    }


    private void createCategoriesList(int categories){
        LinearLayout categoriesLayout = (LinearLayout) v.findViewById(R.id.categoriesLayout);

        // If the categories number is odd, we'll add one row with an only element
        if(categories % 2 != 0){
            categories++;
            isOdd = true;
        }

        for(int rows = categories / 2; rows > 0; rows--){
            categoriesLayout.addView(createCategoriesRow(rows == 1));
        }
    }


    private View createCategoriesRow(boolean lastRow){
        CategoriesRowView categoriesRowView = new CategoriesRowView(getActivity());

        boolean addInvisibleView = false;
        if(isOdd && lastRow){
            addInvisibleView = true;
        }

        for(int category = 0; category < 2; category++) {
            categoriesRowView.addView(createCategory(addInvisibleView && category == 1));
        }

        return categoriesRowView;
    }


    private View createCategory(boolean lastCategory){
        View category = inflater.inflate(R.layout.category_view, null, true);
        category.setOnClickListener(this);
        category.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.5f));

        if(lastCategory){
            category.setVisibility(View.INVISIBLE);
        }

        setCategoryInfo(category);

        return category;
    }


    private View setCategoryInfo(View category){
        Category cat = categoriesList.get(numCategories);
        ((TextView) category.findViewById(R.id.categoryName)).setText(findCategoryName(""+cat.id));
        ((ImageView) category.findViewById(R.id.categoryImage)).setImageResource(findCategoryImage("" + cat.id));
        category.setTag(new Integer("" + cat.id));

        numCategories++;
        return category;
    }


    private String findCategoryName(String id){
        //return cat.getName(); When the changes are applied in server.
        return categoriesName[new Integer(id).intValue()];
    }


    private int findCategoryImage(String id){
        String cat = categoriesIcon[new Integer(id).intValue()];
        return getActivity().getResources().getIdentifier(cat, "drawable", getActivity().getPackageName());
    }


    @Override
    public void onClick(View view) {
        int categoryId = ((Integer)view.getTag()).intValue();

        ListAppsByCategoryFragment listApps = new ListAppsByCategoryFragment();
        listApps.setCategoryId(""+categoryId);
        ((MainActivityFragment)getActivity()).replaceFragment(R.id.bottomFragment, listApps, true);
    }

}
