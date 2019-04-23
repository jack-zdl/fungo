package com.game.common.util.network;

import com.google.common.base.Charsets;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import java.util.ArrayList;


public final class HttpUtil {
//	private static final Logger logger = LogManager.getLogger();

	private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";

	private HttpUtil() {
	}

	public static final String httpClientPost(String url) {
		String result = "";
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		try {
			client.executeMethod(getMethod);
			result = getMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			getMethod.releaseConnection();
		}
		return result;
	}

	public static final String httpClientPost(String url, ArrayList<NameValuePair> list) {
		String result = "";
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		try {
			NameValuePair[] params = new NameValuePair[list.size()];
			for (int i = 0; i < list.size(); i++) {
				params[i] = list.get(i);
			}
			postMethod.addParameters(params);
			client.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
//			logger.error(e);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return result;
	}


	public static String postSSL(String url, String data, String certPath, String certPass) {
		HttpsURLConnection conn = null;
		OutputStream out = null;
		InputStream inputStream = null;
		BufferedReader reader = null;
		try {
			KeyStore clientStore = KeyStore.getInstance("PKCS12");
			clientStore.load(new FileInputStream(certPath), certPass.toCharArray());
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(clientStore, certPass.toCharArray());
			KeyManager[] kms = kmf.getKeyManagers();
			SSLContext sslContext = SSLContext.getInstance("TLSv1");

			sslContext.init(kms, null, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			URL _url = new URL(url);
			conn = (HttpsURLConnection) _url.openConnection();

			conn.setConnectTimeout(25000);
			conn.setReadTimeout(25000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);

			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
			conn.connect();

			out = conn.getOutputStream();
			out.write(data.getBytes(Charsets.UTF_8));
			out.flush();

			inputStream = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(inputStream);
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
}
