package com.kisan.project.contactotp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.kisan.project.contactotp.R.id.container;

public class MainActivity extends AppCompatActivity {

    public static JSONObject contact = new JSONObject();
    private static Context pcontext;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pcontext = this;
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
//        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_MSG_LIST = "msg_list";
        private static ArrayList<String> contactList;
        private static ArrayList<String> messageList;
        private static  View rootView=null;
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public static ArrayList<String> loadMsglist(Context context)
        {
            ArrayList<String> mlist = new ArrayList<>();
            DBHandler db = new DBHandler(context);
            Log.d("Reading: ", "Reading all contacts..");
            JSONObject obj = db.getAllMsgDetails();
            JSONArray  arr = new JSONArray();

            try {
                arr = obj.getJSONArray("msgdata");
               // Toast.makeText(context, "data loaded "+String.valueOf(arr.length()), Toast.LENGTH_LONG).show();
                for(int i=0;i<arr.length();i++)
                {
                    String log = arr.getJSONObject(i).get("_name") + "#" +arr.getJSONObject(i).get("_time")+ "#" + arr.getJSONObject(i).get("_otp");
                    // Writing Contacts to log
                    mlist.add(0,log);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return mlist;
        }
        /////

        // initialize the contact list
        private static void initViews(View v, final Context context, ArrayList<String> inList, final int section) {
            RecyclerView recyclerView=null;

            if(section==1)
            recyclerView = (RecyclerView) v.findViewById(R.id.card_recycler_view_one);//fragment_one contact list
            else
            {
                recyclerView = (RecyclerView) v.findViewById(R.id.card_recycler_view_two);//fragment_two message list
            }

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter adapter = new DataAdapter(inList,section);
            recyclerView.setAdapter(adapter);

            recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                });

                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && gestureDetector.onTouchEvent(e)) {
                        int position = rv.getChildAdapterPosition(child);
                        if(section==1)//fragment_one contact list
                        {
                            JSONObject _contact;
                            //Toast.makeText(context, (CharSequence) contactList.get(position), Toast.LENGTH_SHORT).show();

                            try {
                                _contact = contact.getJSONArray("data").getJSONObject(position);
                                Intent to_contact_detail = new Intent(context, ContactInfo.class);
                                to_contact_detail.putExtra("json_contact", _contact.toString());
                                context.startActivity(to_contact_detail);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }else//fragment_two message list
                        {
                           // DialogBox(context, "test00","yesysys");
                        }


                    }

                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });
        }

///  JSON list of contact


        public static ArrayList<String> loadContactList() {
            ArrayList<String> List = new ArrayList<>();
            JSONArray jlist = new JSONArray();
            JSONObject item = new JSONObject();

                try {
                    item.put("fname", "Tushar");
                    item.put("lname", "Chavhan");
                    item.put("number", "+919420678744");
                    jlist.put(item);

                    item = new JSONObject();

                    item.put("fname", "Rajesh");
                    item.put("lname", "Shedolkar");
                    item.put("number", "+918867794871");
                    jlist.put(item);

                    item = new JSONObject();

                    item.put("fname", "Aditya");
                    item.put("lname", "Agarwalla");
                    item.put("number", "+919111011382");
                    jlist.put(item);

                    item = new JSONObject();

                    item.put("fname", "Jack");
                    item.put("lname", "Richer");
                    item.put("number", "+919970257396");
                    jlist.put(item);

                    item = new JSONObject();

                    item.put("fname", "Aaditya");
                    item.put("lname", "Agarwalla");
                    item.put("number", "+919971792703");
                    jlist.put(item);

                    contact.put("data", jlist);

                    String fname = (contact.getJSONArray("data").getJSONObject(0)).getString("fname");
                    String lname = (contact.getJSONArray("data").getJSONObject(0)).getString("lname");
                    List.add(fname + " " + lname);

                    fname = (contact.getJSONArray("data").getJSONObject(1)).getString("fname");
                    lname = (contact.getJSONArray("data").getJSONObject(1)).getString("lname");
                    List.add(fname + " " + lname);

                    fname = (contact.getJSONArray("data").getJSONObject(2)).getString("fname");
                    lname = (contact.getJSONArray("data").getJSONObject(2)).getString("lname");
                    List.add(fname + " " + lname);

                    fname = (contact.getJSONArray("data").getJSONObject(3)).getString("fname");
                    lname = (contact.getJSONArray("data").getJSONObject(3)).getString("lname");
                    List.add(fname + " " + lname);

                    fname = (contact.getJSONArray("data").getJSONObject(4)).getString("fname");
                    lname = (contact.getJSONArray("data").getJSONObject(4)).getString("lname");
                    List.add(fname + " " + lname);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            return List;
        }


        /// load message list

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            int section = getArguments().getInt(ARG_SECTION_NUMBER);
            if (section == 1) { // fragment_one contact list

                rootView = inflater.inflate(R.layout.fragment_main_one, container, false);

                contactList = loadContactList();
                initViews(rootView, getContext(), contactList, section);
            } else
            {   //fragment_two message list
                rootView = inflater.inflate(R.layout.fragment_main_two, container, false);
                setHasOptionsMenu(true);
               // Toast.makeText(getContext(),"please refresh the list", Toast.LENGTH_LONG).show();
            }
            return rootView;
        }

        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_frag_two, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if(id == R.id.action_refresh){
                messageList = loadMsglist(getContext());
                initViews(rootView, getContext(), messageList, 2);
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if(position==1)
            {

            }
          return PlaceholderFragment.newInstance(position + 1);

        }

        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE;
        }


        @Override
        public int getCount() {
            return 2;
        }


    }


}
