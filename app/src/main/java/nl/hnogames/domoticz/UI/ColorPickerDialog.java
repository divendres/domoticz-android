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

package nl.hnogames.domoticz.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ColorInt;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.OnColorListener;
import com.larswerkman.lobsterpicker.sliders.LobsterOpacitySlider;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;

import nl.hnogames.domoticz.Containers.DevicesInfo;
import nl.hnogames.domoticz.R;
import nl.hnogames.domoticz.Utils.SharedPrefUtil;

public class ColorPickerDialog implements DialogInterface.OnDismissListener {

    private final MaterialDialog.Builder mdb;
    private DismissListener dismissListener;
    private Context mContext;
    private LobsterPicker lobsterPicker;
    private LobsterShadeSlider shadeSlider;
    private SharedPrefUtil mSharedPrefs;
    private int idx;

    public ColorPickerDialog(Context mContext, int idx) {
        this.mContext = mContext;
        mSharedPrefs=new SharedPrefUtil(mContext);
        this.idx=idx;
        mdb = new MaterialDialog.Builder(mContext);
        boolean wrapInScrollView = true;
        mdb.customView(R.layout.dialog_color, wrapInScrollView)
                .positiveText(android.R.string.ok);
        mdb.dismissListener(this);
    }

    public void show() {
        mdb.title(mContext.getString(R.string.choose_color));
        final MaterialDialog md = mdb.build();
        View view = md.getCustomView();

        lobsterPicker = (LobsterPicker) view.findViewById(R.id.lobsterpicker);
        shadeSlider = (LobsterShadeSlider) view.findViewById(R.id.shadeslider);
        lobsterPicker.addDecorator(shadeSlider);
        lobsterPicker.setColorHistoryEnabled(true);
        lobsterPicker.setHistory(mSharedPrefs.getPreviousColor(idx));
        lobsterPicker.setColorPosition(mSharedPrefs.getPreviousColorPosition(idx));

        lobsterPicker.addOnColorListener(new OnColorListener() {
            @Override
            public void onColorChanged(int color) {
            }

            @Override
            public void onColorSelected(int color) {
                mSharedPrefs.savePreviousColor(idx, color, lobsterPicker.getColorPosition());
                dismissListener.onChangeColor(color);
            }
        });

        md.show();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (dismissListener != null)
            dismissListener.onDismiss(lobsterPicker.getColor());
    }

    public void onDismissListener(DismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    public interface DismissListener {
        void onDismiss(int color);
        void onChangeColor(int color);
    }
}