package com.tempos21.market.ui.view.listApps;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.tempos21.mymarket.sdk.model.app.App;
import com.worldline.alfredo.R;

import java.util.List;


/***
 * This views shows the Apps provided in an ArrayList. IT can handle Async load and infinite list
 * 
 * @author Sergi Martinez
 * 
 */
public class ListAppsView extends FrameLayout implements OnScrollListener, OnItemClickListener {

	
	
	/***
	 * These constants manage the status of the list.
	 * STATUS_FIRST_LOAD: this is the initial status. No items have been loaded. Progress bar
	 * 					  is moving.
	 * STATUS_SHOWING: the list is showing n elements and no load process is being performed
	 * STATUS_LOAD: the list is waiting for more items to add at the end. Once added, it will 
	 *              return to STATUS_SHOWING
	 * STATUS_FINISHED: the view have been notified as no more items should be loaded and is showing the
	 *                  whole list.
	 */
	private static final int STATUS_FIRST_LOAD = 0;
	private static final int STATUS_SHOWING = 1;
	private static final int STATUS_LOAD = 2;
	private static final int STATUS_FINISHED = 3;
	
	// Number of columns to show according to the list shape and screen size
//	private static final int LIST_COLUMNS_TABLET = 3;
//	private static final int LIST_COLUMNS_PHONE = 1;
//	private static final int GRID_COLUMNS_TABLET = 5;
//	private static final int GRID_COLUMNS_PHONE = 3;
	
	/**
	 * This is the listener responsible to warn when the list have been pulled to the end and more 
	 * apps should be loaded
	 */
	private OnLoadingStatusListener onLoadingStatusListener;
	

	private Context context;
	private List<App> apps;
	private View v;
	private GridView appsGrid;
	private AppsAdapter ad;
	private int status = STATUS_FIRST_LOAD;
	private ProgressBar appsProgress;
	private OnItemClickListener onItemClickListener;

	
	
	public ListAppsView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public ListAppsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public ListAppsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public OnLoadingStatusListener getOnLoadingStatusListener() {
		return onLoadingStatusListener;
	}

	public void setOnLoadingStatusListener(
			OnLoadingStatusListener onLoadingStatusListener) {
		this.onLoadingStatusListener = onLoadingStatusListener;
	}

	public int getStatus() {
		return status;
	}

	/**
	 * Generates the view and initializes items and listeners
	 */
	private void init() {
		if (!isInEditMode()) {
			v = View.inflate(context, R.layout.list_apps, null);
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			v.setLayoutParams(params);
			this.addView(v);
			findViews();
			setStatus(STATUS_FIRST_LOAD);
			setListeners();
			
		}
	}

	private void findViews() {
		this.appsGrid = (GridView) findViewById(R.id.appsGrid);
		this.appsProgress = (ProgressBar) findViewById(R.id.appsProgress);

	}

	private void setListeners() {
		appsGrid.setOnScrollListener(this);
		appsGrid.setOnItemClickListener(this);
	}
	public List<App> getApps() {
		return apps;
	}

	
	/**
	 * Once the list of apps is set, we change the status from first loading to showing.
	 * @param apps the apps to show
	 */
	public void setApps(List<App> apps) {
		this.apps = apps;
		setStatus(STATUS_SHOWING);

	}

	/**
	 * We are adding more items to the list of apps. Normally we come from LOAD status
	 * @param apps
	 */
	public void addApps(List<App> apps) {
		int firstPosition=appsGrid.getFirstVisiblePosition();
		if (this.apps==null) {
			this.apps=apps;
		}else{
		
			this.apps.addAll(apps);
		}	
		setStatus(STATUS_SHOWING);
		appsGrid.setSelection(firstPosition+getColumns());
	
		
	}
	
	/**
	 * Changes the view from grid to list and viceversa
	 */
	public void swapMode() {
		setData();
	}
	
	public void setMode() {
		setData();
	}

	/**
	 * Reloads the list in order to destroy the viewholders and change the view mode
	 * It also stablished the number of columns
	 */
	private void setData() {
		if (status != STATUS_FIRST_LOAD && apps!=null && apps.size()>=0) {
			ad = new AppsAdapter(context, apps);
			appsGrid.setAdapter(ad);
			appsGrid.setNumColumns(getColumns());
            appsGrid.setHorizontalSpacing(30);
            appsGrid.setVerticalSpacing(30);
		}
	}

	/**
	 * Calculates the number of columns to show according to format and screen 
	 * @return number of columns to show in the grid.
	 */
	private int getColumns() {
		return context.getResources().getInteger(R.integer.grid_columns);
	}

	/**
	 * Changes the view elements according to the new status
	 * @param newStatus The new status to stablish
	 */
	private void setStatus(int newStatus) {
		if (status == newStatus) {
			return;
		}
		status=newStatus;
		// We are waiting for initial load, just show the progress bar
		if (status == STATUS_FIRST_LOAD) { 
			appsProgress.setVisibility(VISIBLE);
			appsGrid.setVisibility(INVISIBLE);
		}
		// We received data, just show the list
		if (status == STATUS_SHOWING) {
			appsProgress.setVisibility(INVISIBLE);
			appsGrid.setVisibility(VISIBLE);
			setData();
		}
		// We are waiting for more data
		if (status == STATUS_LOAD) {
			appsProgress.setVisibility(VISIBLE);
			appsGrid.setVisibility(VISIBLE);
		}
		
		if (status== STATUS_FINISHED) {
			appsProgress.setVisibility(INVISIBLE);
			appsGrid.setVisibility(VISIBLE);
		}
	}

	/** 
	 * Here we control if the list is scrolled to the end in order to ask for more items
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisible, int visibleCount, int totalCount) {
		boolean loadMore = firstVisible + visibleCount >= totalCount;
		if (status==STATUS_SHOWING && loadMore && onLoadingStatusListener!=null) {
			// We arrive here if three conditions are met:
			// 1. We are not loading anything and end of list has not been notified
			// 2. We are at the end of the list
			// 3. There is anybody listening to our petition
			setStatus(STATUS_LOAD); // we are loading
			onLoadingStatusListener.onLoadingStatus(totalCount); //let's ask for more data
		}			
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}
	
	/**
	 * This interface should be implemented for the item who wants to receive our petition for more data
	 * @author Sergi Martinez
	 *
	 */
	public interface OnLoadingStatusListener {
		/**
		 * The callback itself
		 * @param itemCount the numbers of items we have at the moment in order to load the next ones 
		 */
		public void onLoadingStatus(int itemCount);
	}
	
	public interface OnItemClickListener {
		public void   onItemClick(App app);
	}

	public void setFinished() {
		setStatus(STATUS_FINISHED);
		
	
	}

	@Override
	public void   onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (onItemClickListener!=null) {
			App app=apps.get(position);
			getOnItemClickListener().onItemClick(app);
		}
	}

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}



}
