package com.rsm.yuri.projecttaxilivredriver.adddialog.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.adddialog.AddDialogPresenter;
import com.rsm.yuri.projecttaxilivredriver.adddialog.di.AddDialogComponent;
import com.rsm.yuri.projecttaxilivredriver.adddialog.dialoglistenercallback.AddDialogFragmentListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yuri_ on 10/11/2017.
 */

public class AddDialogFragment extends DialogFragment implements AddDialogView, DialogInterface.OnShowListener {

    @BindView(R.id.editTxtValue)
    EditText inputValue;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    AddDialogPresenter presenter;

    TaxiLivreDriverApp app;

    public final static String KEY = "key";

    public AddDialogFragment() {// Empty constructor required for DialogFragment
        //addContactPresenter = new AddContactPresenterImpl(this);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder =  new  AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_dialog_message_title)
                .setPositiveButton(R.string.add_dialog_message_add,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                .setNegativeButton(R.string.add_dialog_message_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });

        LayoutInflater i = getActivity().getLayoutInflater();
        View view = i.inflate(R.layout.fragment_add_dialog,null);

        ButterKnife.bind(this, view);
        inputValue.setHint(getArguments().getString(KEY));

        app = (TaxiLivreDriverApp) getActivity().getApplication();
        setupInjection();

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);

        return dialog;
    }

    private void setupInjection() {
        //AddDialogComponent addDialogComponent = app.getAddDialogComponent(this);
        //addDialogComponent.inject(this);
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {

            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(inputValue.getText().toString().length() > 3)
                        presenter.add(getArguments().getString(KEY), inputValue.getText().toString());
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }

        presenter.onShow();
    }

    @Override
    public void showInput() {
        inputValue.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInput() {
        inputValue.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void contactAdded() {
        //Toast.makeText(getActivity(), R.string.addcontact_message_contactadded, Toast.LENGTH_SHORT).show();
        AddDialogFragmentListener activity = (AddDialogFragmentListener) getActivity();
        activity.onFinishAddDialog(getArguments().getString(KEY), inputValue.getText().toString());
        dismiss();
    }

    @Override
    public void contactNotAdded() {
        inputValue.setText("");
        inputValue.setError(getString(R.string.add_dialog_error_message));
    }
}
