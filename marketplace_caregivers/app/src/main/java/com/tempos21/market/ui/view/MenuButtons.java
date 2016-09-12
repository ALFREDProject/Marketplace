package com.tempos21.market.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tempos21.market.util.TLog;
import com.worldline.alfredo.R;


public class MenuButtons extends LinearLayout implements OnClickListener, OnKeyListener {

    public static final int SEARCH = 0;
    public static final int HOME = 1;
    public static final int LATEST = 2;
    public static final int CATEGORIES = 3;
    public static final int COUNTRIES = 4;
    public static final int MYAPPS = 5;
    public static final int UPDATED = 6;
    public static final int TESTING = 7;
//	public static final int STATISTICS=8;

    private static final int NUM_BUTTONS = 8;
    //	private View menuShadow;
//	private View rightBoder;
    private static boolean first = true;
    private Context context;
    private View menu;
    private View selectedArrow;
    private LinearLayout[] buttons;
    private FoldingListener foldingListener;
    private int selectedButton = HOME;
    private OnMenuClickListener onMenuClickListener;
    private onSearchListener onSearchListener;
    private boolean isTester = false;
    //	private boolean isDeveloper=false;
    private View doSearch;
    private View doSearchLayout;
    private EditText doSearchText;
    private boolean update;
    private SlidingMenu sliding;
    public MenuButtons(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    public MenuButtons(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public OnMenuClickListener getOnMenuClickListener() {
        return onMenuClickListener;
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    private void init() {
        menu = LinearLayout.inflate(context, R.layout.mainmenu, this);
        //this.addView(menu);
        //this.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        findViews();
        setListeners();
        selectButton();
        setupTester();
//		setupDeveloper();

    }

    private void findViews() {
        this.buttons = new LinearLayout[NUM_BUTTONS];
        doSearch = menu.findViewById(R.id.ic_doSearch);
        doSearchText = (EditText) findViewById(R.id.doSearchText);
        doSearchLayout = findViewById(R.id.doSearchLayout);
        buttons[SEARCH] = (LinearLayout) menu.findViewById(R.id.searchLayout);
        buttons[HOME] = (LinearLayout) menu.findViewById(R.id.homeLayout);
        buttons[LATEST] = (LinearLayout) menu.findViewById(R.id.latestLayout);
        buttons[CATEGORIES] = (LinearLayout) menu.findViewById(R.id.categoriesLayout);
        buttons[COUNTRIES] = (LinearLayout) menu.findViewById(R.id.countriesLayout);
        buttons[MYAPPS] = (LinearLayout) menu.findViewById(R.id.myAppsLayout);
        buttons[UPDATED] = (LinearLayout) menu.findViewById(R.id.updatedLayout);
        buttons[TESTING] = (LinearLayout) menu.findViewById(R.id.testingSubLayout);
//		buttons[STATISTICS]=(LinearLayout) menu.findViewById(R.id.statisticsLayout);
        selectedArrow = menu.findViewById(R.id.selectedArrow);
//		menuShadow=menu.findViewById(R.id.menuShadow);
//		rightBoder=menu.findViewById(R.id.rightBorder);
    }

    private void setListeners() {
        for (int i = 0; i < NUM_BUTTONS; i++) {
            buttons[i].setOnClickListener(this);
        }
        doSearch.setOnClickListener(this);
        doSearchText.setOnKeyListener(this);
    }

    private void setupTester() {
        if (isTester) {
            buttons[TESTING].setVisibility(View.VISIBLE);
        } else {
            buttons[TESTING].setVisibility(View.GONE);
        }
    }

//	private void setupDeveloper() {
//		if (isDeveloper) {
//			buttons[STATISTICS].setVisibility(View.VISIBLE);
//		}else{
//			buttons[STATISTICS].setVisibility(View.GONE);
//		}
//	}

    public void clickUpdate() {
        update = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (update) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) selectedArrow.getLayoutParams();
            params.topMargin = buttons[UPDATED].getTop();
            selectedArrow.setLayoutParams(params);

        }
    }


    public void setSlidingMode(SlidingMenu sliding) {
        this.sliding = sliding;
    }


    @Override
    public void onClick(View v) {
        update = false;
        if (v.getId() == buttons[SEARCH].getId()) {
            showDoSearch();
            selectedButton = 0;
            sliding.setMode(SlidingMenu.EXPANDED);
        }
        if (v.getId() == doSearch.getId()) {
            hideDoSearch();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) selectedArrow.getLayoutParams();
            params.topMargin = buttons[0].getTop();
            selectedButton = 0;
            if (onSearchListener != null && !doSearchText.getText().toString().equals("")) {
                onSearchListener.onSearch(doSearchText.getText().toString());
            }
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(doSearchText.getWindowToken(), 0);
        }
        for (int i = 1; i < NUM_BUTTONS; i++) {
            if (buttons[i].getId() == v.getId()) {
                if (this.onMenuClickListener != null) {
                    onMenuClickListener.onMenuClick(i);
                }
                selectedButton = i;
            }
        }
        selectButton();

    }

    public void selectButton() {
        for (int i = 1; i < NUM_BUTTONS; i++) {
            if (i == selectedButton) {
                //buttons[i].setEnabled(false);
                buttons[i].setEnabled(true);
            } else {
                buttons[i].setEnabled(true);
            }
        }
        if (!first) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) selectedArrow.getLayoutParams();
            params.topMargin = buttons[selectedButton].getTop();
            selectedArrow.setLayoutParams(params);
            TLog.i("Top: " + params.topMargin);
        } else {

        }
        first = false;
    }

    public boolean isTester() {
        return isTester;
    }

    public void setTester(boolean isTester) {
        this.isTester = isTester;
        setupTester();
    }

//	public boolean isDeveloper() {
//		return isDeveloper;
//	}
//	public void setDeveloper(boolean isDeveloper) {
//		this.isDeveloper = isDeveloper;
//		setupDeveloper();
//	}

    public void hideDoSearch() {
        buttons[SEARCH].setVisibility(View.VISIBLE);
        doSearchLayout.setVisibility(View.GONE);
        doSearchText.setEnabled(false);
        doSearchText.setFocusableInTouchMode(false);

        doSearchText.setFocusable(false);

        if (foldingListener != null) {
            foldingListener.onFolding();
        }

    }

    public void showDoSearch() {
        buttons[SEARCH].setVisibility(View.GONE);
        doSearchLayout.setVisibility(View.VISIBLE);
        doSearchText.setEnabled(true);
        doSearchText.setFocusable(true);
        doSearchText.setFocusableInTouchMode(true);
        doSearchText.requestFocus();
        if (foldingListener != null) {
            foldingListener.onUnfolding();
        }
    }

    public onSearchListener getOnSearchListener() {
        return onSearchListener;
    }

    public void setOnSearchListener(onSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    public void selectedUpdates() {
        onClick(buttons[UPDATED]);
    }

    public FoldingListener getFoldingListener() {
        return foldingListener;
    }

    public void setFoldingListener(FoldingListener foldingListener) {
        this.foldingListener = foldingListener;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            hideDoSearch();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) selectedArrow.getLayoutParams();
            params.topMargin = buttons[0].getTop();
            selectedButton = 0;
            if (onSearchListener != null && !doSearchText.getText().toString().equals("")) {
                onSearchListener.onSearch(doSearchText.getText().toString());
            }
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(doSearchText.getWindowToken(), 0);

            return true;
        }
        return false;
    }

    public interface OnMenuClickListener {
        public void onMenuClick(int option);
    }

    public interface onSearchListener {
        public void onSearch(String query);
    }

    public interface FoldingListener {
        public void onFolding();

        public void onUnfolding();
    }
}
