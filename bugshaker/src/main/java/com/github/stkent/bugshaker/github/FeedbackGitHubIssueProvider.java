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


import static com.github.stkent.bugshaker.github.ReportBugActivity.GIT_HUB_CONFIGURATION_EXTRA;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.bugshaker.flow.FeedbackProvider;


public final class FeedbackGitHubIssueProvider implements FeedbackProvider {

    @NonNull
    private final GitHubConfiguration gitHubConfiguration;

    public FeedbackGitHubIssueProvider(
            @NonNull GitHubConfiguration gitHubConfiguration) {
        this.gitHubConfiguration = gitHubConfiguration;
    }

    @Override
    public void submitFeedback(@NonNull Activity activity,
            @Nullable Uri screenShotUri) {

        final Intent reportActivityIntent = new Intent(activity, ReportBugActivity.class);
        reportActivityIntent.setData(screenShotUri);
        reportActivityIntent.putExtra(GIT_HUB_CONFIGURATION_EXTRA, gitHubConfiguration);
        activity.startActivity(reportActivityIntent);
    }
}
