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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.github.stkent.bugshaker.R;

public class ReportBugActivity extends AppCompatActivity {

    public static final String GIT_HUB_CONFIGURATION_EXTRA = "GIT_HUB_CONFIGURATION_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_report_bug);

        EditText editText = findViewById(R.id.bug_text);

        final GitHubConfiguration configuration = getIntent().getParcelableExtra(
                GIT_HUB_CONFIGURATION_EXTRA);

        String textBody = configuration.toString() + " \n "
                + getIntent().getData().toString();

        editText.setText(textBody);
    }
}
