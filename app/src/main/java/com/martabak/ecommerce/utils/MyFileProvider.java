package com.martabak.ecommerce.utils;

import androidx.core.content.FileProvider;

import com.martabak.ecommerce.R;

public class MyFileProvider extends FileProvider {
    public MyFileProvider() {
        super(R.xml.file_paths);
    }
}
