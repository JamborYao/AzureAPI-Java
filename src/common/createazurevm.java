package common;

import java.net.URI;
import java.util.ArrayList;

import java.util.HashMap;
// Imports for Exceptions
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import com.microsoft.windowsazure.exception.ServiceException;

import org.xml.sax.SAXException;

// Imports for Azure App Service management configuration
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.management.configuration.ManagementConfiguration;

// Service management imports for App Service Web Apps creation
import com.microsoft.windowsazure.management.websites.*;
import com.microsoft.windowsazure.management.websites.models.*;
// Imports for authentication
import com.microsoft.windowsazure.core.utils.KeyStoreType;
import com.microsoft.windowsazure.management.websites.models.WebSpaceNames;

public class createazurevm {

	private static String uri = "https://management.core.windows.net/";
	private static String subscriptionId = "5c99153f-be77-49ec-bbaa-107014e5dc69";
	private static String keyStoreLocation = "D:\\javajks.jks";
	private static String keyStorePassword = "123Aking";

	// Define web app parameter values.
	private static String webAppName = "jamborjavacrate";
	private static String domainName = ".azurewebsites.net";
	private static String webSpaceName = WebSpaceNames.EASTASIAWEBSPACE;
	private static String appServicePlanName = "jamborplan";

	public static void main(String[] args) throws IOException,
			URISyntaxException, ServiceException, ParserConfigurationException,
			SAXException, Exception {

		// Create web app
		getWebsiteConfig();

	}

	private static void createWebApp() throws Exception {

		// Specify configuration settings for the App Service management client.
		Configuration config = ManagementConfiguration.configure(new URI(uri),
				subscriptionId, keyStoreLocation, // Path to the JKS file
				keyStorePassword, // Password for the JKS file
				KeyStoreType.jks // Flag that you are using a JKS keystore
				);

		// Create the App Service Web Apps management client to call Azure APIs
		// and pass it the App Service management configuration object.
		WebSiteManagementClient webAppManagementClient = WebSiteManagementService
				.create(config);

		// Create an App Service plan for the web app with the specified
		// parameters.
		WebHostingPlanCreateParameters appServicePlanParams = new WebHostingPlanCreateParameters();
		appServicePlanParams.setName(appServicePlanName);
		appServicePlanParams.setSKU(SkuOptions.Free);
		webAppManagementClient.getWebHostingPlansOperations().create(
				webSpaceName, appServicePlanParams);

		// Set webspace parameters.
		WebSiteCreateParameters.WebSpaceDetails webSpaceDetails = new WebSiteCreateParameters.WebSpaceDetails();
		webSpaceDetails.setGeoRegion(GeoRegionNames.WESTUS);
		webSpaceDetails.setPlan(WebSpacePlanNames.VIRTUALDEDICATEDPLAN);
		webSpaceDetails.setName(webSpaceName);

		// Set web app parameters.
		// Note that the server farm name takes the Azure App Service plan name.
		WebSiteCreateParameters webAppCreateParameters = new WebSiteCreateParameters();
		webAppCreateParameters.setName(webAppName);
		webAppCreateParameters.setServerFarm(appServicePlanName);
		webAppCreateParameters.setWebSpace(webSpaceDetails);

		// Set usage metrics attributes.
		WebSiteGetUsageMetricsResponse.UsageMetric usageMetric = new WebSiteGetUsageMetricsResponse.UsageMetric();
		usageMetric.setSiteMode(WebSiteMode.Basic);
		usageMetric.setComputeMode(WebSiteComputeMode.Shared);

		// Define the web app object.
		ArrayList<String> fullWebAppName = new ArrayList<String>();
		fullWebAppName.add(webAppName + domainName);
		WebSite webApp = new WebSite();
		webApp.setHostNames(fullWebAppName);

		// Create the web app.
		WebSiteCreateResponse webAppCreateResponse = webAppManagementClient
				.getWebSitesOperations().create(webSpaceName,
						webAppCreateParameters);

		// Output the HTTP status code of the response; 200 indicates the
		// request succeeded; 4xx indicates failure.
		System.out.println("----------");
		System.out.println("Web app created - HTTP response "
				+ webAppCreateResponse.getStatusCode() + "\n");

		// Output the name of the web app that this application created.
		String shinyNewWebAppName = webAppCreateResponse.getWebSite().getName();
		System.out.println("----------\n");
		System.out.println("Name of web app created: " + shinyNewWebAppName
				+ "\n");
		System.out.println("----------\n");
	}

	public static void getWebsiteConfig() throws Exception {
		Configuration config = ManagementConfiguration.configure(new URI(uri),
				subscriptionId, keyStoreLocation, // Path to the JKS file
				keyStorePassword, // Password for the JKS file
				KeyStoreType.jks // Flag that you are using a JKS keystore
				);

		WebSiteManagementClient webAppManagementClient = WebSiteManagementService
				.create(config);
		/*WebSiteGetConfigurationResponse conResponse = (webAppManagementClient
				.getWebSitesOperations()).getConfiguration("eastasiawebspace",
						"JK123");
		*/
		WebSiteOperationsImpl webOperation = 
				(com.microsoft.windowsazure.management.websites.WebSiteOperationsImpl)
				(webAppManagementClient
				.getWebSitesOperations());
		WebSiteGetConfigurationResponse conResponse= webOperation.getConfiguration("eastasiawebspace",
				"JK123");
		HashMap<String, String> appSettings = conResponse.getAppSettings();
		System.out.println("appSettings size:: " + appSettings.size());
		for (String appSettingVal : appSettings.values()) {
			System.out.println(appSettingVal);
		}
		//WebSpacesGetResponse webspacedetail=webAppManagementClient.getWebSpacesOperations().get("eastasiawebspace");
		

	}
}
