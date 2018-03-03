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


import static com.github.stkent.bugshaker.utilities.ObjectUtils.requireNonNull;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public final class GitHubConfiguration implements Parcelable {
    private static final String PROPERTY_REQUIRED =
            "Property required: ";

    private @NonNull
    final String ownerName;
    private @NonNull
    final String repositoryName;
    private @NonNull
    final String authenticationToken;

    public GitHubConfiguration(@NonNull String ownerName, @NonNull String repositoryName,
            @NonNull String authenticationToken) {
        this.ownerName = requireNonNull(ownerName, PROPERTY_REQUIRED + "ownerName");
        this.repositoryName = requireNonNull(repositoryName, PROPERTY_REQUIRED + "repositoryName");
        this.authenticationToken = requireNonNull(authenticationToken,
                PROPERTY_REQUIRED + "authenticationToken");
    }

    @NonNull
    public String getOwnerName() {
        return ownerName;
    }

    @NonNull
    public String getRepositoryName() {
        return repositoryName;
    }

    @NonNull
    public String getAuthenticationToken() {
        return authenticationToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ownerName);
        dest.writeString(this.repositoryName);
        dest.writeString(this.authenticationToken);
    }

    protected GitHubConfiguration(Parcel in) {
        this.ownerName = in.readString();
        this.repositoryName = in.readString();
        this.authenticationToken = in.readString();
    }

    public static final Parcelable.Creator<GitHubConfiguration> CREATOR =
            new Parcelable.Creator<GitHubConfiguration>() {
                @Override
                public GitHubConfiguration createFromParcel(Parcel source) {
                    return new GitHubConfiguration(source);
                }

                @Override
                public GitHubConfiguration[] newArray(int size) {
                    return new GitHubConfiguration[size];
                }
            };

    @Override
    public String toString() {
        return "GitHubConfiguration{"
                + "ownerName='" + ownerName + '\''
                + ", repositoryName='" + repositoryName + '\''
                + ", authenticationToken='" + authenticationToken + '\''
                + '}';
    }
}
