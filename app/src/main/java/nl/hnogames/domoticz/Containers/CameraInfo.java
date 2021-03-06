/*
 * Copyright (C) 2015 Domoticz
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package nl.hnogames.domoticz.Containers;

import org.json.JSONException;
import org.json.JSONObject;

public class CameraInfo {
    JSONObject jsonObject;
    int devices;
    String Name;
    String Address;
    String Username;
    String Password;
    String Port;
    boolean Enabled;
    String ImageURL;
    int idx;

    public CameraInfo(JSONObject row) throws JSONException {
        this.jsonObject = row;
        if (row.has("Name"))
            Name = row.getString("Name");
        if (row.has("Enabled") &&
                row.getString("Enabled").equals("true"))
            Enabled = true;
        else
            Enabled = false;
        if (row.has("Address"))
            Address = row.getString("Address");
        if (row.has("Password"))
            Password = row.getString("Password");
        if (row.has("Port"))
            Port = row.getString("Port");
        if (row.has("Username"))
            Username = row.getString("Username");
        if (row.has("ImageURL"))
            ImageURL = row.getString("ImageURL");
        idx = row.getInt("idx");
    }

    @Override
    public String toString() {
        return "CameraInfo{" +
                "idx=" + idx +
                ", Name='" + Name +
                "', Address='" + Address +
                "', ImageURL='" + ImageURL +
                ", Port='" + Port +
                ", Username='" + Username +
                ", Password='" + Password +
                ", FullURL='" + getFullURL() +
                ", Enabled=" + Enabled +
                "'}";
    }

    public String getName() {
        return Name;
    }

    public boolean getEnabled() {
        return Enabled;
    }

    public String getAddress() {
        return Address;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public String getPort() {
        return Port;
    }

    public int getIdx() {
        return idx;
    }

    public int getDevices() {
        return devices;
    }

    //get the full parsed URL for the ip camera image
    public String getFullURL() {
        String combinedUserName = "";
        if (getUsername() != null && getUsername().length() > 0) {
            combinedUserName += getUsername();
            if (getPassword() != null && getPassword().length() > 0)
                combinedUserName += ":" + getPassword();
        }
        String combinedUrl = "";
        if (combinedUserName.length() > 0)
            combinedUrl = combinedUserName + "@";

        if (getAddress() != null && !getAddress().startsWith("http://"))
            combinedUrl = "http://" + combinedUrl;

        combinedUrl += getAddress();

        if (getPort() != null && getPort().length() > 0)
            combinedUrl += ":" + getPort();

        combinedUrl += "/" + getImageURL();
        return replaceUserPassInURL(combinedUrl);
    }

    //Domoticz adds some username tags in the url, we have to replace them later
    private String replaceUserPassInURL(String url) {
        String combinedUrl = url;
        if (combinedUrl.indexOf("#USERNAME") >= 0)
            combinedUrl = combinedUrl.replace("#USERNAME", getUsername());
        if (combinedUrl.indexOf("#PASSWORD") >= 0)
            combinedUrl = combinedUrl.replace("#PASSWORD", getPassword());
        if (combinedUrl.indexOf("username") >= 0)
            combinedUrl = combinedUrl.replace("username", getUsername());
        if (combinedUrl.indexOf("password") >= 0)
            combinedUrl = combinedUrl.replace("password", getPassword());
        return combinedUrl;
    }
}