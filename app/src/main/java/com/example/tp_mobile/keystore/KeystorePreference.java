package com.example.tp_mobile.keystore;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public class KeystorePreference {

    private SharedPreferences sharedPreferences;

    public KeystorePreference(Context context) throws Exception {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    // Enregistrer une clé
    public void saveEncryptionSecret(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    // Récupérer une clé
    public String getEncryptionSecret(String key) {
        return sharedPreferences.getString(key, null);
    }

    // Supprimer une clé
    public void deleteEncryptionSecret(String key) {
        sharedPreferences.edit().remove(key).apply();
    }
}
