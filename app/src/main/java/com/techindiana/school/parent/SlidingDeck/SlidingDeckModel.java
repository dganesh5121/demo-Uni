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

public class SlidingDeckModel {
    private String id;
    private String category;
    private String title;
    private String description;
    private String nbDate;
    private String nbTime;
    private String image;


    public SlidingDeckModel(String id, String category, String title,String description,  String nbDate,
                            String nbTime, String image) {

        this.id = id;
        this.category = category;
        this.title = title;
        this.description = description;
        this.nbDate = nbDate;
        this.nbTime = nbTime;
        this.image = image;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNbDate() {
        return nbDate;
    }

    public void setNbDate(String nbDate) {
        this.nbDate = nbDate;
    }

    public String getNbTime() {
        return nbTime;
    }

    public void setNbTime(String nbTime) {
        this.nbTime = nbTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
