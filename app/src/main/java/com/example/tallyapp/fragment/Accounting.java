package com.example.tallyapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.tallyapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Accounting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Accounting extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private BottomNavigationView bottomNavigationView;
    private Fragment expenseFragment, incomeFragment;

    public Accounting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Accounting.
     */
    // TODO: Rename and change types and number of parameters
    public static Accounting newInstance(String param1, String param2) {
        Accounting fragment = new Accounting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_accounting, container, false);
        initView(view);
        if (savedInstanceState == null) {
            // 如果是第一次加载，替换默认布局为支出界面
            AccountingExpense expenseFragment = new AccountingExpense();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.accounting_content, expenseFragment)
                    .commit();
        }
        checkButtom();
        return view;
    }

    private void initView( View view){
        bottomNavigationView = view.findViewById(R.id.accounting_BottonNavigationView);
        expenseFragment = new AccountingExpense();
        incomeFragment = new AccountingIncome();
    }

    private void replaceFragment(Fragment fragment, boolean isRightDirection) {
        int enterAnim, exitAnim, popEnterAnim, popExitAnim;

        if (isRightDirection) {
            enterAnim = R.anim.slide_in_right;
            exitAnim = R.anim.slide_out_left;
            popEnterAnim = R.anim.slide_in_left;
            popExitAnim = R.anim.slide_out_right;
        } else {
            enterAnim = R.anim.slide_in_left;
            exitAnim = R.anim.slide_out_right;
            popEnterAnim = R.anim.slide_in_right;
            popExitAnim = R.anim.slide_out_left;
        }

        getChildFragmentManager().beginTransaction()
                .setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
                .replace(R.id.accounting_content, fragment)
                .addToBackStack(null) // 如果需要添加到返回栈中
                .commit();
    }

    private void checkButtom() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            //根据当前页码点击时判断左右滑动过渡动画
            if (item.getItemId() == R.id.expense_type) {
                replaceFragment(expenseFragment, false);
                return true;
            } else {
                replaceFragment(incomeFragment, true);
                return true;
            }
        });
    }
}