package org.egx.scraping.IO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Data
@AllArgsConstructor
public class Stock {

    String reutersCode;
    double currPrice;
    double rateOfChange;
    double percentageOfChange;
    double open;
    double prevClose;
    double highest;
    double lowest;
    double volume;
    double value;
    String date;
    String time;
    public Stock(double currPrice, double rateOfChange, double percentageOfChange, double open, double prevClose, double highest, double lowest, double volume, double value){
        this.currPrice = currPrice;
        this.rateOfChange = rateOfChange;
        this.percentageOfChange = percentageOfChange;
        this.open = open;
        this.prevClose = prevClose;
        this.highest = highest;
        this.lowest = lowest;
        this.volume = volume;
        this.value = value;
        this.date = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
        this.time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}
