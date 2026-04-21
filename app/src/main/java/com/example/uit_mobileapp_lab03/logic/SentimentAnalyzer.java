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
        float[] scores = analyze(text);

        // Assuming index 1 is the "Positive" label in the tflite model
        if (scores.length > 1) {
            return scores[1];
        }
        return 0.0f;
    }

    public float[] analyze(String textToAnalyze) {
        // 1. Check if the classifier was loaded successfully
        if (classifier == null) {
            return new float[]{0.5f, 0.5f}; // return neutral/zero scores if model failed
        }

        // 2. Run the model on the input text
        // This returns a list of categories (0 - negative, 1 - positive)
        var results = classifier.classify(textToAnalyze);

        // 3. Convert the results into a float array
        float[] scores = new float[results.size()];
        for (int i = 0; i < results.size(); i++) {
            scores[i] = results.get(i).getScore();
        }

        return scores;
    }
}
