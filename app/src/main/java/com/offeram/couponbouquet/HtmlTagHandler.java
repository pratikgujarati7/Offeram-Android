package com.offeram.couponbouquet;

import android.text.Editable;
import android.text.Html;
import android.util.Log;

import org.xml.sax.XMLReader;

/**
 * Created by admin on 29-Sep-17.
 */

public class HtmlTagHandler implements Html.TagHandler {
    boolean first = true;
    String parent = null;
    int index = 1;

    @Override
    public void handleTag(boolean opening, String tag, Editable output,
                          XMLReader xmlReader) {

        if (tag.equals("ul")) parent = "ul";
        else if (tag.equals("ol")) parent = "ol";
        if (tag.equals("li")) {
//            Log.e("In HtmlTagHandler", "In If -> LI");
            if (parent.equals("ul")) {
//                Log.e("In HtmlTagHandler", "In If -> UL");
                if (first) {
                    output.append("\n○ ");
                    first = false;
                } else {
                    first = true;
                }
            } else {
//                Log.e("In HtmlTagHandler", "In ELse -> LI");
                if (first) {
//                    Log.e("In HtmlTagHandler", "In If -> OL");
                    output.append("\n" + index + ". ");
                    first = false;
                    index++;
                } else {
                    first = true;
                }
            }
        }
    }

}