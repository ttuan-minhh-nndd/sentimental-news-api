package com.example.uit_mobileapp_lab03.logic;

import android.content.Context;
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier;
import java.io.IOException;

public class SentimentAnalyzer {
    // global variable for storing the AI brain after having loaded
    private NLClassifier classifier;

    public SentimentAnalyzer(Context context) {
        try {
            // find the targeted file inside the assets folder
            classifier = NLClassifier.createFromFile(context, "sentiment_analysis.tflite");
        } catch (IOException e) {
            // handle error if the file cannot be loaded
            e.printStackTrace();
        }
    }

    // this function is called from Adapter or PagingSource
    public float classifyText(String text) {
        if (classifier == null) {
            return classifier.classify(text).get(0).getScore();
        }
        return 0.0f;
    }
}
