/*
 * Copyright Txus Ballesteros 2016 (@txusballesteros)
 *
 * This file is part of some open source application.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Contact: Txus Ballesteros <txus.ballesteros@gmail.com>
 */
package com.techindiana.school.parent.SlidingDeck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techindiana.school.parent.R;
import com.techindiana.school.parent.Vars.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SlidingDeckAdapter extends ArrayAdapter<SlidingDeckModel> {
    Context mContext;
    public SlidingDeckAdapter(Context context) {

        super(context, R.layout.sliding_item);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sliding_item, parent, false);
        }
        SlidingDeckModel item = getItem(position);
        view.setTag(item);
        ((TextView) view.findViewById(R.id.description)).setText(item.getDescription().trim());
        ((TextView) view.findViewById(R.id.name)).setText(item.getTitle().trim());
        String inputDPattern = "yyyy-MM-dd";
        String outputDPattern = "d MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputDPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputDPattern);

        Date sdate = null;

        // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
        try {
            sdate = inputFormat.parse(item.getNbDate());
           // tvDate.setText(outputFormat.format(sdate));
            ((TextView) view.findViewById(R.id.date)).setText(outputFormat.format(sdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }



        String inputTPattern = "HH:mm:ss";
        String outputTPattern = "hh:mm a";
        SimpleDateFormat inputTFormat = new SimpleDateFormat(inputTPattern);
        SimpleDateFormat outputTFormat = new SimpleDateFormat(outputTPattern);

        Date sT = null;

        // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
        try {
            sT = inputTFormat.parse(item.getNbTime());
         //   tvTime.setText(outputTFormat.format(sT));
            ((TextView) view.findViewById(R.id.time)).setText(outputTFormat.format(sT));
        } catch (ParseException e) {
            e.printStackTrace();
        }




     //   ((TextView) view.findViewById(R.id.date)).setText(item.getNbDate().trim());
     //   ((TextView) view.findViewById(R.id.time)).setText(item.getNbTime().trim());
        FrameLayout slidingDeckFly=(FrameLayout)view.findViewById(R.id.slidingDeckFly);
      /*  slidingDeckFly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), ActivityNoticeBoard.class);
                parent.getContext().startActivity(intent);
            }
        });*/
        ImageView avatar = (ImageView) view.findViewById(R.id.avatar);
//        Picasso.with(parent.getContext())
//                .load(Constant.webImgPath + item.getImage())
//                .placeholder(R.mipmap.logo)
//
//                .into(avatar);

        Glide.with(parent.getContext()).
                load(Constant.webImgPath + item.getImage()).
                placeholder(R.mipmap.splash_screen_logo).
                centerCrop().
                error(R.mipmap.splash_screen_logo).
                into(avatar);
        // .transform(new RoundedTransform())
        return view;
    }
}
