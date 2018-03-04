/*
 * Copyright 2018 EyeSeeTea
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.github.stkent.bugshaker.github;

import static com.github.stkent.bugshaker.utilities.NetworkUtils.isDeviceConnected;
import static com.github.stkent.bugshaker.utilities.StringUtils.addMarkdownCodeBlock;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.github.stkent.bugshaker.R;
import com.github.stkent.bugshaker.github.api.GitHubApiProvider;
import com.github.stkent.bugshaker.github.api.GitHubResponse;
import com.github.stkent.bugshaker.github.api.Issue;
import com.github.stkent.bugshaker.utilities.Logger;
import com.github.stkent.bugshaker.utilities.Toaster;

import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReportBugActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_GIT_HUB_CONFIGURATION = "EXTRA_GIT_HUB_CONFIGURATION";
    public static final String EXTRA_DEVICE_INFO = "EXTRA_DEVICE_INFO";
    public static final String EXTRA_IS_LOGGER_ACTIVE = "EXTRA_IS_LOGGER_ACTIVE";
    private static final int DELAY_BEFORE_SENDING_REPORT = 4;
    private static final int PROGRESS_BAR_ELEVATION = 5;

    private String deviceInfo;
    private EditText issueTitleEditText;
    private Toaster toaster;
    private ProgressBar progressBar;
    private BugReportProvider bugReportProvider;
    private Button reportBugButton;
    private Logger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_bug);
        bindUI();

        initValuesFromIntent();

    }

    @Override
    public void onClick(View view) {

        if (areFieldsNotEmpty()) {
            submitBugOnlyWithNetworkConnectivity();
        } else {
            issueTitleEditText.setError(getString(R.string.required));
        }
    }

    private void submitBugOnlyWithNetworkConnectivity() {
        if (isDeviceConnected(this)) {
            submitBug();
        } else {
            toaster.toast(R.string.error_network_connection_not_available);
        }
    }

    private void initValuesFromIntent() {
        GitHubConfiguration gitHubConfiguration = getIntent().getParcelableExtra(
                EXTRA_GIT_HUB_CONFIGURATION);

        deviceInfo = getIntent().getStringExtra(EXTRA_DEVICE_INFO);
        boolean isLoggerActive = getIntent().getBooleanExtra(EXTRA_IS_LOGGER_ACTIVE, false);
        logger = new Logger(isLoggerActive);
        bugReportProvider = new GitHubApiProvider(gitHubConfiguration);
    }

    private void bindUI() {
        progressBar = findViewById(R.id.progressBar);
        toaster = new Toaster(getApplicationContext());
        issueTitleEditText = findViewById(R.id.issue_title);

        reportBugButton = findViewById(R.id.report_bug);

        reportBugButton.setOnClickListener(this);

    }

    private void submitBug() {

        Issue newIssue = getIssueFromUI();

        prepareUIComponentsBeforeSubmit();

        bugReportProvider.addIssue(newIssue)
                //It avoids the activity finishes too quickly
                // and causes bad user experience
                .delay(DELAY_BEFORE_SENDING_REPORT, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GitHubResponse>() {
                    @Override
                    public void onCompleted() {
                        prepareUIComponentsAfterSubmit();
                    }

                    @Override
                    public void onError(Throwable e) {
                        prepareUIComponentsAfterSubmit();
                        toaster.toast(R.string.error_unable_to_send_report);
                        logger.printStackTrace(e);
                    }

                    @Override
                    public void onNext(GitHubResponse response) {
                        toaster.toast(R.string.bug_submitted);
                        finish();
                    }
                });
    }

    private boolean areFieldsNotEmpty() {
        return !issueTitleEditText.getText().toString().trim().isEmpty();
    }

    @NonNull
    private Issue getIssueFromUI() {
        EditText bugReportEditText = findViewById(R.id.bug_text);

        String issueTitle = issueTitleEditText.getText().toString();
        String bugReportText = bugReportEditText.getText().toString();

        return new Issue(issueTitle,
                bugReportText + "\n \n" + addMarkdownCodeBlock(deviceInfo));
    }

    private void prepareUIComponentsBeforeSubmit() {
        reportBugButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        //Adding elevation progressBar to old versions of Android
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setTranslationZ(progressBar, PROGRESS_BAR_ELEVATION);
        }
    }

    private void prepareUIComponentsAfterSubmit() {
        reportBugButton.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

}
