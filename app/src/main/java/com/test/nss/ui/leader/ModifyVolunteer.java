package com.test.nss.ui.leader;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.test.nss.DatabaseAdapter;
import com.test.nss.Password;
import com.test.nss.R;
import com.test.nss.SwipeHelperRight;
import com.test.nss.api.RetrofitClient;
import com.test.nss.ediary;

import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.test.nss.Helper.AUTH_TOKEN;
import static com.test.nss.Helper.black;
import static com.test.nss.Helper.leaderId;
import static com.test.nss.Helper.primaryColDark;
import static com.test.nss.Helper.transparent;

public class ModifyVolunteer extends Fragment {

    LinearLayout linearL;
    RecyclerView recViewLeader, recViewVolUniv, recViewVolArea, recViewVolClg;
    List<AdapterDataVolunteer> dataVolListUniv, dataVolListArea, dataVolListClg;
    Button univ, area, clg;
    FloatingActionButton back;

    LinearLayout detailsVol;
    CardView cardModify, leader, leaderAll;

    Context context;
    View root;

    public ModifyVolunteer() {
    }

    public static ModifyVolunteer newInstance() {
        return new ModifyVolunteer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_modify_details, container, false);
        recViewLeader = requireActivity().findViewById(R.id.vecLeaderList);

        linearL = root.findViewById(R.id.linearL);
        dataVolListUniv = ListUtils.union(addVolActData("First Year University"), addVolActData("Second Year University"));
        dataVolListArea = ListUtils.union(ListUtils.union(addVolActData("First Year Area Based One"), addVolActData("First Year Area Based Two")),
                ListUtils.union(addVolActData("Second Year Area Based One"), addVolActData("Second Year Area Based Two")));
        dataVolListClg = ListUtils.union(addVolActData("First Year College"), addVolActData("Second Year College"));
        context = requireContext();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String thisVec = getArguments() != null ? getArguments().getString("thisVec") : null;
        back = root.findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        detailsVol = root.findViewById(R.id.detailsVol);
        cardModify = root.findViewById(R.id.cardModify);
        leader = requireActivity().findViewById(R.id.volAct);
        leaderAll = requireActivity().findViewById(R.id.volActAll);

        univ = root.findViewById(R.id.univ);
        area = root.findViewById(R.id.area);
        clg = root.findViewById(R.id.clg);

        recViewVolUniv = root.findViewById(R.id.recVecDetailUniv);
        MyListAdapterVolunteer adapterVolUniv = new MyListAdapterVolunteer(dataVolListUniv, context);

        recViewVolArea = root.findViewById(R.id.recVecDetailArea);
        MyListAdapterVolunteer adapterVolArea = new MyListAdapterVolunteer(dataVolListArea, context);

        recViewVolClg = root.findViewById(R.id.recVecDetailClg);
        MyListAdapterVolunteer adapterVolClg = new MyListAdapterVolunteer(dataVolListClg, context);
        recViewVolClg.setLayoutManager(new LinearLayoutManager(context));
        recViewVolClg.setAdapter(adapterVolClg);

        univ.setOnClickListener(view1 -> {
            detailsVol.setVisibility(View.VISIBLE);
            univ.setTextColor(primaryColDark);
            area.setTextColor(black);
            clg.setTextColor(black);
            recViewVolUniv.setVisibility(View.VISIBLE);
            recViewVolArea.setVisibility(View.GONE);
            recViewVolClg.setVisibility(View.GONE);
        });

        area.setOnClickListener(view1 -> {
            detailsVol.setVisibility(View.VISIBLE);
            area.setTextColor(primaryColDark);
            univ.setTextColor(black);
            clg.setTextColor(black);
            recViewVolArea.setVisibility(View.VISIBLE);
            recViewVolUniv.setVisibility(View.GONE);
            recViewVolClg.setVisibility(View.GONE);
        });

        clg.setOnClickListener(view1 -> {
            detailsVol.setVisibility(View.VISIBLE);
            clg.setTextColor(primaryColDark);
            univ.setTextColor(black);
            area.setTextColor(black);
            recViewVolClg.setVisibility(View.VISIBLE);
            recViewVolUniv.setVisibility(View.GONE);
            recViewVolArea.setVisibility(View.GONE);
        });

        back.setOnClickListener(view12 -> {
            hideFab();
            new Handler().postDelayed(this::onDetach, 250);
        });

        // UNIV
        SwipeHelperRight swipeHelperRightHoursUniv = new SwipeHelperRight(context, recViewVolUniv) {

            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                DatabaseAdapter mdbF = new DatabaseAdapter(context);
                mdbF.createDatabase();
                mdbF.open();
                Cursor cF = mdbF.getVolState(Integer.parseInt(adapterVolUniv.list.get(viewHolder.getAdapterPosition()).getId()));
                cF.moveToFirst();
                if (cF.getString(cF.getColumnIndex("State")).equals("Submitted") || cF.getString(cF.getColumnIndex("State")).equals("Modified")) {
                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_disapprove_24,
                                    transparent,
                                    pos -> {
                                        Log.e("Clicked", "instantiateUnderlayButton: ");
                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListUniv.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(context, R.style.delDialog);

                                        builder3.setTitle("Do you want to delete?");
                                        builder3.setCancelable(false);

                                        builder3.setMessage(dataVolListUniv.get(p).getAct());
                                        builder3.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                adapterVolUniv.notifyItemChanged(p);
                                            }
                                        });

                                        builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                mdb.createDatabase();
                                                mdb.open();
                                                mdb.setStateVolAct("LeaderDelete", Integer.parseInt(adapterVolUniv.list.get(p).getId()));

                                                Snackbar.make(view, "Deleted: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                                adapterVolUniv.list.set(p, new AdapterDataVolunteer(
                                                                adapterVolUniv.list.get(p).getDate(),
                                                                adapterVolUniv.list.get(p).getAct(),
                                                                adapterVolUniv.list.get(p).getHours(),
                                                                adapterVolUniv.list.get(p).getId(),
                                                                "LeaderDelete",
                                                                adapterVolUniv.list.get(p).getActCode(),
                                                                adapterVolUniv.list.get(p).getAssActCode()
                                                        )
                                                );
                                                adapterVolUniv.notifyDataSetChanged();
                                                adapterVolUniv.notifyItemChanged(p);

                                                Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putDetailsLeader(
                                                        "Token " + AUTH_TOKEN,
                                                        Integer.parseInt(adapterVolUniv.list.get(p).getHours()),
                                                        thisVec,
                                                        adapterVolUniv.list.get(p).getActCode(),
                                                        adapterVolUniv.list.get(p).getAssActCode(),
                                                        5,
                                                        Password.PASS,
                                                        Integer.parseInt(adapterVolUniv.list.get(p).getId())
                                                );
                                                mdb.close();
                                                putDetailsLeader.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    @EverythingIsNonNull
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.errorBody() != null) {
                                                            Log.e("error", "onResponse: " + response.errorBody().toString());
                                                        } else if (response.isSuccessful()) {
                                                            Log.e("Done", "onResponse: " + adapterVolUniv.list.get(p).getId());
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        });
                                        builder3.show();
                                        //adapterVolUniv.notifyDataSetChanged();
                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_approve_24,
                                    transparent,
                                    pos -> {
                                        Log.e("Clicked", "instantiateUnderlayButton: ");

                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListUniv.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(context, R.style.delDialog);

                                        builder3.setTitle("Do you want to approve?");
                                        builder3.setCancelable(false);

                                        builder3.setMessage(dataVolListUniv.get(p).getAct());
                                        builder3.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                adapterVolUniv.notifyItemChanged(p);
                                            }
                                        });

                                        builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                                DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                mdb.createDatabase();
                                                mdb.open();
                                                mdb.setStateVolAct("Approved", Integer.parseInt(adapterVolUniv.list.get(p).getId()));

                                                Snackbar.make(view, "Approved: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                                adapterVolUniv.list.set(p, new AdapterDataVolunteer(
                                                                adapterVolUniv.list.get(p).getDate(),
                                                                adapterVolUniv.list.get(p).getAct(),
                                                                adapterVolUniv.list.get(p).getHours(),
                                                                adapterVolUniv.list.get(p).getId(),
                                                                "Approved",
                                                                adapterVolUniv.list.get(p).getActCode(),
                                                                adapterVolUniv.list.get(p).getAssActCode()
                                                        )
                                                );
                                                adapterVolUniv.notifyDataSetChanged();
                                                adapterVolUniv.notifyItemChanged(p);

                                                Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putApprove(
                                                        "Token " + AUTH_TOKEN,
                                                        Integer.parseInt(adapterVolUniv.list.get(p).getHours()),
                                                        thisVec,
                                                        adapterVolUniv.list.get(p).getActCode(),
                                                        adapterVolUniv.list.get(p).getAssActCode(),
                                                        2,
                                                        leaderId,
                                                        Password.PASS,
                                                        Integer.parseInt(adapterVolUniv.list.get(p).getId())
                                                );
                                                mdb.close();

                                                putDetailsLeader.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    @EverythingIsNonNull
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.errorBody() != null) {
                                                            Log.e("error", "onResponse: " + response.errorBody().toString());
                                                        } else if (response.isSuccessful()) {
                                                            Log.e("Done", "onResponse: " + adapterVolUniv.list.get(p).getId());
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        });
                                        builder3.show();
                                        //adapterVolUniv.notifyDataSetChanged();
                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            context,
                            "",
                            R.drawable.ic_vol_edit_24,
                            transparent,
                            pos -> {
                                //adapterVolUniv.notifyDataSetChanged();
                                // builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                //   @Override
                                // public void onClick(DialogInterface dialog3, int which3) {
                                //   dialog3.dismiss();
                                int p;
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == dataVolListUniv.size())
                                    p = viewHolder.getAdapterPosition() - 1;

                                else
                                    p = viewHolder.getAdapterPosition();

                                Log.e("This", "instantiateUnderlayButton: " + p);
                                View viewInflated = LayoutInflater.from(context).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.inputDialog);
                                builder.setCancelable(false);

                                EditText input = viewInflated.findViewById(R.id.input);
                                builder.setView(viewInflated);

                                adapterVolUniv.notifyDataSetChanged();
                                adapterVolUniv.notifyItemChanged(p);
                                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterVolUniv.notifyItemChanged(p);
                                });

                                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    //adapterVolUniv.notifyDataSetChanged();
                                    dialog.dismiss();
                                    if (!input.getText().toString().trim().equals("")) {
                                        int newHours = Integer.parseInt(input.getText().toString());

                                        DatabaseAdapter mdb = new DatabaseAdapter(context);
                                        mdb.createDatabase();
                                        mdb.open();
                                        if (newHours >= 1 && newHours <= 10) {
                                            mdb.setStateVolAct("LeaderModified", Integer.parseInt(adapterVolUniv.list.get(p).getId()));
                                            adapterVolUniv.list.add(p + 1, new AdapterDataVolunteer(
                                                    adapterVolUniv.list.get(p).getDate(),
                                                    adapterVolUniv.list.get(p).getAct(),
                                                    String.valueOf(newHours),
                                                    adapterVolUniv.list.get(p).getId(),
                                                    "LeaderModified",
                                                    adapterVolUniv.list.get(p).getActCode(),
                                                    adapterVolUniv.list.get(p).getAssActCode()
                                            ));

                                            adapterVolUniv.notifyDataSetChanged();
                                            dataVolListUniv.remove(p);
                                            adapterVolUniv.notifyItemInserted(p);

                                            mdb.setVolActHours(
                                                    Integer.parseInt(adapterVolUniv.list.get(p).getHours()),
                                                    Integer.parseInt(adapterVolUniv.list.get(p).getId())

                                            );
                                            Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putDetailsLeader(
                                                    "Token " + AUTH_TOKEN,
                                                    newHours,
                                                    thisVec,
                                                    adapterVolUniv.list.get(p).getActCode(),
                                                    adapterVolUniv.list.get(p).getAssActCode(),
                                                    6,
                                                    Password.PASS,
                                                    Integer.parseInt(adapterVolUniv.list.get(p).getId())
                                            );
                                            adapterVolUniv.notifyDataSetChanged();
                                            mdb.close();
                                            putDetailsLeader.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                @EverythingIsNonNull
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful()) {
                                                        Snackbar.make(view, "Edited for: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                }
                                            });
                                            adapterVolUniv.notifyItemChanged(p);
                                        } else
                                            Toast.makeText(context, "Please enter in given range", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                );
                                if (viewInflated.getParent() != null)
                                    ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                                builder.show();
                            }
                    ));
                    mdbF.close();
                }
                //adapterVolUniv.notifyDataSetChanged();
            }
        };

        recViewVolUniv.setLayoutManager(new LinearLayoutManager(context));
        recViewVolUniv.setAdapter(adapterVolUniv);

        // AREA
        SwipeHelperRight swipeHelperRightHoursArea = new SwipeHelperRight(context, recViewVolArea) {

            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                DatabaseAdapter mdbF = new DatabaseAdapter(context);
                mdbF.createDatabase();
                mdbF.open();
                Cursor cF = mdbF.getVolState(Integer.parseInt(adapterVolArea.list.get(viewHolder.getAdapterPosition()).getId()));
                cF.moveToFirst();
                if (cF.getString(cF.getColumnIndex("State")).equals("Submitted") || cF.getString(cF.getColumnIndex("State")).equals("Modified")) {
                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_disapprove_24,
                                    transparent,
                                    pos -> {
                                        Log.e("Clicked", "instantiateUnderlayButton: ");
                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListArea.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(context, R.style.delDialog);

                                        builder3.setTitle("Do you want to delete?");
                                        builder3.setCancelable(false);

                                        builder3.setMessage(dataVolListArea.get(p).getAct());
                                        builder3.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                adapterVolArea.notifyItemChanged(p);
                                            }
                                        });

                                        builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                mdb.createDatabase();
                                                mdb.open();
                                                mdb.setStateVolAct("LeaderDelete", Integer.parseInt(adapterVolArea.list.get(p).getId()));

                                                Snackbar.make(view, "Deleted: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                                adapterVolArea.list.set(p, new AdapterDataVolunteer(
                                                                adapterVolArea.list.get(p).getDate(),
                                                                adapterVolArea.list.get(p).getAct(),
                                                                adapterVolArea.list.get(p).getHours(),
                                                                adapterVolArea.list.get(p).getId(),
                                                                "LeaderDelete",
                                                                adapterVolArea.list.get(p).getActCode(),
                                                                adapterVolArea.list.get(p).getAssActCode()
                                                        )
                                                );
                                                adapterVolArea.notifyDataSetChanged();
                                                adapterVolArea.notifyItemChanged(p);

                                                Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putDetailsLeader(
                                                        "Token " + AUTH_TOKEN,
                                                        Integer.parseInt(adapterVolArea.list.get(p).getHours()),
                                                        thisVec,
                                                        adapterVolArea.list.get(p).getActCode(),
                                                        adapterVolArea.list.get(p).getAssActCode(),
                                                        5,
                                                        Password.PASS,
                                                        Integer.parseInt(adapterVolArea.list.get(p).getId())
                                                );
                                                mdb.close();
                                                putDetailsLeader.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    @EverythingIsNonNull
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.errorBody() != null) {
                                                            Log.e("error", "onResponse: " + response.errorBody().toString());
                                                        } else if (response.isSuccessful()) {
                                                            Log.e("Done", "onResponse: " + adapterVolArea.list.get(p).getId());
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        });
                                        builder3.show();
                                        //adapterVolArea.notifyDataSetChanged();
                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_approve_24,
                                    transparent,
                                    pos -> {
                                        Log.e("Clicked", "instantiateUnderlayButton: ");

                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListArea.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(context, R.style.delDialog);

                                        builder3.setTitle("Do you want to approve?");
                                        builder3.setCancelable(false);

                                        builder3.setMessage(dataVolListArea.get(p).getAct());
                                        builder3.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                adapterVolArea.notifyItemChanged(p);
                                            }
                                        });

                                        builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                                DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                mdb.createDatabase();
                                                mdb.open();
                                                mdb.setStateVolAct("Approved", Integer.parseInt(adapterVolArea.list.get(p).getId()));

                                                Snackbar.make(view, "Approved: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                                adapterVolArea.list.set(p, new AdapterDataVolunteer(

                                                                adapterVolArea.list.get(p).getDate(),
                                                                adapterVolArea.list.get(p).getAct(),
                                                                adapterVolArea.list.get(p).getHours(),
                                                                adapterVolArea.list.get(p).getId(),
                                                                "Approved",
                                                                adapterVolArea.list.get(p).getActCode(),
                                                                adapterVolArea.list.get(p).getAssActCode()
                                                        )
                                                );
                                                adapterVolArea.notifyDataSetChanged();
                                                adapterVolArea.notifyItemChanged(p);

                                                Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putApprove(
                                                        "Token " + AUTH_TOKEN,
                                                        Integer.parseInt(adapterVolArea.list.get(p).getHours()),
                                                        thisVec,
                                                        adapterVolArea.list.get(p).getActCode(),
                                                        adapterVolArea.list.get(p).getAssActCode(),
                                                        2,
                                                        leaderId,
                                                        Password.PASS,
                                                        Integer.parseInt(adapterVolArea.list.get(p).getId())
                                                );
                                                mdb.close();

                                                putDetailsLeader.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    @EverythingIsNonNull
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.errorBody() != null) {
                                                            Log.e("error", "onResponse: " + response.errorBody().toString());
                                                        } else if (response.isSuccessful()) {
                                                            Log.e("Done", "onResponse: " + adapterVolArea.list.get(p).getId());
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        });
                                        builder3.show();
                                        //adapterVolArea.notifyDataSetChanged();
                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            context,
                            "",
                            R.drawable.ic_vol_edit_24,
                            transparent,
                            pos -> {
                                //adapterVolArea.notifyDataSetChanged();
                                // builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                //   @Override
                                // public void onClick(DialogInterface dialog3, int which3) {
                                //   dialog3.dismiss();
                                int p;
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == dataVolListArea.size())
                                    p = viewHolder.getAdapterPosition() - 1;

                                else
                                    p = viewHolder.getAdapterPosition();

                                Log.e("This", "instantiateUnderlayButton: " + p);
                                View viewInflated = LayoutInflater.from(context).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.inputDialog);
                                builder.setCancelable(false);

                                EditText input = viewInflated.findViewById(R.id.input);
                                builder.setView(viewInflated);

                                adapterVolArea.notifyDataSetChanged();
                                adapterVolArea.notifyItemChanged(p);
                                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterVolArea.notifyItemChanged(p);
                                });

                                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    //adapterVolArea.notifyDataSetChanged();
                                    dialog.dismiss();
                                    if (!input.getText().toString().trim().equals("")) {
                                        int newHours = Integer.parseInt(input.getText().toString());

                                        DatabaseAdapter mdb = new DatabaseAdapter(context);
                                        mdb.createDatabase();
                                        mdb.open();

                                        if (newHours >= 1 && newHours <= 10) {
                                            mdb.setStateVolAct("LeaderModified", Integer.parseInt(adapterVolArea.list.get(p).getId()));
                                            adapterVolArea.list.add(p + 1, new AdapterDataVolunteer(
                                                    adapterVolArea.list.get(p).getDate(),
                                                    adapterVolArea.list.get(p).getAct(),
                                                    String.valueOf(newHours),
                                                    adapterVolArea.list.get(p).getId(),
                                                    "LeaderModified",
                                                    adapterVolArea.list.get(p).getActCode(),
                                                    adapterVolArea.list.get(p).getAssActCode()
                                            ));

                                            adapterVolArea.notifyDataSetChanged();
                                            dataVolListArea.remove(p);
                                            adapterVolArea.notifyItemInserted(p);
                                            mdb.setVolActHours(
                                                    Integer.parseInt(adapterVolArea.list.get(p).getHours()),
                                                    Integer.parseInt(adapterVolArea.list.get(p).getId())

                                            );
                                            Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putDetailsLeader(
                                                    "Token " + AUTH_TOKEN,
                                                    newHours,
                                                    thisVec,
                                                    adapterVolArea.list.get(p).getActCode(),
                                                    adapterVolArea.list.get(p).getAssActCode(),
                                                    6,
                                                    Password.PASS,
                                                    Integer.parseInt(adapterVolArea.list.get(p).getId())
                                            );

                                            adapterVolArea.notifyDataSetChanged();
                                            mdb.close();
                                            putDetailsLeader.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                @EverythingIsNonNull
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful()) {
                                                        Snackbar.make(view, "Edited for: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                }
                                            });
                                            adapterVolArea.notifyItemChanged(p);
                                        } else
                                            Toast.makeText(context, "Please enter in given range", Toast.LENGTH_SHORT).show();

                                    }
                                        }
                                );
                                if (viewInflated.getParent() != null)
                                    ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                                builder.show();
                            }
                    ));
                    mdbF.close();
                }
                //adapterVolArea.notifyDataSetChanged();
            }
        };

        recViewVolArea.setLayoutManager(new LinearLayoutManager(context));
        recViewVolArea.setAdapter(adapterVolArea);

        // COLLEGE
        SwipeHelperRight swipeHelperRightHoursClg = new SwipeHelperRight(context, recViewVolClg) {

            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                DatabaseAdapter mdbF = new DatabaseAdapter(context);
                mdbF.createDatabase();
                mdbF.open();
                Cursor cF = mdbF.getVolState(Integer.parseInt(adapterVolClg.list.get(viewHolder.getAdapterPosition()).getId()));
                cF.moveToFirst();
                if (cF.getString(cF.getColumnIndex("State")).equals("Submitted") || cF.getString(cF.getColumnIndex("State")).equals("Modified")) {
                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_disapprove_24,
                                    transparent,
                                    pos -> {
                                        Log.e("Clicked", "instantiateUnderlayButton: ");
                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListClg.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(context, R.style.delDialog);

                                        builder3.setTitle("Do you want to delete?");
                                        builder3.setCancelable(false);

                                        builder3.setMessage(dataVolListClg.get(p).getAct());
                                        builder3.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                adapterVolClg.notifyItemChanged(p);
                                            }
                                        });

                                        builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                mdb.createDatabase();
                                                mdb.open();

                                                mdb.setStateVolAct("LeaderDelete", Integer.parseInt(adapterVolClg.list.get(p).getId()));

                                                Snackbar.make(view, "Deleted: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                                adapterVolClg.list.set(p, new AdapterDataVolunteer(
                                                                adapterVolClg.list.get(p).getDate(),
                                                                adapterVolClg.list.get(p).getAct(),
                                                                adapterVolClg.list.get(p).getHours(),
                                                                adapterVolClg.list.get(p).getId(),
                                                                "LeaderDelete",
                                                                adapterVolClg.list.get(p).getActCode(),
                                                                adapterVolClg.list.get(p).getAssActCode()
                                                        )
                                                );
                                                adapterVolClg.notifyDataSetChanged();
                                                adapterVolClg.notifyItemChanged(p);

                                                Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putDetailsLeader(
                                                        "Token " + AUTH_TOKEN,
                                                        Integer.parseInt(adapterVolClg.list.get(p).getHours()),
                                                        thisVec,
                                                        adapterVolClg.list.get(p).getActCode(),
                                                        adapterVolClg.list.get(p).getAssActCode(),
                                                        5,
                                                        Password.PASS,
                                                        Integer.parseInt(adapterVolClg.list.get(p).getId())
                                                );
                                                mdb.close();
                                                putDetailsLeader.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    @EverythingIsNonNull
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.errorBody() != null) {
                                                            Log.e("error", "onResponse: " + response.errorBody().toString());
                                                        } else if (response.isSuccessful()) {
                                                            Log.e("Done", "onResponse: " + adapterVolClg.list.get(p).getId());
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        });
                                        builder3.show();
                                        //adapterVolClg.notifyDataSetChanged();
                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_approve_24,
                                    transparent,
                                    pos -> {
                                        Log.e("Clicked", "instantiateUnderlayButton: ");

                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListClg.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(context, R.style.delDialog);

                                        builder3.setTitle("Do you want to approve?");
                                        builder3.setCancelable(false);

                                        builder3.setMessage(dataVolListClg.get(p).getAct());
                                        builder3.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                adapterVolClg.notifyItemChanged(p);
                                            }
                                        });

                                        builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                                DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                mdb.createDatabase();
                                                mdb.open();
                                                mdb.setStateVolAct("Approved", Integer.parseInt(adapterVolClg.list.get(p).getId()));

                                                Snackbar.make(view, "Approved: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                                adapterVolClg.list.set(p, new AdapterDataVolunteer(
                                                                adapterVolClg.list.get(p).getDate(),
                                                                adapterVolClg.list.get(p).getAct(),
                                                                adapterVolClg.list.get(p).getHours(),
                                                                adapterVolClg.list.get(p).getId(),
                                                                "Approved",
                                                                adapterVolClg.list.get(p).getActCode(),
                                                                adapterVolClg.list.get(p).getAssActCode()
                                                        )
                                                );
                                                adapterVolClg.notifyDataSetChanged();
                                                adapterVolClg.notifyItemChanged(p);

                                                Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putApprove(
                                                        "Token " + AUTH_TOKEN,
                                                        Integer.parseInt(adapterVolClg.list.get(p).getHours()),
                                                        thisVec,
                                                        adapterVolClg.list.get(p).getActCode(),
                                                        adapterVolClg.list.get(p).getAssActCode(),
                                                        2,
                                                        leaderId,
                                                        Password.PASS,
                                                        Integer.parseInt(adapterVolClg.list.get(p).getId())
                                                );
                                                mdb.close();

                                                putDetailsLeader.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    @EverythingIsNonNull
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.errorBody() != null) {
                                                            Log.e("error", "onResponse: " + response.errorBody().toString());
                                                        } else if (response.isSuccessful()) {
                                                            Log.e("Done", "onResponse: " + adapterVolClg.list.get(p).getId());
                                                        }
                                                    }

                                                    @Override
                                                    @EverythingIsNonNull
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        });
                                        builder3.show();
                                        //adapterVolClg.notifyDataSetChanged();
                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            context,
                            "",
                            R.drawable.ic_vol_edit_24,
                            transparent,
                            pos -> {
                                //adapterVolClg.notifyDataSetChanged();
                                // builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                //   @Override
                                // public void onClick(DialogInterface dialog3, int which3) {
                                //   dialog3.dismiss();
                                int p;
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == dataVolListClg.size())
                                    p = viewHolder.getAdapterPosition() - 1;

                                else
                                    p = viewHolder.getAdapterPosition();

                                Log.e("This", "instantiateUnderlayButton: " + p);
                                View viewInflated = LayoutInflater.from(context).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.inputDialog);
                                builder.setCancelable(false);

                                EditText input = viewInflated.findViewById(R.id.input);
                                builder.setView(viewInflated);

                                adapterVolClg.notifyDataSetChanged();
                                adapterVolClg.notifyItemChanged(p);
                                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterVolClg.notifyItemChanged(p);
                                });

                                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    //adapterVolClg.notifyDataSetChanged();
                                    dialog.dismiss();
                                    if (!input.getText().toString().trim().equals("")) {
                                        int newHours = Integer.parseInt(input.getText().toString());

                                        DatabaseAdapter mdb = new DatabaseAdapter(context);
                                        mdb.createDatabase();
                                        mdb.open();
                                        if (newHours >= 1 && newHours <= 10) {
                                            mdb.setStateVolAct("LeaderModified", Integer.parseInt(adapterVolClg.list.get(p).getId()));
                                            adapterVolClg.list.add(p + 1, new AdapterDataVolunteer(
                                                    adapterVolClg.list.get(p).getDate(),
                                                    adapterVolClg.list.get(p).getAct(),
                                                    String.valueOf(newHours),
                                                    adapterVolClg.list.get(p).getId(),
                                                    "LeaderModified",
                                                    adapterVolClg.list.get(p).getActCode(),
                                                    adapterVolClg.list.get(p).getAssActCode()
                                            ));

                                            adapterVolClg.notifyDataSetChanged();
                                            dataVolListClg.remove(p);
                                            adapterVolClg.notifyItemInserted(p);
                                            mdb.setVolActHours(
                                                    Integer.parseInt(adapterVolClg.list.get(p).getHours()),
                                                    Integer.parseInt(adapterVolClg.list.get(p).getId())

                                            );
                                            Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putDetailsLeader(
                                                    "Token " + AUTH_TOKEN,
                                                    newHours,
                                                    thisVec,
                                                    adapterVolClg.list.get(p).getActCode(),
                                                    adapterVolClg.list.get(p).getAssActCode(),
                                                    6,
                                                    Password.PASS,
                                                    Integer.parseInt(adapterVolClg.list.get(p).getId())
                                            );
                                            adapterVolClg.notifyDataSetChanged();
                                            mdb.close();
                                            putDetailsLeader.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                @EverythingIsNonNull
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful()) {
                                                        Snackbar.make(view, "Edited for: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                }
                                            });
                                            adapterVolClg.notifyItemChanged(p);
                                        } else
                                            Toast.makeText(context, "Please enter in given range", Toast.LENGTH_SHORT).show();

                                    }
                                        }
                                );
                                if (viewInflated.getParent() != null)
                                    ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                                builder.show();
                            }
                    ));
                    mdbF.close();
                }
                //adapterVolClg.notifyDataSetChanged();
            }
        };

        recViewVolClg.setLayoutManager(new LinearLayoutManager(context));
        recViewVolClg.setAdapter(adapterVolClg);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHelperRightHoursUniv);
        itemTouchHelper.attachToRecyclerView(recViewVolUniv);

        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(swipeHelperRightHoursArea);
        itemTouchHelper2.attachToRecyclerView(recViewVolArea);

        ItemTouchHelper itemTouchHelper3 = new ItemTouchHelper(swipeHelperRightHoursClg);
        itemTouchHelper3.attachToRecyclerView(recViewVolClg);
    }

    public List<AdapterDataVolunteer> addVolActData(String actName) {
        ArrayList<AdapterDataVolunteer> data3 = new ArrayList<>();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        assert getArguments() != null;
        String thisVec = getArguments().getString("thisVec");

        Cursor c0 = mDbHelper.getVolDetails(actName, thisVec);
        int m = c0.getCount();

        c0.moveToFirst();
        while (m > 0) {
            data3.add(new AdapterDataVolunteer(
                    c0.getString(c0.getColumnIndex("Date")),
                    c0.getString(c0.getColumnIndex("AssignedActivityName")),
                    c0.getString(c0.getColumnIndex("Hours")),
                    c0.getString(c0.getColumnIndex("id")),
                    c0.getString(c0.getColumnIndex("State")),
                    c0.getInt(c0.getColumnIndex("ActCode")),
                    c0.getInt(c0.getColumnIndex("AssignedActivityCode"))
            ));
            c0.moveToNext();
            m = m - 1;
        }
        mDbHelper.close();
        return data3;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        leader.setVisibility(View.VISIBLE);
        leaderAll.setVisibility(View.VISIBLE);
        linearL.setVisibility(View.GONE);
    }

    private void hideFab() {
        View v = root.findViewById(R.id.back);
        int x = v.getWidth() / 2;
        int y = v.getHeight() / 2;

        float inRad = (float) Math.hypot(x, y);
        Animator animator = ViewAnimationUtils.createCircularReveal(v, x, y, inRad, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.GONE);
            }
        });
        animator.start();
    }
}