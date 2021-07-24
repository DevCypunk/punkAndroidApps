package com.undroider.dinnermenus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.undroider.dinnermenus.databinding.ActivityItemListBinding;
import com.undroider.dinnermenus.databinding.ItemListBinding;
import com.undroider.dinnermenus.dummy.DummyContent;

import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity
{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private static final String BRANCH_FOOD = "B_Food";

    private ActivityItemListBinding binding_bin;


    private EditText edi_name, edi_price;

    private Button btn_saveItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding_bin = ActivityItemListBinding.inflate(getLayoutInflater());

        setContentView(binding_bin.getRoot());
        //setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        edi_name = findViewById(R.id.id_ediTxt_inputName);
        edi_price = findViewById(R.id.id_ediTxt_inputPrice);
        btn_saveItem = findViewById(R.id.id_btn_savedOrder);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null)
        {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);


        btn_saveItem.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DummyContent.DinnerItem dinner_obj = new DummyContent.DinnerItem(binding_bin.idEdiTxtInputName.getText().toString().trim(),
                        binding_bin.idEdiTxtInputPrice.getText().toString().trim());

                FirebaseDatabase databaseFire = FirebaseDatabase.getInstance("https://dinnermenus-29b33-default-rtdb.firebaseio.com/");
                DatabaseReference dataRef = databaseFire.getReference(BRANCH_FOOD);

                dataRef.push().setValue(dinner_obj);

                edi_name.setText("");
                edi_price.setText("");
            }
        });
    }
    // end mth onCreate

    private void setupRecyclerView(@NonNull RecyclerView recyclerView)
    {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://dinnermenus-29b33-default-rtdb.firebaseio.com/");
        DatabaseReference dataRef = database.getReference(BRANCH_FOOD);

        dataRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                DummyContent.DinnerItem dinnerObj = snapshot.getValue(DummyContent.DinnerItem.class);

                dinnerObj.setDin_id(snapshot.getKey());

                if (!DummyContent.ITEMS.contains(dinnerObj))
                {
                    DummyContent.addItem(dinnerObj);
                }

                recyclerView.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
    //end setupRecyclerView

    //AdapterFunction
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
    {

        private final ItemListActivity mParentActivity;
        private final List<DummyContent.DinnerItem> mValues;
        private final boolean mTwoPane;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DummyContent.DinnerItem item = (DummyContent.DinnerItem) view.getTag();
                if (mTwoPane)
                {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.getDin_id());

                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);

                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                }
                else
                {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.getDin_id());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<DummyContent.DinnerItem> items,
                                      boolean twoPane)
        {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position)
        {
            holder.mIdView.setText("$ " + mValues.get(position).getDin_price());
            holder.mContentView.setText(mValues.get(position).getDin_name());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount()
        {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view)
            {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }

    }
    // end Inner Cls SimpleItemRecyclerViewAdapter

}