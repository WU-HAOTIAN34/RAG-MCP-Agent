package com.wht.ai2025.constant;

import java.io.File;

public interface CommonConstant {

    public static final String TRAVELER_KNOWLEDGE = "TravelerKnowledge";
    public static final String FILE_FOLDER = System.getProperty("user.dir")
            + File.separator + "tmp"
            + File.separator + "file";
    public static final String DOWNLOAD_FOLDER = System.getProperty("user.dir")
            + File.separator + "tmp"
            + File.separator + "download";
    public static final String PDF_FOLDER = System.getProperty("user.dir")
            + File.separator + "tmp"
            + File.separator + "pdf";
}
