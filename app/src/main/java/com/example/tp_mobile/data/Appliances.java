package com.example.tp_mobile.data;

import com.example.tp_mobile.R;

public class Appliances {

    public static Integer getItemId(String name) {
        switch (name.trim().toLowerCase()) {
            case "aspirateur": return R.drawable.cleaner;
            case "chauffage": return R.drawable.heater;
            case "fer à repasser": return R.drawable.iron;
            case "machine à laver": return R.drawable.washing_machine;
        }
        return -1;
    }
}
