package com.example.firestorechatjava.listner;

import java.io.File;
import java.util.List;

public interface IImageCompressTaskListener {

    void onComplete(List<File> compressed);
    void onError(Throwable error);
}