package com.printer.task;

import java.util.ArrayList;

import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.nip.api.NipsRequest;
import com.printer.impl.Printer;

public class Job extends NipsRequest {
	static Logger logger = Logger.getLogger(Job.class);

	String id = "";
	Image doc = null;
	Address addr = null;

	public Job(JSONObject data) {
		this.id = data.get("_id").toString();
		JSONObject recver = (JSONObject) data.get("recver");
		this.addr = new Address(recver.get("addr").toString(), recver.get(
				"mobile").toString());
		JSONObject image = (JSONObject) data.get("image");
		this.doc = new Image(this.id, image.get("out").toString(),
				Long.valueOf(image.get("time").toString()));
	}

	public void changeState(int stat, String msg) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("rid", id));
		params.add(new BasicNameValuePair("sta", String.valueOf(stat)));
		params.add(new BasicNameValuePair("err", msg));
		sq(NipsRequest.BASE_URL + "/api/jobs/print_sta", params);
	}

	public void execute() {
		final String path = this.doc.download();
		if (path == null) {
			changeState(2, "download failed");
			logger.info("download failed:" + doc.id);
			return;
		}
		logger.info("path:" + path);
		Printer p = new Printer();
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
			logger.error(e.getMessage());
		}
	}

}
