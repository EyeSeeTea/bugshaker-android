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
package com.github.stkent.bugshaker.github.api;

import android.support.annotation.NonNull;

import com.github.stkent.bugshaker.github.BugReportProvider;
import com.github.stkent.bugshaker.github.GitHubConfiguration;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;


public class GitHubApiProvider implements BugReportProvider {

    @NonNull
    private final GitHubConfiguration gitHubConfiguration;

    public GitHubApiProvider(
            @NonNull final GitHubConfiguration gitHubConfiguration) {
        this.gitHubConfiguration = gitHubConfiguration;
    }


    @Override
    public Observable<GitHubResponse> addIssue(@NonNull Issue newIssue) {

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(GitHubApi.BASE_URL)
                .build();

        String owner = gitHubConfiguration.getOwnerName();
        String repo = gitHubConfiguration.getRepositoryName();
        String token = gitHubConfiguration.getAuthenticationToken();


        GitHubApi gitHubApi = retrofit.create(GitHubApi.class);

        return gitHubApi.addIssue(newIssue, owner, repo, "token " + token);
    }
}
