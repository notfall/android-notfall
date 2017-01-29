package com.mayassin.android.notfall.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mayassin.android.notfall.R;

import java.util.ArrayList;

/**
 * Created by mohamed on 1/29/17.
 */

public class RecycleViewAdapterHelpers extends RecyclerView.Adapter<RecycleViewAdapterHelpers.CustomViewHolder> {
    private ArrayList<Object> allHelpers;
//    private PopUpInterface popUpInterface;
    public RecycleViewAdapterHelpers(ArrayList<Object> allCourses) {
        this.allHelpers = allCourses;
    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_helper, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

//    public void setPopUpInterface(PopUpInterface popUpInterface) {
//        this.popUpInterface = popUpInterface;
//    }

    @Override
    public void onBindViewHolder(RecycleViewAdapterHelpers.CustomViewHolder holder, int position) {
        Object helper = allHelpers.get(position);

//        holder.titleText.setText(course.title);
//        holder.crnText.setText(course.getCRNText());
    }

    @Override
    public int getItemCount() {
        return (null != allHelpers ? allHelpers.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView titleText;
        protected TextView crnText;
        // Info button, not actually a button to save memory but just as useful!
        protected ImageView nextButton;
        protected View mLayout;

        public CustomViewHolder(View view) {
            super(view);
//            this.titleText = (TextView) view.findViewById(R.id.title_text_view);
//            this.crnText = (TextView) view.findViewById(R.id.crn_text_view);
//            this.mLayout = view.findViewById(R.id.class_info_layout);
//            this.nextButton = (ImageView) view.findViewById(R.id.next_button_image_view);
            this.nextButton.setOnClickListener(nextButtonClickListener);
            this.mLayout.setOnClickListener(layoutOnClickListener);
        }

        private View.OnClickListener layoutOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object course = allHelpers.get(getAdapterPosition());
// locaterInterface.goToExhibit(allExhibits.get(getAdapterPosition()).getTitle());
//                popUpInterface.popUp(course);
            }
        };

        private View.OnClickListener nextButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object helper = allHelpers.get(getAdapterPosition());
//                locaterInterface.popUpBox(exhibit);
                // GO to ClassActivity
//                popUpInterface.goToCoursePage(course);
            }
        };
    }
}