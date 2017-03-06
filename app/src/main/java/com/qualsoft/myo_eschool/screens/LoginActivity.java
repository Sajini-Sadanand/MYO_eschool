package com.qualsoft.myo_eschool.screens;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qualsoft.coremodule.HttpCallmethods.eSchoolWebServiceFactory;
import com.qualsoft.coremodule.HttpCallmethods.eSchoolWebServices;
import com.qualsoft.coremodule.ResponseModel.DashBoardModel;
import com.qualsoft.coremodule.model.Response;
import com.qualsoft.myo_eschool.R;
import com.qualsoft.myo_eschool.constants.MessageConstants;
import com.qualsoft.myo_eschool.utilities.ProgressDialogUtility;

import org.json.JSONException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    EditText userNameET, mobileNumberET, fullNameET;
    String mUserName, mMobile, mFullName;
    TextView registerTV;
    private loginAsync mLoginAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindView();
        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName = userNameET.getText().toString().trim();
                mMobile = mobileNumberET.getText().toString().trim();
                mFullName = fullNameET.getText().toString().trim();
                new loginAsync().execute();
            }
        });
    }

    private void bindView() {
        userNameET = (EditText) findViewById(R.id.txtusername);
        mobileNumberET = (EditText) findViewById(R.id.txtmobilenumber);
        fullNameET = (EditText) findViewById(R.id.txtfullname);
        registerTV = (TextView) findViewById(R.id.btnregister);
    }

    private class loginAsync extends
            AsyncTask<String, Void, Response<DashBoardModel>> {
        ProgressDialogUtility progressDialogUtility;

        @Override
        protected void onPreExecute() {
            progressDialogUtility = ProgressDialogUtility.getInstance(LoginActivity.this);
            progressDialogUtility.showProgressDialog(MessageConstants.AUTHENTICATING, true);
        }

        @Override
        protected Response<DashBoardModel> doInBackground(
                String... params) {
            Response<DashBoardModel> response = new Response<>();
            eSchoolWebServices mService = eSchoolWebServiceFactory.getService(LoginActivity.this);
//            file not found error
            try {
                response = mService.login(mUserName, mMobile, mFullName);

            } catch (IOException e) {
                response.setThrowable(e);
            } catch (JSONException e) {
                response.setThrowable(e);
            } catch (Exception e) {
                response.setThrowable(e);
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response<DashBoardModel> response) {
            //mProgressDialog.cancel();
            if (response != null) {
                if (response.isSuccess()) {

                    DashBoardModel model = response.getResult();
                    progressDialogUtility.hideProgressDialog();
                    Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();

                } else {
                    progressDialogUtility.hideProgressDialog();
                    Toast.makeText(LoginActivity.this, response.getServerMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialogUtility.hideProgressDialog();
                Toast.makeText(LoginActivity.this, response.getServerMessage(), Toast.LENGTH_SHORT).show();
            }


        }
    }
}
