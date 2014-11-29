package com.grigorio.rzd.utils;

import javafx.print.PrinterJob;
import javafx.scene.web.WebView;

/**
 * Class to print a content of a web view
 */
public class WebViewPrinter {
    public static void print(WebView wvToPrint) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(wvToPrint.getScene().getWindow())) {
            wvToPrint.getEngine().print(job);
            job.endJob();
        }
    }
}
