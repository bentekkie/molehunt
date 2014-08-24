package com.dragonfruit.codinghouse.molehunt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridLayout;




public class SettingsActivity extends Activity {
    GridLayout main;

	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setupVars();
        setupUI();

    }

    private void setupUI() {
        main.setColumnCount(5);

    }

    private void setupVars() {
        main = new GridLayout(this);

    }
}