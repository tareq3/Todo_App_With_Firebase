/*
 * Created by Tareq Islam on 6/22/18 10:52 PM
 *
 *  Last modified 6/22/18 10:52 PM
 */

package com.mti.todo_app_with_firebase.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mti.todo_app_with_firebase.MainActivity;
import com.mti.todo_app_with_firebase.R;
import com.mti.todo_app_with_firebase.model.ToDo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * Created by Tareq on 22,June,2018.
 */

//Todo: this interface will create channel from viewHolder to Adapter
interface ItemClickListener {

    void onClick(View view, int position, boolean isLongClick);
}

//Todo: this viewHolder holds Ui
 class ListItemViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

    private ItemClickListener mItemClickListener;
    TextView item_title, item_description;


    public ListItemViewHolder(View itemView) {
        super(itemView);

        item_title = itemView.findViewById(R.id.item_Title);
        item_description = itemView.findViewById(R.id.item_Description);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    //Todo: Need setItemClickListener to use onClick funtion with holder object
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener=itemClickListener;
    }

    @Override
    public void onClick(View v) {
        mItemClickListener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action");
        menu.add(0, 0, getAdapterPosition(), "DELETE");
    }

}


 public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder> implements Filterable {


     private Context mContext;

     private List<ToDo> mToDoListFiltered;
     private List<ToDo> mToDoList;

      private static ItemClickDataChannel mItemClickDataChannel; //this thing must be static





     public ListItemAdapter(Context context) {
         mContext = context;
      }

      public void updateAdapter(List<ToDo> toDoList){
          mToDoListFiltered = toDoList;
          mToDoList=toDoList;
          notifyDataSetChanged();
      }

      //optional
     public ListItemAdapter(Context context, List<ToDo> toDoList) {
         mContext = context;
         mToDoListFiltered = toDoList;
         mToDoList=toDoList;

     }


     public void setItemClickDataChannel(ItemClickDataChannel itemClickDataChannel){
         mItemClickDataChannel=itemClickDataChannel;
     }

     @NonNull
        @Override
        public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         LayoutInflater inflater=LayoutInflater.from(mContext);

         View view=inflater.inflate(R.layout.list_item,parent,false);


            return new ListItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
            //Todo: Load Data

            //Set data for item
            holder.item_title.setText(mToDoListFiltered.get(position).getTitle());
            holder.item_description.setText(mToDoListFiltered.get(position).getDescription());


            //we can sh
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    //when user select item, data will auto set for text view
                    if (!isLongClick){ //if not using long click

                        //Todo: Pass the data using ItemClickDataChannel
                        mItemClickDataChannel.onItemClickPassData(new ArrayList<Object>(Arrays.asList(
                                mToDoListFiltered.get(position).getTitle(),
                                mToDoListFiltered.get(position).getDescription()
                        )));

                     }
                }


            });


        }

        @Override
        public int getItemCount() {
            return mToDoListFiltered.size();
        }

     //TODO: filtering CODE Below

     @Override
     public Filter getFilter() {
         return new Filter() {
             @Override
             protected FilterResults performFiltering(CharSequence constraint) {
                 FilterResults filterResults=new FilterResults();
                 // If the constraint (search string/pattern) is null
                 // or its length is 0, i.e., its empty then
                 // we just set the `values` property to the
                 // original contacts list which contains all of them


                 //constraint is the text we have written on searchbox
                 String charString=constraint.toString();
                 if(charString.isEmpty() || constraint==null || constraint.length()==0){
                     mToDoListFiltered=mToDoList;
                     filterResults.values=mToDoListFiltered;
                     filterResults.count=mToDoListFiltered.size();

                 }else{
                     // Some search constraint has been passed
                     // so let's filter accordingly

                     List<ToDo> filteredList=new ArrayList<>();
                     // We'll go through all the List and see
                     // if they contain the supplied string
                     for(ToDo row : mToDoList){
                         // name match condition. this might differ depending on your requirement
                         // here we are looking for title or description match
                         if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                             filteredList.add(row);
                         }
                     }
                     mToDoListFiltered=filteredList;
                     filterResults.values=mToDoListFiltered;
                 }

                 // Finally set the filtered values


                 return filterResults;
             }

             @Override
             protected void publishResults(CharSequence constraint, FilterResults results) {
                 mToDoListFiltered= (ArrayList<ToDo>) results.values;
                 notifyDataSetChanged();
             }
         };

     }

    }
