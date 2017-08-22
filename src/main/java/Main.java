import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.javascript.host.URL;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.AbstractScheduledService;

public class Main {
	static class Checker extends AbstractScheduledService
	{
		private final String site;
		public Checker(String site)
		{
			this.site = site;
		}
		@Override
		protected void runOneIteration() throws Exception {
			if (!testInet(site))
			{
				System.out.println("Disconnected, reconnecting.");
				post();
			}
			else
			{
				System.out.println("Still connected");
			}
		}
		@Override
		protected Scheduler scheduler() {
			return Scheduler.newFixedDelaySchedule(5, 5, TimeUnit.SECONDS);
		}
		
	}
	public static void main(String[] args) {
		Checker checker = new Checker("google.com");
		checker.startAsync();
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean testInet(String site) {
	    Socket sock = new Socket();
	    InetSocketAddress addr = new InetSocketAddress(site,80);
	    try {
	        sock.connect(addr,3000);
	        return true;
	    } catch (IOException e) {
	        return false;
	    } finally {
	        try {sock.close();}
	        catch (IOException e) {}
	    }
	}
	public static void post() throws Exception
	{
		final WebClient webClient = new WebClient();

	    WebRequest requestSettings = new WebRequest(new java.net.URL("1.1.1.1/login.html"), HttpMethod.POST);

	    requestSettings.setAdditionalHeader("Accept", "*/*");
	    requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	    requestSettings.setAdditionalHeader("Referer", "REFURLHERE");
	    requestSettings.setAdditionalHeader("Accept-Language", "en-US,en;q=0.8");
	    requestSettings.setAdditionalHeader("Accept-Encoding", "gzip,deflate,sdch");
	    requestSettings.setAdditionalHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
	    requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
	    requestSettings.setAdditionalHeader("Cache-Control", "no-cache");
	    requestSettings.setAdditionalHeader("Pragma", "no-cache");
	    requestSettings.setAdditionalHeader("Origin", "http://1.1.1.1/login.html");

	    requestSettings.setRequestBody("REQUESTBODY");

	    Page redirectPage = webClient.getPage(requestSettings);
	    webClient.close();
	}


}
