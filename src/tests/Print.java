package tests;

import java.util.ArrayList;

import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.nip.api.NipsRequest;
import com.printer.impl.Printer;

public class Print {
	static Logger logger = Logger.getLogger(Print.class);

	public static void main(String[] args) {
		Printer p = new Printer();
		final String path = "/users/mac/a.eps";
		try {
			p.print(path, new PrintJobListener() {
				@Override
				public void printDataTransferCompleted(PrintJobEvent arg0) {

				}

				@Override
				public void printJobCanceled(PrintJobEvent arg0) {
					logger.error("print canceled");
				}

				@Override
				public void printJobCompleted(PrintJobEvent arg0) {
					logger.error("printed:" + path);
				}

				@Override
				public void printJobFailed(PrintJobEvent arg0) {
					logger.error("print failed:" + path);
				}

				@Override
				public void printJobNoMoreEvents(PrintJobEvent arg0) {

				}

				@Override
				public void printJobRequiresAttention(PrintJobEvent arg0) {

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
