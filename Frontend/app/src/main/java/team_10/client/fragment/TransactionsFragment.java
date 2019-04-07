package team_10.client.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import team_10.client.R;
import team_10.client.object.account.Account;
import team_10.client.object.account.Transaction;
import team_10.client.utility.TransactionsAdapter;

import static team_10.client.settings.SharedPreferencesManager.getUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransactionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ArrayList<Transaction> transactions;
    ArrayList<Transaction> recurringTransactions;
    TransactionsAdapter adapter;
    TransactionsAdapter recurringAdapter;
    RecyclerView recyclerView;
    RecyclerView recurringRecyclerView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionsFragment newInstance(String param1, String param2) {
        TransactionsFragment fragment = new TransactionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void editTransactions()
    {
        List<Account> usersAccounts = getUser().getAccounts();
        for (int i = 0; i < usersAccounts.size(); i++)
        {
            transactions.addAll(usersAccounts.get(i).getTransactions().values());
        }
    }

    public void editRecurringTransactions()
    {
        List<Account> usersAccounts = getUser().getAccounts();
        for (int i = 0; i < usersAccounts.size(); i++)
        {
            ArrayList<Transaction> temp = new ArrayList<>();
            temp.addAll(usersAccounts.get(i).getTransactions().values());
            for (int j = 0; j < temp.size(); j++)
            {
                if (temp.get(j).getRecurring() == 1)
                {
                    recurringTransactions.add(temp.get(j));
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvTransactions);

        transactions = new ArrayList<Transaction>();
        adapter = new TransactionsAdapter(transactions);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recurringRecyclerView = (RecyclerView) view.findViewById(R.id.rvRecurringTransactions);

        recurringTransactions = new ArrayList<Transaction>();
        recurringAdapter = new TransactionsAdapter(recurringTransactions);
        recurringRecyclerView.setAdapter(recurringAdapter);
        recurringRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recurringRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        editTransactions();
        editRecurringTransactions();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
