package com.mog.authserver.gcs.util;

public class GcsURLParser {
    private static final String GCS_URL = "https://storage.googleapis.com/";

    public static String generateURL(String bucketName, String filename) {
        return GCS_URL + bucketName + "/" + filename;
    }

    public static String parseFileName(String url) {
        String[] split = url.split("/");
        return split[4];
    }
}
