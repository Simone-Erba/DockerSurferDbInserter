import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;

import org.json.JSONObject;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

class Main {

	public static void main(String[] args) {

		searchApi("ubuntu");
		searchHttp("ngix");
		pullHttp("stuartmarsden/docker-twisted");

	}

	private static void pullHttp(String nome) {
		try {
			String url2 = "https://auth.docker.io/token?service=registry.docker.io&scope=repository:" + nome + ":pull";
			URL obj2;

			obj2 = new URL(url2);

			HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();

			// optional default is GET
			con2.setRequestMethod("GET");
			// add request header
			// con.setRequestProperty("username", "simoneerba");
			System.out.println("content       " + con2.getHeaderField("Www-Authenticate"));
			String s2 = con2.getResponseMessage();
			InputStream in2 = con2.getInputStream();
			String encoding2 = con2.getContentEncoding();
			encoding2 = encoding2 == null ? "UTF-8" : encoding2;
			String body2 = IOUtils.toString(in2, encoding2);
			System.out.println(body2);
			int responseCode2 = con2.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url2);
			System.out.println("\nmessaggio : " + s2);
			System.out.println("Response Code : " + responseCode2);
			JSONObject json = new JSONObject(body2);
			String token = json.getString("token");
			System.out.println(token);
			String url3 = "https://registry-1.docker.io/v2/" + nome + "/manifests/latest";
			URL obj3;

			obj3 = new URL(url3);

			HttpURLConnection con3 = (HttpURLConnection) obj3.openConnection();

			// optional default is GET
			con3.setRequestMethod("GET");
			// add request header
			// con.setRequestProperty("username", "simoneerba");
			con3.setRequestProperty("Authorization", "Bearer " + token);
			System.out.println("content       " + con3.getHeaderField("Www-Authenticate"));

			String s3 = con3.getResponseMessage();
			InputStream in3 = con3.getInputStream();
			String encoding3 = con3.getContentEncoding();
			encoding3 = encoding3 == null ? "UTF-8" : encoding3;
			String body3 = IOUtils.toString(in3, encoding3);
			System.out.println(body3);
			int responseCode3 = con3.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url3);
			System.out.println("\nmessaggio : " + s3);
			System.out.println("Response Code : " + responseCode3);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void searchHttp(String string) {
		try {
			String url = "https://index.docker.io/v1/search?q=library/*&page=1&n=100";
			URL obj;

			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");
			// add request header
			// con.setRequestProperty("username", "simoneerba");
			System.out.println("content       " + con.getHeaderField("Www-Authenticate"));
			String s = con.getResponseMessage();
			InputStream in1 = con.getInputStream();
			String encoding1 = con.getContentEncoding();
			encoding1 = encoding1 == null ? "UTF-8" : encoding1;
			String body1 = IOUtils.toString(in1, encoding1);
			System.out.println(body1);
			JSONObject json1 = new JSONObject(body1);
			String token1 = json1.getJSONArray("results").toString();
			System.out.println(token1);
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("\nmessaggio : " + s);
			System.out.println("Response Code : " + responseCode);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void searchApi(String query) {
		DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
				.withDockerHost("tcp://localhost:2375").withDockerTlsVerify(false)
				// .withDockerCertPath("/Users/leonardo/.docker/machine/machines/docker-1.11.2")
				// .withDockerConfig("/home/user/.docker")
				.withApiVersion("1.23").withRegistryUrl("https://index.docker.io/v2/")
				.withRegistryUsername("simoneerba").withRegistryPassword("").withRegistryEmail("dockeruser@github.com")
				.build();
		DockerClient docker = DockerClientBuilder.getInstance(config).build();
		Info info = docker.infoCmd().exec();
		System.out.print(info);
		List<SearchItem> dockerSearch = docker.searchImagesCmd(query).exec();
		System.out.println("Search returned" + dockerSearch.toString());
		Iterator<SearchItem> i = dockerSearch.iterator();
		while (i.hasNext()) {
			SearchItem s = i.next();

			System.out.println(s.getName());
		}

	}
}