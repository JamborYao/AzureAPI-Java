package common;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.management.configuration.*;
import com.microsoft.windowsazure.management.compute.*;
import com.microsoft.windowsazure.management.compute.models.VirtualMachineOSImageListResponse.VirtualMachineOSImage;
import com.microsoft.windowsazure.management.compute.models.VirtualMachineVMImageListResponse.VirtualMachineVMImage;
import java.util.ArrayList;
import java.io.File;
import java.lang.String;

public class images {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String publishSettingsFileName = "src/common/globalazure.publishsettings";
		String subscriptionId = "5c99153f-be77-49ec-bbaa-107014e5dc69";
		File file = new File(publishSettingsFileName);
		if (!file.exists()) {
			System.err.println("File not found: " + publishSettingsFileName);
			System.exit(1);
		}

		try {
			Configuration config = PublishSettingsLoader
					.createManagementConfiguration(publishSettingsFileName,
							subscriptionId);
			ComputeManagementClient computeManagementClient = ComputeManagementService
					.create(config);

			ArrayList<VirtualMachineOSImage> osImages = computeManagementClient
					.getVirtualMachineOSImagesOperations().list().getImages();

			int i = 0;
			String m = "no";
			while (osImages.size() > i) {
				if (osImages.get(i).getName()
						.equals("buildimage-20150529-207092")) {
					System.out.println("os images has my customer image");
					m = "yes";
					break;
				}
				i++;
				
			}
			if (m.equals("no")) {
				System.out.println("os images don't find");
			}
			ArrayList<VirtualMachineVMImage> vmImages = computeManagementClient
					.getVirtualMachineVMImagesOperations().list().getVMImages();
			int j = 0;
			while (vmImages.size() > j) {
				if (vmImages.get(j).getName()
						.equals("buildimage-20150529-207092")) {
					System.out.println("vm images has my customer image");
					break;
				}

				j++;
			}
			System.out.println("finished!");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
