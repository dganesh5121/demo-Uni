package com.github.mikephil.charting.interfaces.dataprovider;

import com.github.mikephil.charting.data.CombinedData;

/**
 * Created by philipp on 11/06/16.
 */
public interface CombinedDataProvider extends  BarDataProvider, BubbleDataProvider {

    CombinedData getCombinedData();
}
