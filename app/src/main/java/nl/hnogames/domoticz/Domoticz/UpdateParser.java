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

package nl.hnogames.domoticz.Domoticz;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import nl.hnogames.domoticz.Interfaces.JSONParserInterface;
import nl.hnogames.domoticz.Interfaces.UpdateReceiver;

public class UpdateParser implements JSONParserInterface {

    private static final String TAG = UpdateParser.class.getSimpleName();
    private UpdateReceiver receiver;

    public UpdateParser(UpdateReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            String version = "";
            if (response.has("revision"))
                version = response.getString("revision");
            if (response.has("HaveUpdate") && !response.getBoolean("HaveUpdate"))
                version = "";

            receiver.onReceiveUpdate(version);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Exception error) {
        Log.e(TAG, "VersionParser of JSONParserInterface exception");
        receiver.onError(error);
    }
}